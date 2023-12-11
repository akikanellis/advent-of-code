package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day03Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-03-input-example.txt, 157",
        "/inputs/year2022/day-03-input-puzzle.txt,  7_990",
    )
    fun `calculates sum of priorities for misplaced item types`(
        inputFile: String,
        expectedSumOfPriorities: Int,
    ) {
        val input = resourceText(inputFile)

        val sumOfPrioritiesForMisplacedItemTypes = Day03.sumOfPrioritiesForMisplacedItemTypes(input)

        assertEquals(expectedSumOfPriorities, sumOfPrioritiesForMisplacedItemTypes)
    }

    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-03-input-example.txt, 70",
        "/inputs/year2022/day-03-input-puzzle.txt,  2_602",
    )
    fun `calculates sum of priorities for badges`(inputFile: String, expectedSumOfPriorities: Int) {
        val input = resourceText(inputFile)

        val sumOfPrioritiesForBadges = Day03.sumOfPrioritiesForBadges(input)

        assertEquals(expectedSumOfPriorities, sumOfPrioritiesForBadges)
    }
}
