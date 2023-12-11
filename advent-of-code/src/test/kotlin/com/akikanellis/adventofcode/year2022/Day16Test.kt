package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day16Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-16-input-example.txt, false, 1_651",
        "/inputs/year2022/day-16-input-puzzle.txt,  false, 1_653",
        "/inputs/year2022/day-16-input-example.txt, true,  1_707",
        "/inputs/year2022/day-16-input-puzzle.txt,  true,  2_223",
    )
    fun `finds highest pressure that can be released`(
        inputFile: String,
        elephantWillHelp: Boolean,
        expectedPressure: Int,
    ) {
        val input = resourceText(inputFile)

        val highestPressureThanCanBeReleased =
            Day16.highestPressureThanCanBeReleased(input, elephantWillHelp)

        assertEquals(expectedPressure, highestPressureThanCanBeReleased)
    }
}
