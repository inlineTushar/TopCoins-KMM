package com.tushar.data.keyprovider

import android.content.Context
import android.os.Build
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.ByteArrayInputStream
import java.util.Properties
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The class is used for decrypt the keys during runtime
 * that is encrypted during build time by EncryptFileTask.
 * Using AES-256-GCM encryption with a key derived from app version.
 */
@Singleton
class SecureKeyProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : KeyProvider {

    private val properties: Properties by lazy {
        decryptKeyProperties()
    }

    /**
     * Returns the decrypted `key` value.
     *
     * @return The authentication key, or null if not found
     * @throws SecurityException if decryption fails
     */
    override operator fun get(key: String): String? {
        return try {
            properties.getProperty(key)
        } catch (e: Exception) {
            throw SecurityException("Failed to retrieve $key", e)
        }
    }

    /**
     * Decrypts the key.properties file from res/raw.
     *
     * @return Decrypted Properties object
     * @throws SecurityException if decryption fails
     */
    private fun decryptKeyProperties(): Properties {
        return try {
            val (versionCode, versionName) = getAppVersion()
            decryptProperties(versionCode, versionName)
        } catch (e: Exception) {
            throw SecurityException("Failed to decrypt key properties", e)
        }
    }

    /**
     * Decrypts properties using AES-256-GCM.
     */
    private fun decryptProperties(versionCode: Int, versionName: String): Properties {
        // Generate decryption key from app version
        val masterKey = generateMasterKey(versionCode, versionName).toByteArray(Charsets.UTF_8)
        val ivSeed = generateIV(versionCode, versionName).toByteArray(Charsets.UTF_8)

        val key = SecretKeySpec(masterKey, "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, ivSeed))

        // Read encrypted file from res/raw
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

        // Decrypt
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        // Load properties
        val properties = Properties()
        properties.load(ByteArrayInputStream(decryptedBytes))

        return properties
    }

    /**
     * Gets the current app version information from PackageManager.
     */
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

    /**
     * Generates master key from version information.
     * Must match the encryption algorithm in EncryptFileTask.
     */
    private fun generateMasterKey(versionCode: Int, versionName: String): String {
        val seed =
            "$versionCode?$versionName-TopCOInS.SeCuRe_KeY-GeNeRaToR!2024@CoIn#CaP%EnCrYpT&HaSh*PrOtEcT"
        return mixer(seed).substring(0, 32)
    }

    /**
     * Generates initialization vector from version information.
     * Must match the encryption algorithm in EncryptFileTask.
     */
    private fun generateIV(versionCode: Int, versionName: String): String {
        val seed = "$versionName@$versionCode-TopCOInS!IvGeNeRaToR#2024&SeCuRe%EnCrYpT"
        return mixer(seed).substring(0, 12) // GCM IV length = 12 bytes
    }

    /**
     * Mixer algorithm that interleaves characters from both ends of seed.
     * Must match the encryption algorithm in EncryptFileTask.
     */
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

    companion object Companion {
        const val KEY_COIN_AUTH = "COIN_AUTH_KEY"
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class KeyProviderModule {
    @Binds
    @Singleton
    abstract fun bind(real: SecureKeyProvider): KeyProvider
}
