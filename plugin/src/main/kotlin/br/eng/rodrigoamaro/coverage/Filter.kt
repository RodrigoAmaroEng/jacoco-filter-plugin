package br.eng.rodrigoamaro.coverage

interface Filter {
    fun matches(className: String): Boolean
}

class StartingPackageFilter(private val packagePath: String) : Filter {
    override fun matches(className: String): Boolean = className.startsWith(packagePath)
}

class ContainsFilter(private val content: String) : Filter {
    override fun matches(className: String): Boolean = className.contains(content)
}

class ContainsRegexFilter(private val content: Regex) : Filter {
    override fun matches(className: String): Boolean = className.contains(content)
}

open class FilterSetup {
    var type: FilterType = FilterType.CONTAINS
    var content: String = ""
}

enum class FilterType {
    CONTAINS,
    CONTAINS_REGEX,
    STARTS_WITH;

    fun get(setup:FilterSetup) :Filter {
        return when (setup.type) {
            CONTAINS -> ContainsFilter(setup.content)
            CONTAINS_REGEX -> ContainsRegexFilter(Regex(setup.content))
            STARTS_WITH -> StartingPackageFilter(setup.content)
        }
    }
}