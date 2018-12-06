package br.eng.rodrigoamaro.coverage

data class LineCoverage(
    val lineNumber: Int,
    val coveredInstructions: Int,
    val missedInstructions: Int,
    val totalInstructions: Int = coveredInstructions + missedInstructions
)