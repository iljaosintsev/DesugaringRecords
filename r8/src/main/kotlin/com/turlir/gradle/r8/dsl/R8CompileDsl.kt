package com.turlir.gradle.r8.dsl

import org.gradle.process.JavaForkOptions

interface R8CompileDsl: R8CompileSpecMixin {

    fun javaForkOptions(action: JavaForkOptions.() -> Unit)

    fun backend(action: BackendExtension.() -> Unit)
}