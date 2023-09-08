package com.turlir.gradle.r8.dsl

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFiles

interface R8CompileSpecMixin: R8CompileSpec {

    fun enableClassFileMode()

    fun enableMinApi(value: Int)

    fun compileMode(isClassFile: Boolean, minApi: Int)

    @InputFiles
    @Classpath
    fun getClasspath(): FileCollection

    fun classpath(vararg paths: Any)

    fun setClasspath(classpath: FileCollection)
}