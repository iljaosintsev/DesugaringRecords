package com.turlir.gradle.r8.internal

internal class CompileMode(
    val prefix: String,
    val entryPoint: String,
    val isR8: Boolean,
) {
    internal companion object
}