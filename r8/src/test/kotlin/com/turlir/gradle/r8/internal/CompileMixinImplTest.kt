package com.turlir.gradle.r8.internal

import com.turlir.gradle.r8.dsl.R8CompileSpec
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.configurationcache.extensions.serviceOf
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.*
import org.junit.Test

class CompileMixinImplTest {

    private val project = ProjectBuilder.builder()
        .withName("test")
        .build()
        .also {
            it.version = "1.0.0"
        }

    private val options = project.objects.newInstance(R8CompileSpec::class.java)
    private val fileCollectionFactory = project.serviceOf<FileCollectionFactory>()

    private val target = CompileMixinImpl(options, fileCollectionFactory)

    @Test
    fun `When enable class file mode Then update state`() {
        options.apply {
            minApi.set(21)
            classFileMode.set(false)
        }

        target.enableClassFileMode()

        options.apply {
            assertNull(minApi.orNull)
            assertTrue(classFileMode.get())
        }
    }

    @Test
    fun `When enable min api Then update state`() {
        options.apply {
            classFileMode.set(true)
            minApi.set(null as Int?)
        }

        target.enableMinApi(21)

        options.apply {
            assertFalse(classFileMode.get())
            assertEquals(21, minApi.orNull)
        }
    }

    @Test
    fun `When set compile mode Then update state`() {
        options.apply {
            classFileMode.set(false)
            minApi.set(34)
        }

        target.compileMode(isClassFile = true, minApi = 21)

        options.apply {
            assertTrue(classFileMode.get())
            assertEquals(21, minApi.orNull)
        }
    }

    @Test
    fun `Given classpath value When setting classpath Then clean old value`() {
        target.classpath(
            project.files("demo.jar")
        )

        val classpath = project.files("other.jar")
        target.setClasspath(classpath)

        assertEquals(classpath.files, target.getClasspath().files)
    }

    @Test
    fun `When adding classpath item Then hold values`() {
        val classpath = project.files("other.jar")
        target.classpath(classpath)

        assertEquals(classpath.files, target.getClasspath().files)
    }
}