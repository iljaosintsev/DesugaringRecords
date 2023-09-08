package com.turlir.gradle.r8.internal

import com.turlir.gradle.r8.utils.ArgsBuilder
import org.gradle.api.Project
import org.gradle.process.JavaExecSpec
import com.turlir.gradle.r8.utils.TaskHelper

internal class CompileInteractor(
    private val project: Project,
    private val mode: CompileMode
) {

    fun validate(bundle: TaskBundle) {
        val jarTask = TaskHelper.findJarTask(project)
        requireNotNull(jarTask) {
            "Project should contains `org.gradle.jvm.tasks.Jar` task"
        }
        if (mode.isR8 && bundle.classFileMode && bundle.minApi != null) {
            throw IllegalStateException("R8 doesn't support class file mode and min-api together")
        }
        if (mode.isR8 && bundle.proguardConfig == null) {
            project.logger.warn("Warning: `proguardConfig` not found")
        }
        requireNotNull(bundle.backend) {
            "Define backend { } section"
        }
    }

    fun configure(spec: JavaExecSpec, bundle: TaskBundle): JavaExecSpec {
        val builder = ArgsBuilder()
        val backend = requireNotNull(bundle.backend)

        bundle.proguardConfig?.let {
            builder.pgConf(it.toString())
        }
        bundle.classpath
            .takeIf { it.isNotEmpty() }
            ?.let {
                builder.classpath(it)
            }
        if (bundle.classFileMode) {
            builder.classFile()
        }

        builder.lib(backend.platform)

        bundle.minApi?.let {
            builder.minApi(it.toString())
        }
        if (bundle.release) {
            builder.release()
        }
        bundle.output?.let {
            builder.output(it)
        }

        return spec.apply {
            classpath = project.files(backend.compiler)
            mainClass.set(mode.entryPoint)
            args(builder.build())
            args(bundle.target)
        }
    }
}