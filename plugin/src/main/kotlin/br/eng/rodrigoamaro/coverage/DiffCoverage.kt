package br.eng.rodrigoamaro.coverage

import io.reflectoring.diffparser.api.UnifiedDiffParser
import io.reflectoring.diffparser.api.model.Diff
import io.reflectoring.diffparser.api.model.Range
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.math.RoundingMode

class DiffCoverage(
    diffFile: String,
    private val coverageData: CoverageData,
    source: File
) {

    private val delta: List<Diff>
    private val sourcePath: File

    init {
        val parser = UnifiedDiffParser()
        val stream = FileInputStream(diffFile)
        delta = parser.parse(stream)
        sourcePath = File(source.canonicalPath.substringAfter(File(".").canonicalPath))
    }

    fun calculate(): BigDecimal {
        println("Filtering diff with valid source from '${sourcePath.path}'")
        if (doesNotHaveAtLeastOneFileOnSource())
            return BigDecimal(100).setScale(1)

        val coverage = delta
            .filter { !isTestSourceFolder(it) }
            .flatMap { diff ->
                coverageData.linesForFile(diff.toFileName.substringAfter('/'))
                    .filter { line -> isOneOfChangedLines(diff, line) }
            }.map { Pair(it.coveredInstructions, it.totalInstructions) }
            .reduce { last, actual -> Pair(last.first + actual.first, last.second + actual.second) }

        return BigDecimal(coverage.first * 100 / coverage.second)
            .setScale(1, RoundingMode.HALF_UP)
    }

    private fun doesNotHaveAtLeastOneFileOnSource(): Boolean {
        return delta.none { it.toFileName.substringAfter('/').overlapIn(sourcePath.path) != "" }
    }

    private fun isOneOfChangedLines(diff: Diff, line: LineCoverage) =
        diff.hunks.map { it.toFileRange }.any { it.lineStart >= line.lineNumber && line.lineNumber <= it.lastLine }

    private fun isTestSourceFolder(it: Diff) =
        it.toFileName.contains(Regex("/\\w*[Tt]est/"))

    private val Range.lastLine
        get() = lineStart + lineCount - 1


}