package br.eng.rodrigoamaro.coverage

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.*


open class DeltaCoverageTask : DefaultTask() {

    @TaskAction
    fun calculate() {
        val configuration = project.coverage() ?: throw IllegalStateException("Coverage section not found")
        val gitDir = File(project.rootProject.projectDir, ".git")
        val git = Git.open(gitDir)
        val lastCommit = git.repository.resolve("HEAD")
        val branch =git.repository.resolve(configuration.baseBranch)

        javaClass.classLoader
            .getResourceAsStream("report.dtd")
            .writeTo(File(configuration.destination, "report.dtd"))

        val oldTreeIterator = git.treeFromObjectId(branch)
        val newTreeIterator = git.treeFromObjectId(lastCommit)
        val outputStream = ByteArrayOutputStream()
        DiffFormatter(outputStream).use { formatter ->
            formatter.setRepository(git.repository)
            formatter.format(oldTreeIterator, newTreeIterator)
        }
        val diffFile = File(configuration.destination, "diff-file")
        with(FileOutputStream(diffFile)) {
            write(outputStream.toString().toByteArray(Charsets.UTF_8))
        }
        val coverage = DiffCoverage(
            diffFile.path, CoverageData("${configuration.destination}/merged.xml"),
            configuration.sourcePath
        )
        println("Coverage on new code: ${coverage.calculate()}")

    }


    private fun Git.treeFromObjectId(objectId: ObjectId):AbstractTreeIterator {
        RevWalk(repository).use { walk ->
            val commit = walk.parseCommit(objectId)
            val treeId = commit.tree.id
            repository.newObjectReader().use { reader -> return CanonicalTreeParser(null, reader, treeId) }
        }
    }

    private fun InputStream.writeTo(file:File) {
        val stream = if (this is BufferedInputStream) this else BufferedInputStream(this)
        with(BufferedOutputStream(FileOutputStream(file))) {
            bufferedWriter(Charsets.UTF_8).write(stream.bufferedReader().readText())
            flush()
        }
    }
}