package com.tushar.security.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Task to encrypt a file using AES/GCM encryption.
 * The master key and IV are generated from the app's version information.
 */
abstract class EncryptFileTask : DefaultTask() {

    @get:Input
    abstract val inputFileName: Property<String>

    @get:Input
    abstract val versionCode: Property<Int>

    @get:Input
    abstract val versionName: Property<String>

    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        group = "encryption"
        description = "Encrypts a properties file using AES/GCM encryption"
    }

    @TaskAction
    fun encryptFile() {
        val masterKey = generateMasterKey().toByteArray(Charsets.UTF_8)
        val ivSeed = generateIV().toByteArray(Charsets.UTF_8)

        val key = SecretKeySpec(masterKey, "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, ivSeed))

        val inputBytes = inputFile.get().asFile.readBytes()
        val outputBytes = cipher.doFinal(inputBytes)

        val output = outputFile.get().asFile
        output.parentFile.mkdirs()
        output.writeBytes(outputBytes)

        logger.lifecycle("Encrypted ${inputFile.get().asFile.name} to ${output.absolutePath}")
    }

    private fun generateMasterKey(): String {
        val seed =
            "${versionCode.get()}?${versionName.get()}-TopCOInS.SeCuRe_KeY-GeNeRaToR!2024@CoIn#CaP%EnCrYpT&HaSh*PrOtEcT"
        return mixer(seed).substring(0, 32)
    }

    private fun generateIV(): String {
        val seed =
            "${versionName.get()}@${versionCode.get()}-TopCOInS!IvGeNeRaToR#2024&SeCuRe%EnCrYpT"
        return mixer(seed).substring(0, 12) // GCM IV length needs to be 12 bytes
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
