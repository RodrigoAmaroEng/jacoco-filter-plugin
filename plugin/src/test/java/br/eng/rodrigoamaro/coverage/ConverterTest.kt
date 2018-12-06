package br.eng.rodrigoamaro.coverage

import org.gradle.internal.impldep.org.junit.runner.RunWith
import org.gradle.internal.impldep.org.junit.runners.JUnit4
import org.junit.Test
import java.io.File
import java.io.FileFilter


@RunWith(JUnit4::class)
class ConverterTest {

    private val classPath = File(javaClass.classLoader.getResource("kotlin-classes/debug").file)
    private val sourcePath = File("../../sample/src/main")
    private val destination = File("./out/report")

    @Test
    fun simple() {
        val analyser = CoverageDataAnalyser(FilteredCoverageBuilder(), listOf(classPath))
        val converter = Converter(analyser, sourcePath, destination)
        converter.convert(
            File(javaClass.classLoader.getResource("").file)
                .listFiles(FileFilter { it.name.endsWith(".ec") }).toList()
        )
    }

    @Test
    fun deltaCoverage() {
        val coverageData = CoverageData(javaClass.classLoader.getResource("merged.xml").file)
        val diffCoverage = DiffCoverage(javaClass.classLoader.getResource("diff-file").file, coverageData)
        val coverage = diffCoverage.calculate()
        println("Coverage: $coverage")
    }

}