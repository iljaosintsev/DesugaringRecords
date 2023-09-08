package com.turlir.gradle.r8.dsl

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

interface R8CompileSpec {

    @get:Input
    @get:Optional
    val release: Property<Boolean>

    @get:Input
    @get:Optional
    val minApi: Property<Int>

    @get:Input
    val classFileMode: Property<Boolean>

    @get:InputFile
    @get:PathSensitive(value = PathSensitivity.ABSOLUTE)
    @get:Optional
    val proguardConfig: RegularFileProperty

    @get:InputFile
    @get:PathSensitive(value = PathSensitivity.ABSOLUTE)
    val target: RegularFileProperty

    val output: RegularFileProperty
}