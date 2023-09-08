package com.turlir.gradle.r8.utils

import com.turlir.gradle.r8.AbsTest
import com.turlir.gradle.r8.internal.d8
import com.turlir.gradle.r8.internal.CompileMode
import com.turlir.gradle.r8.internal.r8
import com.turlir.gradle.r8.ArtifactHelper.desugaredJar
import com.turlir.gradle.r8.ArtifactHelper.desugaredZip
import com.turlir.gradle.r8.ArtifactHelper.minifiedJar
import com.turlir.gradle.r8.ArtifactHelper.minifiedZip
import com.turlir.gradle.r8.ArtifactHelper.projectJar
import com.turlir.gradle.r8.dsl.R8CompileSpec
import com.turlir.gradle.r8.internal.CompileMixinImpl
import com.turlir.gradle.r8.utils.TaskHelper.setupConvention
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.configurationcache.extensions.serviceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class TaskHelperTest: AbsTest() {

    private val fileCollectionFactory = project.serviceOf<FileCollectionFactory>()
    private val r8CompileSpec = project.objects.newInstance(R8CompileSpec::class.java)

    private val options = CompileMixinImpl(r8CompileSpec, fileCollectionFactory)

    @Before
    fun setup() {
        setupJarTask()
    }

    //<editor-fold desc="setupConvention">

    @Test
    fun `Given jar task When construct D8 class mode The set default value for output`() {
        options.classFileMode.set(true)

        setupConvention(project, options, CompileMode.d8())

        val output = options.output.get()
        assertEquals(desugaredJar, output.asFile.name)
    }

    @Test
    fun `Given jar task When construct D8 dex mode The set default value for output`() {
        options.classFileMode.set(false)

        setupConvention(project, options, CompileMode.d8())

        val output = options.output.get()
        assertEquals(desugaredZip, output.asFile.name)
    }

    @Test
    fun `Given jar task When construct R8 class mode The set default value for output`() {
        options.classFileMode.set(true)

        setupConvention(project, options, CompileMode.r8())

        val output = options.output.get()
        assertEquals(minifiedJar, output.asFile.name)
    }

    @Test
    fun `Given jar task When construct R8 dex mode The set default value for output`() {
        options.classFileMode.set(false)

        setupConvention(project, options, CompileMode.r8())

        val output = options.output.get()
        assertEquals(minifiedZip, output.asFile.name)
    }

    @Test
    fun `Given jar task When construct The set default value for target`() {
        setupConvention(project, options, CompileMode.d8())

        val output = options.target.get()
        assertEquals(projectJar, output.asFile.name)
    }

    //</editor-fold>
}