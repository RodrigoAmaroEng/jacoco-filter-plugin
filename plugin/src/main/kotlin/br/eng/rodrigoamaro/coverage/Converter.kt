package br.eng.rodrigoamaro.coverage

import org.jacoco.core.analysis.IBundleCoverage
import org.jacoco.core.tools.ExecFileLoader
import org.jacoco.report.DirectorySourceFileLocator
import org.jacoco.report.FileMultiReportOutput
import org.jacoco.report.IReportVisitor
import org.jacoco.report.html.HTMLFormatter
import org.jacoco.report.xml.XMLFormatter
import java.io.File
import java.io.FileOutputStream


class Converter(
    private val analyser: CoverageDataAnalyser,
    private val sourcePath: File,
    private val destination: File
) {
    fun convert(files: List<File>) {
        val loader = ExecFileLoader()
        files.onEach { loader.load(it) }

        val coverage = analyser.analyse(loader)

        if (!destination.exists()) destination.mkdirs()

        val xmlVisitor = XMLFormatter().createVisitor(FileOutputStream(File(destination, "merged.xml")))
        generateReport(loader, coverage, xmlVisitor)

        val htmlVisitor = HTMLFormatter().createVisitor(FileMultiReportOutput(destination))
        generateReport(loader, coverage, htmlVisitor)
    }


    private fun generateReport(
        loader: ExecFileLoader, coverage: IBundleCoverage, visitor: IReportVisitor
    ) {
        visitor.visitInfo(loader.sessionInfoStore.infos, loader.executionDataStore.contents)
        visitor.visitBundle(coverage, DirectorySourceFileLocator(sourcePath, "utf-8", 4))
        visitor.visitEnd()
    }


}