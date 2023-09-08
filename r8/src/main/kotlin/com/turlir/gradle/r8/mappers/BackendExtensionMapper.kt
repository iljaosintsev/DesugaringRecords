package com.turlir.gradle.r8.mappers

import com.turlir.gradle.r8.dsl.BackendExtension
import com.turlir.gradle.r8.dsl.CompilerVariant
import com.turlir.gradle.r8.dsl.PlatformVariant
import com.turlir.gradle.r8.internal.BackendPath
import com.turlir.gradle.r8.utils.AndroidLookup

internal object BackendExtensionMapper {

    fun map(backend: BackendExtension): BackendPath {
        val helper = AndroidLookup()
        val compilerFile = when (val compiler = backend.compiler) {
            is CompilerVariant.BuildTools -> {
                helper.lookupCompiler(compiler.version)
            }
            is CompilerVariant.Link -> compiler.file
        }
        val platformFile = when (val platform = backend.platform) {
            is PlatformVariant.Android -> {
                helper.lookupPlatform(platform.version)
            }
            is PlatformVariant.Link -> platform.file
        }
        return BackendPath(compilerFile, platformFile)
    }
}