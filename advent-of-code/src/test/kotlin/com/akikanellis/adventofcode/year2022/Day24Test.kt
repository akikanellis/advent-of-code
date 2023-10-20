package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day24Test {
    @ParameterizedTest
    @CsvSource(
        "/day-24-input-example.txt, 18",
        "/day-24-input-puzzle.txt,  286"
    )
    fun `calculates fewest number of minutes required to reach the goal`(
        inputFile: String,
        expectedNumberOfMinutes: Int
    ) {
        val input = resourceText(inputFile)

        val numberOfMinutes = Day24.numberOfMinutesToReachGoal(input)

        assertEquals(expectedNumberOfMinutes, numberOfMinutes)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-24-input-example.txt, 54",
        "/day-24-input-puzzle.txt,  820"
    )
    fun `calculates fewest number of minutes required to reach all the goals`(
        inputFile: String,
        expectedNumberOfMinutes: Int
    ) {
        val input = resourceText(inputFile)

        val numberOfMinutes = Day24.numberOfMinutesToReachAllGoals(input)

        assertEquals(expectedNumberOfMinutes, numberOfMinutes)
    }
}
