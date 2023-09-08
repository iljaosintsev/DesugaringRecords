package com.turlir.gradle.r8

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

internal object ArtifactHelper {

    const val projectJar = "test-1.0.0.jar"

    const val minifiedJar = "minified-test-1.0.0.jar"
    const val minifiedZip = "minified-test-1.0.0.zip"

    const val desugaredJar = "desugared-test-1.0.0.jar"
    const val desugaredZip = "desugared-test-1.0.0.zip"

    fun setupJarTaskFor(project: Project): Jar =
        project.tasks.create("jar", Jar::class.java).apply {
            destinationDirectory.set(project.buildDir)
            archiveBaseName.set(project.name)
            archiveVersion.set(project.version.toString())
            archiveExtension.set("jar")
        }
}
