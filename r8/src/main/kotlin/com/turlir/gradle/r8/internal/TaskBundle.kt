package com.turlir.gradle.r8.internal

import java.io.File

internal data class TaskBundle(
    val release: Boolean,
    val minApi: Int?,
    val classFileMode: Boolean,
    val proguardConfig: File?,
    val target: File,
    val output: File?,
    val classpath: Set<File>,
    val backend: BackendPath?,
) {
    internal companion object
}