package com.turlir.gradle.r8.dsl

import java.io.File
import kotlin.properties.Delegates

open class BackendExtension {
    var compiler: CompilerVariant by Delegates.notNull()
    var platform: PlatformVariant by Delegates.notNull()

    @Suppress("unused")
    fun compileSdk(version: Int) {
        platform = PlatformVariant.Android(version)
    }

    @Suppress("unused")
    fun buildTools(version: String) {
        compiler = CompilerVariant.BuildTools(version)
    }
}

sealed class CompilerVariant {
    class BuildTools(val version: String): CompilerVariant()
    class Link(val file: File): CompilerVariant()
}

sealed class PlatformVariant {
    class Android(val version: Int): PlatformVariant()
    class Link(val file: File): PlatformVariant()
}