package com.turlir.gradle.r8.utils

import org.gradle.util.GradleVersion
import java.io.File

internal class AndroidLookup(
    private val androidHome: String?
) {

    constructor() : this(
        System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
    )

    fun lookupCompiler(buildToolsVersion: String?): File {
        return lookupCompiler(
            lookupBuildTools(
                lookupAndroidHome(),
                buildToolsVersion
            )
        )
    }

    fun lookupPlatform(version: Int): File {
        val platform = lookupAndroidHome()
            .resolve("platforms")
            .resolve("android-$version")
            .resolve("android.jar")
        require(platform.exists()) {
            "Cannot find platform v$version"
        }
        return platform
    }

    private fun lookupAndroidHome(): File {
        requireNotNull(androidHome) {
            "Setup \$ANDROID_HOME or \$ANDROID_SDK_ROOT env variable"
        }
        val home = File(androidHome)
        require(home.exists()) {
            "Cannot find $androidHome dir"
        }
        return home
    }

    private fun lookupBuildTools(androidHome: File, version: String?): File {
        val buildToolsDir = File(androidHome, "build-tools")
        require(buildToolsDir.exists()) {
            "Cannot find `build-tools` dir into $androidHome"
        }
        val toolDir = if (version == null) {
            val buildToolsVersions = buildToolsDir.listFiles { dir, name ->
                File(dir, name).isDirectory
            }
            buildToolsVersions?.sortBy {
                GradleVersion.version(it.name)
            }
            buildToolsVersions?.lastOrNull()
        } else {
            val gv = GradleVersion.version(version)
            File(buildToolsDir, gv.version)
        }
        require(toolDir != null && toolDir.exists() && toolDir.isDirectory) {
            "Cannot find build-tools"
        }
        return toolDir
    }

    private fun lookupCompiler(toolDir: File): File {
        val jar = File(toolDir, "lib/d8.jar")
        require(jar.exists()) {
            "Cannot find `lib/d8.jar` into $toolDir"
        }
        return jar
    }
}