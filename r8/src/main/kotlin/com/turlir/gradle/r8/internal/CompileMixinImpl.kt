package com.turlir.gradle.r8.internal

import com.turlir.gradle.r8.dsl.R8CompileSpec
import com.turlir.gradle.r8.dsl.R8CompileSpecMixin
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

internal open class CompileMixinImpl internal constructor(
    private val holder: R8CompileSpec,
    private val fileCollectionFactory: FileCollectionFactory
): R8CompileSpecMixin, R8CompileSpec by holder {

    @Inject
    @Suppress("unused")
    constructor(
        objectFactory: ObjectFactory,
        fileCollectionFactory: FileCollectionFactory
    ): this(
        objectFactory.newInstance(R8CompileSpec::class.java),
        fileCollectionFactory
    )

    private var classpath = fileCollectionFactory.configurableFiles("classpath")

    override fun enableClassFileMode() {
        minApi.set(null as Int?)
        classFileMode.set(true)
    }

    override fun enableMinApi(value: Int) {
        classFileMode.set(false)
        minApi.set(value)
    }

    override fun compileMode(isClassFile: Boolean, minApi: Int) {
        classFileMode.set(isClassFile)
        this.minApi.set(minApi)
    }

    override fun classpath(vararg paths: Any) {
        classpath.from(paths)
    }

    override fun setClasspath(classpath: FileCollection) {
        this.classpath = fileCollectionFactory.configurableFiles("classpath")
        this.classpath.setFrom(classpath)
    }

    override fun getClasspath(): FileCollection {
        return classpath
    }
}