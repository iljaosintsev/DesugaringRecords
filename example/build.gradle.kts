import com.turlir.gradle.r8.dsl.Mode
import com.turlir.gradle.r8.CompileR8
import com.turlir.gradle.r8.dsl.CompilerVariant
import com.turlir.gradle.r8.dsl.PlatformVariant
import com.turlir.gradle.r8.dsl.R8CompileDsl
import org.gradle.internal.jvm.Jvm

plugins {
    kotlin("jvm")
    `java-library`
    application
}

group = "com.turlir.records"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlin.coreLibrariesVersion}")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    compilerOptions {
        // only for Person data-class, not for User java record
        freeCompilerArgs.set(listOf(
            // "-Xstring-concat=inline" // StringBuilder().append
            // "-Xstring-concat=indy" // StringConcatFactory.makeConcat
            // "-Xstring-concat=indy-with-constants" // StringConcatFactory.makeConcatWithConstants
        ))
    }
}

tasks.withType<Jar>  {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
        attributes["Manifest-version"] = version
    }
}

buildscript {
    dependencies {
        classpath("com.turlir.gradle:r8")
    }
}

val buildToolsVersion = "34.0.0"

val proguardFile: File
    get() = project.file(project.properties["proguard"].toString())

val dexByR8 = tasks.register("dexByR8", CompileR8::class.java, Mode.R8)
dexByR8.configure {
    backend {
        compiler = CompilerVariant.BuildTools(buildToolsVersion)
        platform = PlatformVariant.Android(34)
    }
    // enableClassFileMode() make error,
    // android runtime doesn't know about StringConcatFactory
    enableMinApi(26)
    // emitRecordAnnotations() // required for invoke-custom, save java.lang.Record into bytecode
    classpath(
        configurations.runtimeClasspath.map { it.files }
    )
    proguardConfig.set(proguardFile)
}

val minifyWithR8 = tasks.register("minifyWithR8", CompileR8::class.java, Mode.R8)
minifyWithR8.configure {
    backend {
        compiler = CompilerVariant.BuildTools(buildToolsVersion)
        platform = PlatformVariant.Link(Jvm.current().javaHome) // not android runtime
    }
    enableClassFileMode()
    emitRecordAnnotations() // required for invoke-custom
    classpath(
        configurations.runtimeClasspath.map { it.files }
    )
    proguardConfig.set(proguardFile)
}

tasks.register("runMinified", JavaExec::class) {
    classpath(
        configurations.runtimeClasspath,
        minifyWithR8.map { it.output }
    )
    mainClass.set(application.mainClass)
}.configure {
    group = "application"
}

val desugarMinfied = tasks.register("desugarMinfied", CompileR8::class.java, Mode.D8)
desugarMinfied.configure {
    backend {
        buildTools(buildToolsVersion)
        compileSdk(34)
    }
    compileMode(isClassFile = true, minApi = 26)
    emitRecordAnnotations()
    target.set(
        minifyWithR8.flatMap { it.output } // r8 -> d8
    )
    classpath(
        configurations.runtimeClasspath.map { it.files }
    )
}

fun R8CompileDsl.emitRecordAnnotations() =
    this.javaForkOptions {
        systemProperty("com.android.tools.r8.emitRecordAnnotationsInDex", true)
    }