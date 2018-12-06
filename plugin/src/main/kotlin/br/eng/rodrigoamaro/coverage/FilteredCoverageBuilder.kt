package br.eng.rodrigoamaro.coverage

import org.jacoco.core.analysis.CoverageBuilder
import org.jacoco.core.analysis.IClassCoverage

class FilteredCoverageBuilder(
    patternsToFilter: Array<Filter> = emptyArray()
) : CoverageBuilder() {
    private val commonFilters = arrayOf<Filter>().plus(patternsToFilter.map { it }.toList())


    override fun visitCoverage(coverage: IClassCoverage?) {
        if (coverage != null && notMatchesFilter(coverage)) {
            super.visitCoverage(coverage)
        }
    }

    private fun notMatchesFilter(coverage: IClassCoverage): Boolean {
        return commonFilters.none { it.matches(coverage.name) }
    }

}