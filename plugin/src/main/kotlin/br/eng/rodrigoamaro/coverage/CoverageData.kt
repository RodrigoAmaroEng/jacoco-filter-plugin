package br.eng.rodrigoamaro.coverage

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class CoverageData(filePath: String) {
    private val document: Document

    companion object {
        const val KOTLIN_CODE = "/src/main/kotlin/"
        const val JAVA_CODE = "/src/main/java/"
    }

    init {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        document = builder.parse(File(filePath))
    }

    fun linesForFile(file: String): List<LineCoverage> {
        val packageName = file.filePackage()
        if (packageName.isBlank())
            return listOf()
        val fileName = file.substringAfterLast('/')
        return document.documentElement.childrenByTag("package")
            .filter { it.item("name") == packageName }
            .flatMap { it.childrenByTag("sourcefile") }
            .filter { it.item("name") == fileName }
            .flatMap { it.childrenByTag("line") }
            .map { LineCoverage(it.item("nr").toInt(), it.item("ci").toInt(), it.item("mi").toInt()) }
    }

    private fun String.filePackage(): String {
        return when {
            this.contains(KOTLIN_CODE) -> this.substringAfter(KOTLIN_CODE)
            this.contains(JAVA_CODE) -> this.substringAfter(JAVA_CODE)
            else -> ""
        }.substringBeforeLast('/')
    }

    private fun Node.item(name: String) = this.attributes.getNamedItem(name).nodeValue

    private fun Node.childrenByTag(name: String) =
        IntRange(0, this.childNodes.length - 1)
            .map { this.childNodes.item(it) }
            .filter { it.nodeName == name }
}