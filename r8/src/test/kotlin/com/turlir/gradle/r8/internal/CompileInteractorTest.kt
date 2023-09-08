package com.turlir.gradle.r8.internal

import com.turlir.gradle.r8.AbsTest
import com.turlir.gradle.r8.ArtifactHelper.desugaredJar
import com.turlir.gradle.r8.ArtifactHelper.projectJar
import org.gradle.api.tasks.JavaExec
import org.junit.Assert.*
import org.junit.Test
import java.io.File

internal class CompileInteractorTest : AbsTest() {

    private val javaExecTask = project.tasks.create("javaExec", JavaExec::class.java)

    //<editor-fold desc="validate">

    @Test(expected = IllegalArgumentException::class)
    fun `Given no jar task When validate Then throw`() {
        val adapter = CompileInteractor(project, CompileMode.d8())
        val state = TaskBundle.factory()
        adapter.validate(state)
    }

    @Test(expected = IllegalStateException::class)
    fun `Given class file mode and min api When validate R8 Then throw`() {
        setupJarTask()
        val state = TaskBundle.factory().copy(
            classFileMode = true,
            minApi = 16
        )
        val adapter = CompileInteractor(project, CompileMode.r8())
        adapter.validate(state)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Given no backend When validate Then throw`() {
        setupJarTask()
        val state = TaskBundle.factory().copy(
            backend = null
        )
        val adapter = CompileInteractor(project, CompileMode.d8())
        adapter.validate(state)
    }

    @Test
    fun `Given correct state and jar task When validate Then ok`() {
        setupJarTask()
        val state = TaskBundle.factory()
        val adapter = CompileInteractor(project, CompileMode.d8())
        adapter.validate(state)
    }

    //</editor-fold>

    //<editor-fold desc="configure">

    @Test
    fun `Given full state When configure Then should have all args`() {
        setupJarTask()
        val config = File("proguard-rules.pro")
        val dependency = File("dependecy.jar")
        val bundle = TaskBundle.factory(
            target = project.file(projectJar),
            output = project.file(desugaredJar),
            proguardConfig = config,
            classpath = setOf(dependency),
            classFileMode = true,
            minApi = 19,
            isRelease = true
        )

        val adapter = CompileInteractor(project, CompileMode.d8())
        adapter.configure(javaExecTask, bundle)

        val expectedList = listOf<String>(
            "--pg-conf",
            config.toString(),

            "--classpath",
            dependency.toString(),

            "--classfile",

            "--lib",
            "lib.jar",

            "--min-api",
            "19",

            "--release",

            "--output",
            project.rootDir.resolve(desugaredJar).absolutePath,

            project.rootDir.resolve(projectJar).absolutePath
        )
        assertEquals(expectedList, javaExecTask.args)
    }

    @Test
    fun `Given empty state When configure D8 Then set D8 entry point`() {
        setupJarTask()
        val bundle = TaskBundle.factory(
            target = project.file(projectJar),
            output = project.file(desugaredJar),
        )

        val adapter = CompileInteractor(project, CompileMode.d8())
        adapter.configure(javaExecTask, bundle)

        assertEquals("com.android.tools.r8.D8", javaExecTask.mainClass.get())
    }

    @Test
    fun `Given empty state When configure R8 Then set R8 entry point`() {
        setupJarTask()
        val bundle = TaskBundle.factory(
            target = project.file(projectJar),
            output = project.file(desugaredJar),
        )

        val adapter = CompileInteractor(project, CompileMode.r8())
        adapter.configure(javaExecTask, bundle)

        assertEquals("com.android.tools.r8.R8", javaExecTask.mainClass.get())
    }

    @Test
    fun `Given backend When configure Then setup backend compiler as classpath`() {
        setupJarTask()
        val bundle = TaskBundle.factory(
            target = project.file(projectJar),
            output = project.file(desugaredJar),
        )

        val adapter = CompileInteractor(project, CompileMode.d8())
        adapter.configure(javaExecTask, bundle)

        assertEquals(project.files("r8.jar").files, javaExecTask.classpath.files)
    }

    //</editor-fold>
}