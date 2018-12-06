package br.eng.rodrigoamaro.coverage

import org.gradle.internal.impldep.org.junit.runner.RunWith
import org.gradle.internal.impldep.org.junit.runners.JUnit4
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.math.BigDecimal


@RunWith(JUnit4::class)
class ConverterTest {

    private val classPath = File(javaClass.classLoader.getResource("classes").file)
    private val sourcePath = File("../sample/src/main")
    private val destination = File("./out/report")

    @Test
    fun simple() {
        val analyser = CoverageDataAnalyser(FilteredCoverageBuilder(), listOf(classPath))
        val converter = Converter(analyser, sourcePath, destination)
        converter.convert(
            listOf(File(javaClass.classLoader.getResource("test.exec").file))
        )
    }

    @Test
    fun deltaCoverageAllFilesPresent() {
        val coverageData = CoverageData(javaClass.classLoader.getResource("merged.xml").file)
        val diffCoverage = DiffCoverage(
            javaClass.classLoader.getResource("diff-file").file, coverageData,
            sourcePath
        )
        val coverage = diffCoverage.calculate()
        Assert.assertEquals(coverage, BigDecimal(93).setScale(1))
    }

    @Test
    fun deltaCoverageNoFilesPresent() {
        val coverageData = CoverageData(javaClass.classLoader.getResource("merged.xml").file)
        val diffCoverage = DiffCoverage(
            javaClass.classLoader.getResource("diff-file-no-changes").file,
            coverageData, sourcePath
        )
        val coverage = diffCoverage.calculate()
        Assert.assertEquals(coverage, BigDecimal(100).setScale(1))
    }
}