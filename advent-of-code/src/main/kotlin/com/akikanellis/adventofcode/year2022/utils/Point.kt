package com.akikanellis.adventofcode.year2022.utils

data class Point(val x: Int, val y: Int) {
    fun zeroX() = copy(x = 0)
    fun minusX() = copy(x = x - 1)
    fun plusX() = copy(x = x + 1)

    fun zeroY() = copy(y = 0)
    fun minusY() = copy(y = y - 1)
    fun plusY() = copy(y = y + 1)

    companion object {
        val ZERO = Point(0, 0)
    }
}
