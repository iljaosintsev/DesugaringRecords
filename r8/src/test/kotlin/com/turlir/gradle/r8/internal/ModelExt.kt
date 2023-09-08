package com.turlir.gradle.r8.internal

import com.turlir.gradle.r8.ArtifactHelper
import java.io.File

internal fun BackendPath.Companion.factory(
    compiler: File = File("r8.jar"),
    platform: File = File("lib.jar")
) = BackendPath(
    compiler,
    platform
)

internal fun CompileMode.Companion.r8() =
    CompileMode(
        prefix = "minified",
        entryPoint = "com.android.tools.r8.R8",
        isR8 = true
    )

internal fun CompileMode.Companion.d8() =
    CompileMode(
        prefix = "desugared",
        entryPoint = "com.android.tools.r8.D8",
        isR8 = false
    )

internal fun TaskBundle.Companion.factory(
    target: File = File(ArtifactHelper.projectJar),
    output: File = File(ArtifactHelper.desugaredZip),
    proguardConfig: File? = null,
    classpath: Set<File> = emptySet(),
    classFileMode: Boolean = false,
    minApi: Int? = null,
    isRelease: Boolean = false,
    backend: BackendPath = BackendPath.factory()
) = TaskBundle(
    release = isRelease,
    minApi = minApi,
    classFileMode = classFileMode,
    proguardConfig = proguardConfig,
    target = target,
    output = output,
    classpath = classpath,
    backend = backend
)