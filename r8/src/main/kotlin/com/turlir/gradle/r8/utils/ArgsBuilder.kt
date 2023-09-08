package com.turlir.gradle.r8.utils

import java.io.File

internal class ArgsBuilder {

    private val args = mutableListOf<String>()

    fun pgConf(value: String): ArgsBuilder {
        args.add("--pg-conf")
        args.add(value)
        return this
    }

    fun classpath(files: Set<File>): ArgsBuilder {
        args.add("--classpath")
        args.addAll(
            files.map { it.toString() }
        )
        return this
    }

    fun classFile(): ArgsBuilder {
        args.add("--classfile")
        return this
    }

    fun lib(platform: File): ArgsBuilder {
        args.add("--lib")
        args.add(platform.toString())
        return this
    }

    fun minApi(minApi: String): ArgsBuilder {
        args.add("--min-api")
        args.add(minApi)
        return this
    }

    fun release(): ArgsBuilder {
        args.add("--release")
        return this
    }

    fun output(file: File): ArgsBuilder {
        args.add("--output")
        args.add(file.toString())
        return this
    }

    fun build(): List<String> {
        return args.toList()
    }
}