package com.akikanellis.adventofcode.year2022

object Day13 {
    fun sumOfIndicesInRightOrder(input: String): Int {
        val pairs = input
            .split("\n\n")
            .filter { it.isNotBlank() }
            .map { pair -> pair.split("\n") }
            .map { Pair(codes(it[0]), codes(it[1])) }

        return 0
    }

    private fun codes(code: String): List<Any> {
        return codeRec(code.toMutableList().also { it.removeAt(0) }, mutableListOf())
    }

    private fun codeRec(code: MutableList<Char>, parentList: MutableList<Any>): List<Any> {
        if (code.isEmpty()) return parentList

        val nextChar = code.removeAt(0)
        val result = if (nextChar == '[') {
            val newList = mutableListOf<Any>()
            parentList.add(newList)

            codeRec(code, newList)
            codeRec(code, parentList)
        } else if (nextChar == ',') {
            codeRec(code, parentList)
        } else if (nextChar.isDigit()) {
            parentList.add(nextChar.digitToInt())
            codeRec(code, parentList)
        } else if (nextChar == ']') {
            parentList.toList()
        } else {
            error("")
        }

        println("Result for '$nextChar' is '$result'")
        return result
    }
}
