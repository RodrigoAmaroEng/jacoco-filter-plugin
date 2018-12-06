package br.eng.rodrigoamaro.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project

class CoveragePlugin : Plugin<Project> {
    companion object {
        const val REFINE_TASK = "refineCoverage"
        const val DELTA_COVERAGE_TASK = "coverageOverBranchDiff"
    }

    override fun apply(project: Project) {
        project.extensions.create("coverage", CoverageExtension::class.java, project)
        project.tasks.create(REFINE_TASK, CoverageRefinerTask::class.java)
        project.tasks.create(DELTA_COVERAGE_TASK, DeltaCoverageTask::class.java) {
            it.mustRunAfter(REFINE_TASK)
        }
    }
}