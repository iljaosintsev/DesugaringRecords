package com.turlir.gradle.r8.utils

import com.turlir.gradle.r8.dsl.R8CompileSpec
import com.turlir.gradle.r8.internal.CompileMode
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip
import org.gradle.jvm.tasks.Jar

internal object TaskHelper {

    fun findJarTask(project: Project): Jar? {
        return project.tasks.withType(Jar::class.java).firstOrNull()
    }

    fun setupConvention(project: Project, holder: R8CompileSpec, compileMode: CompileMode) {
        val jarTask = findJarTask(project) ?: return

        val defaultJar = jarTask.archiveFile
        holder.target.convention(defaultJar)

        val defaultOutput = holder.classFileMode.zip(holder.target)
        { isClassFile, archive ->
            val archiveFile = archive.asFile
            val ext = if (isClassFile) Jar.DEFAULT_EXTENSION else Zip.ZIP_EXTENSION
            val baseName = archiveFile.nameWithoutExtension
            val name = "${compileMode.prefix}-$baseName.$ext"
            archiveFile.parentFile.resolve(name)
        }
        holder.output.convention(
            project.layout.file(defaultOutput)
        )
    }
}