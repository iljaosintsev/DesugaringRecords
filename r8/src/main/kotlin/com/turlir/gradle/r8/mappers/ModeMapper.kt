package com.turlir.gradle.r8.mappers

import com.turlir.gradle.r8.dsl.Mode
import com.turlir.gradle.r8.internal.CompileMode

internal object ModeMapper {

    fun map(mode: Mode): CompileMode {
        return CompileMode(
            prefix = prefix(mode),
            entryPoint = entryPoint(mode),
            isR8 = mode == Mode.R8
        )
    }

    private fun prefix(mode: Mode): String {
        return when (mode) {
            Mode.D8 -> "desugared"
            Mode.R8 -> "minified"
        }
    }

    private fun entryPoint(mode: Mode): String {
        return when (mode) {
            Mode.D8 -> "com.android.tools.r8.D8"
            Mode.R8 -> "com.android.tools.r8.R8"
        }
    }
}