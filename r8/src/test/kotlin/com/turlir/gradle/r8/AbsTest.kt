package com.turlir.gradle.r8

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

internal abstract class AbsTest {

    val project: Project = ProjectBuilder.builder()
        .withName("test")
        .build()
        .also {
            it.version = "1.0.0"
        }

    protected fun setupJarTask() {
        ArtifactHelper.setupJarTaskFor(project)
    }
}