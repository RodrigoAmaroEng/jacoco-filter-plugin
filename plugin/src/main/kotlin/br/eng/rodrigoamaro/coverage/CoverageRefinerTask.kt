package br.eng.rodrigoamaro.coverage

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class CoverageRefinerTask : DefaultTask() {

    @TaskAction
    fun refine() {
        val configuration = project.coverage() ?: throw IllegalStateException("Coverage section not found")
        val coverageBuilder = FilteredCoverageBuilder(configuration.filters.toTypedArray())

        println("${configuration.filters.size} filter(s) detected")
        println("Filtering classes...")
        val classes = configuration.classesPath.flatMap { detectValidClassPaths(it, configuration.sourcePath) }

        val analyser = CoverageDataAnalyser(coverageBuilder, classes)
        val converter = Converter(analyser, configuration.sourcePath, configuration.destination)

        val files = gatherCoverageDataFilesFromPath()

        converter.convert(files)
    }

    private fun gatherCoverageDataFilesFromPath() = list(project.buildDir)
        .filter { it.name.endsWith(".ec") || it.name.endsWith(".exec") }

    private fun detectValidClassPaths(classPath: File, sourcePath: File): List<File> {
        val sourceDirs = extractClassPathFoldersPresentOnSourceCode(classPath, sourcePath)
        return sourceDirs.filter { dir -> sourceDirs.none { it.path == dir.parent } }
    }

    private fun extractClassPathFoldersPresentOnSourceCode(
        classPath: File,
        sourcePath: File
    ): List<File> {
        val sourceDirs = listAllFoldersInside(classPath)
            .filter { File(sourcePath, it.path.substringAfter(classPath.path)).exists() }
        return sourceDirs.distinct()
            .filter { it.folders().none { dir -> !sourceDirs.contains(dir) } }
    }


}