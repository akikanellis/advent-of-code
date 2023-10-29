package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class Day25Test {
    @ParameterizedTest
    @MethodSource("snafuNumbers")
    fun `converts from SNAFU to decimal and back`(
        originalSnafuNumber: String,
        expectedDecimalNumber: Long
    ) {
        val decimalNumber = Day25.toDecimal(originalSnafuNumber)
        val snafuNumber = Day25.toSnafu(decimalNumber)

        assertEquals(expectedDecimalNumber, decimalNumber)
        assertEquals(originalSnafuNumber, snafuNumber)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-25-input-example.txt, 2=-1=0",
        "/day-25-input-puzzle.txt,  122-12==0-01=00-0=02"
    )
    fun `calculates fuel needed in SNAFU`(
        inputFile: String,
        expectedFuelNeededInSnafu: String
    ) {
        val input = resourceText(inputFile)

        val fuelNeededInSnafu = Day25.fuelNeededInSnafu(input)

        assertEquals(expectedFuelNeededInSnafu, fuelNeededInSnafu)
    }

    companion object {
        @JvmStatic
        fun snafuNumbers() = listOf(
            Arguments.of("1", 1),
            Arguments.of("2", 2),
            Arguments.of("1=", 3),
            Arguments.of("1-", 4),
            Arguments.of("10", 5),
            Arguments.of("11", 6),
            Arguments.of("12", 7),
            Arguments.of("2=", 8),
            Arguments.of("2-", 9),
            Arguments.of("20", 10),
            Arguments.of("1=0", 15),
            Arguments.of("1-0", 20),
            Arguments.of("1=11-2", 2022),
            Arguments.of("1-0---0", 12345),
            Arguments.of("1121-1110-1=0", 314159265)
        )
    }
}
