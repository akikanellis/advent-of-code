package com.akikanellis.adventofcode.year2022

object Day13 {
    fun sumOfIndicesInRightOrder(input: String) = input
        .split("\n\n")
        .filter { it.isNotBlank() }
        .map { pair -> pair.split("\n") }
        .map { Pair(codes(it[0]), codes(it[1])) }
        .mapIndexed { index, pair -> Triple(index + 1, pair.first, pair.second) }
        .filter { (_, left, right) -> rightOrder(left, right)!! }
        .sumOf { (index, _, _) -> index }

    private fun rightOrder(left: Any?, right: Any?): Boolean? {
        println("Comparing: left='$left', right='$right'")

        val result = if (left == null && right == null) {
            null
        } else if (left == null) {
            true
        } else if (right == null) {
            false
        } else if (left is Int && right is Int) {
            when {
                left < right -> true
                left > right -> false
                else -> null
            }
        } else if (left is List<*> && right is List<*>) {
            rightOrder(left.firstOrNull(), right.firstOrNull())
                ?: rightOrder(
                    left.drop(1).ifEmpty { null },
                    right.drop(1).ifEmpty { null })
        } else if (left is Int && right is List<*>) {
            rightOrder(listOf(left), right)
        } else if (left is List<*> && right is Int) {
            rightOrder(left, listOf(right))
        } else {
            error("Uh oh")
        }

        return result.also { println("Returning '$it' for comparison left='$left', right='$right''") }
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
            println("nextChar='$nextChar', code='$code'")

            val numberString = (listOf(nextChar) + code.takeWhile { it.isDigit() }).joinToString("")
            
            
            parentList.add(numberString.toInt())

            if (numberString.length > 1) (0..numberString.length - 2).forEach { code.removeAt(it) }

            println("numberString='$numberString', code='$code'")

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

