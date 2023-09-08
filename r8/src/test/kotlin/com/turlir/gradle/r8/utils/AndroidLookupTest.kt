package com.turlir.gradle.r8.utils

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AndroidLookupTest {

    @JvmField
    @Rule
    val temp = TemporaryFolder()

    private val target by lazy(LazyThreadSafetyMode.NONE) {
        AndroidLookup(temp.root.absolutePath)
    }

    @Test
    fun `Given one build tools When specific version Then find compiler`() {
        val compiler = temp.newFolder("build-tools")
            .resolve("34.0.0")
            .resolve("lib")
            .resolve("d8.jar")
            .apply {
                mkdirs() && createNewFile()
            }
        val jar = target.lookupCompiler("34.0.0")
        assertEquals(compiler, jar)
    }

    @Test
    fun `Given multiple build tools When no specific version Then select recent`() {
        val buildToolsDir = temp.newFolder("build-tools").apply {
            resolve("32.0.0").mkdir()
            resolve("33.0.0").mkdir()
        }
        val compiler = buildToolsDir
            .resolve("34.0.0")
            .resolve("lib")
            .resolve("d8.jar")
            .apply {
                mkdirs() && createNewFile()
            }
        val jar = target.lookupCompiler(null)
        assertEquals(compiler, jar)
    }

    //<editor-fold desc="Negative">

    @Test(expected = Exception::class)
    fun `Given no android home Then throw`() {
        val target = AndroidLookup(null)
        target.lookupCompiler(null)
    }

    @Test(expected = Exception::class)
    fun `Given no build tools dir Then throw`() {
        target.lookupCompiler(null)
    }

    @Test(expected = Exception::class)
    fun `Given empty build tools dir Then throw`() {
        temp.newFolder("build-tools")
        target.lookupCompiler(null)
    }

    @Test(expected = Exception::class)
    fun `Given invalid build tools version Then throw`() {
        temp.newFolder("build-tools")
        target.lookupCompiler("a.b.c")
    }

    @Test(expected = Exception::class)
    fun `Given no specific build tools Then throw`() {
        temp.newFolder("build-tools")
        target.lookupCompiler("34.0.0")
    }

    @Test(expected = Exception::class)
    fun `Given no d8Jar Then throw`() {
        temp.newFolder("build-tools")
            .resolve("34.0.0")
            .mkdirs()
        target.lookupCompiler("34.0.0")
    }

    //</editor-fold>
}