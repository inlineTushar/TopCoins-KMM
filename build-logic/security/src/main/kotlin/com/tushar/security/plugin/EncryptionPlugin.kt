package com.tushar.security.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.tushar.security.task.EncryptFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

/**
 * Plugin to set up file encryption tasks for Android modules.
 * This plugin can be applied to both application and library modules,
 * including KMM modules using the Kotlin Multiplatform plugin.
 */
class EncryptionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Register the encryption task
            val encryptTask = tasks.register<EncryptFileTask>("encryptKeyProperties") {
                // Default configuration - can be overridden in the module's build.gradle.kts
                inputFileName.set("key.properties")

                // Set up input file from secret directory in root project
                inputFile.set(rootProject.layout.projectDirectory.file("secret/${inputFileName.get()}"))
            }

            // Configure the task after ALL projects are evaluated
            gradle.projectsEvaluated {
                val appExtension = target.extensions.findByType(ApplicationExtension::class.java)
                val libraryExtension = target.extensions.findByType(LibraryExtension::class.java)
                val isKmpModule = target.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

                when {
                    appExtension != null -> {
                        // For application modules, get version from defaultConfig
                        // Use src/main/res/raw for app modules (not KMM)
                        val resRawDir = target.layout.projectDirectory.dir("src/main/res/raw")
                        val vCode = appExtension.defaultConfig.versionCode ?: 1
                        val vName = appExtension.defaultConfig.versionName ?: "1.0.0"

                        encryptTask.configure {
                            versionCode.set(vCode)
                            versionName.set(vName)
                            outputFile.set(resRawDir.file("key.properties"))
                        }
                        logger.lifecycle("Encryption [${target.name}]: Configured for application module - versionCode: $vCode, versionName: $vName")
                    }

                    libraryExtension != null -> {
                        // For library modules (including KMM)
                        configureForLibraryModule(target, encryptTask, isKmmModule = isKmpModule)
                    }

                    else -> {
                        // For non-Android modules
                        logger.warn("Encryption [${target.name}]: Android extension not found. Encryption requires an Android module.")
                    }
                }
            }

            // Hook into preBuild for application modules
            pluginManager.withPlugin("com.android.application") {
                tasks.named("preBuild").configure {
                    dependsOn(encryptTask)
                }
            }

            // Hook into preBuild for legacy library modules
            pluginManager.withPlugin("com.android.library") {
                tasks.named("preBuild").configure {
                    dependsOn(encryptTask)
                }
            }

            // For KMP Android library modules, hook into compile tasks after evaluation
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                afterEvaluate {
                    // Find any Android resource or compile task to depend on
                    val preBuildTask = tasks.findByName("preBuild")
                    val prepareTask = tasks.findByName("prepareKotlinIdeaImport")

                    if (preBuildTask != null) {
                        preBuildTask.dependsOn(encryptTask)
                    } else {
                        // Hook into Android resource generation tasks
                        tasks.matching { task ->
                            task.name.contains("generateDebugResources") ||
                            task.name.contains("generateReleaseResources") ||
                            task.name.contains("mergeDebugResources") ||
                            task.name.contains("mergeReleaseResources")
                        }.configureEach {
                            dependsOn(encryptTask)
                        }

                        // Also hook into compile tasks as fallback
                        tasks.matching { task ->
                            task.name.startsWith("compile") && task.name.contains("Kotlin")
                        }.configureEach {
                            dependsOn(encryptTask)
                        }
                    }

                    logger.lifecycle("Encryption [${target.name}]: Configured task dependencies for KMP Android library")
                }
            }
        }
    }

    /**
     * Configure encryption for library modules (both legacy and KMP).
     */
    private fun Project.configureForLibraryModule(
        target: Project,
        encryptTask: org.gradle.api.tasks.TaskProvider<EncryptFileTask>,
        isKmmModule: Boolean
    ) {
        // For library modules, try to find app module version or use defaults
        val appProject = rootProject.subprojects.find {
            it.plugins.hasPlugin("com.android.application")
        }

        val versionCodeValue: Int
        val versionNameValue: String

        if (appProject != null) {
            val appExt = appProject.extensions.findByType(ApplicationExtension::class.java)
            versionCodeValue = appExt?.defaultConfig?.versionCode ?: 1
            versionNameValue = appExt?.defaultConfig?.versionName ?: "1.0.0"
            logger.lifecycle("Encryption [${target.name}]: Using version from app module '${appProject.name}' - versionCode: $versionCodeValue, versionName: $versionNameValue")
        } else {
            // Fallback to defaults
            versionCodeValue = 1
            versionNameValue = "1.0.0"
            logger.warn("Encryption [${target.name}]: No app module found. Using default version - versionCode: $versionCodeValue, versionName: $versionNameValue")
        }

        val resRawDir = if (isKmmModule) {
            // KMM modules: use androidMain/res/raw
            target.layout.projectDirectory.dir("src/androidMain/res/raw")
        } else {
            // Regular Android libraries: use src/main/res/raw
            target.layout.projectDirectory.dir("src/main/res/raw")
        }

        encryptTask.configure {
            versionCode.set(versionCodeValue)
            versionName.set(versionNameValue)
            outputFile.set(resRawDir.file("key.properties"))
        }

        val outputPath = if (isKmmModule) "androidMain/res/raw" else "src/main/res/raw"
        logger.lifecycle("Encryption [${target.name}]: Output to $outputPath (KMM: $isKmmModule)")
    }
}
