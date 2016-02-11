package Model

enum class Type {
    EXACT, LOWERBOUND, UPPERBOUND
}

class MinimaxEntry {
    var flag: Type? = null
    var depth: Int? = null
    var value: Int? = null
}