package com.turlir.gradle.r8.mappers

import com.turlir.gradle.r8.dsl.R8CompileSpecMixin
import com.turlir.gradle.r8.internal.BackendPath
import com.turlir.gradle.r8.internal.TaskBundle

internal object TaskStateMapper {

    fun map(mixin: R8CompileSpecMixin, backend: BackendPath?): TaskBundle =
        TaskBundle(
            release = mixin.release.getOrElse(false),
            minApi = mixin.minApi.orNull,
            classFileMode = mixin.classFileMode.getOrElse(false),
            proguardConfig = mixin.proguardConfig.orNull?.asFile,
            target = mixin.target.get().asFile,
            output = mixin.output.orNull?.asFile,
            classpath = mixin.getClasspath().files,
            backend = backend
        )
}