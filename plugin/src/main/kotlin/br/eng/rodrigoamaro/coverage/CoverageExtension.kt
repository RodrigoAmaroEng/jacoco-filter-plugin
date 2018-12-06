package br.eng.rodrigoamaro.coverage

import groovy.lang.Closure
import org.gradle.api.Project
import java.io.File

open class CoverageExtension(private val project: Project) {
    var destination: File = File(project.buildDir, "coverage")
    var classesPath: List<File> = listOf(
        File(project.buildDir, "tmp/kotlin-classes/debug"),
        File(project.buildDir, "intermediates/javac/debug/compileDebugJavaWithJavac/classes")
    )
    var sourcePath: File = File(project.buildDir, "../src/main/java")
    val filters: MutableList<Filter> = mutableListOf()
    var baseBranch: String = "develop"
    var diffCoverageTarget: Int = 0

    fun filter(closure: Closure<FilterSetup>): FilterSetup {
        val filter = FilterSetup()
        project.configure(filter, closure)
        filters.add(filter.type.get(filter))
        return filter
    }

}


fun Project.coverage(): CoverageExtension? {
    return extensions.findByType(CoverageExtension::class.java)
}