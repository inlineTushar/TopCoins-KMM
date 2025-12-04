package com.tushar.data.keyprovider

import android.content.Context
import android.os.Build
import java.io.ByteArrayInputStream
import java.util.Properties
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Android implementation of SecureKeyProvider.
 *
 * Uses encrypted key from androidMain/res/raw/key.properties that is decrypted at runtime
 * using AES-256-GCM encryption with a key derived from app version.
 *
 * Key file location: common/data/src/androidMain/res/raw/key.properties
 * Encryption: Handled by EncryptionPlugin during build
 */
actual class SecureKeyProvider(
    private val context: Context
) : KeyProvider {

    private val properties: Properties by lazy {
        decryptKeyProperties()
    }

    actual override fun get(key: String): String? {
        return try {
            properties.getProperty(key)
        } catch (e: Exception) {
            throw SecurityException("Failed to retrieve $key", e)
        }
    }

    private fun decryptKeyProperties(): Properties {
        return try {
            val (versionCode, versionName) = getAppVersion()
            decryptProperties(versionCode, versionName)
        } catch (e: Exception) {
            throw SecurityException("Failed to decrypt key properties", e)
        }
    }

    private fun decryptProperties(versionCode: Int, versionName: String): Properties {
        val masterKey = generateMasterKey(versionCode, versionName).toByteArray(Charsets.UTF_8)
        val ivSeed = generateIV(versionCode, versionName).toByteArray(Charsets.UTF_8)

        val key = SecretKeySpec(masterKey, "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, ivSeed))

        val resourceId = context.resources.getIdentifier(
            "key",
            "raw",
            context.packageName
        )

        if (resourceId == 0) {
            throw SecurityException("Encrypted key file not found in res/raw/key.properties")
        }

        val encryptedBytes = context.resources.openRawResource(resourceId).use {
            it.readBytes()
        }

        val decryptedBytes = cipher.doFinal(encryptedBytes)

        val properties = Properties()
        properties.load(ByteArrayInputStream(decryptedBytes))

        return properties
    }

    private fun getAppVersion(): Pair<Int, String> {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode
            }
        val versionName = packageInfo.versionName ?: "1.0.0"
        return Pair(versionCode, versionName)
    }

    private fun generateMasterKey(versionCode: Int, versionName: String): String {
        val seed =
            "$versionCode?$versionName-TopCOInS.SeCuRe_KeY-GeNeRaToR!2024@CoIn#CaP%EnCrYpT&HaSh*PrOtEcT"
        return mixer(seed).substring(0, 32)
    }

    private fun generateIV(versionCode: Int, versionName: String): String {
        val seed = "$versionName@$versionCode-TopCOInS!IvGeNeRaToR#2024&SeCuRe%EnCrYpT"
        return mixer(seed).substring(0, 12)
    }

    private fun mixer(seed: String): String {
        var leftIndex = 0
        var rightIndex = seed.length - 1
        val key = StringBuilder()

        while (leftIndex < rightIndex) {
            key.append(seed[leftIndex]).append(seed[rightIndex])
            leftIndex++
            rightIndex--
        }

        return key.toString()
    }
}
