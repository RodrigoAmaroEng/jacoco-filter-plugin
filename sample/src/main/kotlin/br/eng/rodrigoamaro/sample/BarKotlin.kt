package br.eng.rodrigoamaro.sample


class BarKotlin {

    fun calculate(vararg grades: Int): Level {
        val result = grades.toList().average().toBigDecimal()
        return when (result) {
            Double.MIN_VALUE..1.9 -> Level.POOR
            2.0..3.9 -> Level.FAIR
            4.0..5.9 -> Level.AVERAGE
            6.0..7.9 -> Level.GOOD
            else -> Level.EXCELLENT
        }
    }

}