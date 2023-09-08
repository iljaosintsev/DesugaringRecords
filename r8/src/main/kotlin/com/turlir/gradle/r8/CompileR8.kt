package com.turlir.gradle.r8

import com.turlir.gradle.r8.dsl.BackendExtension
import com.turlir.gradle.r8.dsl.R8CompileSpecMixin
import com.turlir.gradle.r8.dsl.Mode
import com.turlir.gradle.r8.dsl.R8CompileDsl
import com.turlir.gradle.r8.mappers.BackendExtensionMapper
import com.turlir.gradle.r8.internal.CompileInteractor
import com.turlir.gradle.r8.internal.CompileMixinImpl
import com.turlir.gradle.r8.utils.TaskHelper
import com.turlir.gradle.r8.mappers.ModeMapper
import com.turlir.gradle.r8.internal.TaskBundle
import com.turlir.gradle.r8.mappers.TaskStateMapper
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaForkOptions
import org.gradle.process.ProcessForkOptions
import org.gradle.process.internal.DefaultJavaExecSpec
import javax.inject.Inject

@CacheableTask
abstract class CompileR8 internal constructor(
    private val javaForkOptions: JavaForkOptions,
    private val mixin: R8CompileSpecMixin,
    mode: Mode
) : DefaultTask(), R8CompileDsl, R8CompileSpecMixin by mixin {

    @Inject
    @Suppress("unused")
    constructor(
        objectFactory: ObjectFactory,
        mode: Mode
    ) : this(
        objectFactory.newInstance(DefaultJavaExecSpec::class.java),
        objectFactory.newInstance(CompileMixinImpl::class.java),
        mode
    )

    private val compileMode = ModeMapper.map(mode)
    private val optionsAdapter = CompileInteractor(project, compileMode)

    init {
        group = "build"
        description = "Compile Jar by $mode"
        TaskHelper.setupConvention(project, mixin, compileMode)
    }

    @get:OutputFile
    override val output: RegularFileProperty = mixin.output

    private val bundle: TaskBundle
        get() {
            val backend = extensions.findByType(BackendExtension::class.java)
                ?.let {
                    BackendExtensionMapper.map(it)
                }
            return TaskStateMapper.map(this, backend)
        }

    @TaskAction
    fun exec() {
        optionsAdapter.validate(bundle)
        project.javaexec { spec ->
            (spec as ProcessForkOptions).copyTo(javaForkOptions)
            javaForkOptions.copyTo(spec)
            optionsAdapter.configure(spec, bundle)
        }
    }

    @Suppress("unused")
    override fun javaForkOptions(action: JavaForkOptions.() -> Unit) {
        action(javaForkOptions)
    }

    @Suppress("unused")
    override fun backend(action: BackendExtension.() -> Unit) {
        val backend = extensions.create("backend", BackendExtension::class.java)
        action(backend)
    }
}