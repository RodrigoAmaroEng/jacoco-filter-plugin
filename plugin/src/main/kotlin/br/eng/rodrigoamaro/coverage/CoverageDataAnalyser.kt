package br.eng.rodrigoamaro.coverage

import org.jacoco.core.analysis.Analyzer
import org.jacoco.core.analysis.CoverageBuilder
import org.jacoco.core.analysis.IBundleCoverage
import org.jacoco.core.tools.ExecFileLoader
import java.io.File

class CoverageDataAnalyser(
    private val coverageBuilder: CoverageBuilder,
    private val classPath: List<File>
) {
    fun analyse(data: ExecFileLoader): IBundleCoverage {
        val analyzer = Analyzer(data.executionDataStore, coverageBuilder)
        classPath.onEach { analyzer.analyzeAll(it) }
        return coverageBuilder.getBundle("Coverage Report")
    }
}