package com.akikanellis.adventofcode.year2022

import kotlin.math.pow

object Day25 {
    fun fuelNeededInSnafu(input: String) = input.lines()
        .filterNot { it.isBlank() }
        .sumOf { toDecimal(it) }
        .let { toSnafu(it) }

    fun toDecimal(snafuNumber: String) = snafuNumber
        .toCharArray()
        .reversed()
        .map { snafuDigit ->
            when (snafuDigit) {
                '-' -> -1
                '=' -> -2
                else -> snafuDigit.digitToInt()
            }
        }
        .withIndex()
        .sumOf { (index, digitValue) -> digitValue * 5.0.pow(index) }
        .toLong()

    fun toSnafu(decimalNumber: Long): String {
        var snafuNumber = ""
        var quotient = decimalNumber

        while (quotient != 0L) {
            val convertedDigit = when (val remainder = quotient % 5) {
                3L -> "="
                4L -> "-"
                else -> remainder.toString()
            }
            snafuNumber = convertedDigit + snafuNumber
            quotient = (quotient + 2) / 5
        }

        return snafuNumber
    }
}
