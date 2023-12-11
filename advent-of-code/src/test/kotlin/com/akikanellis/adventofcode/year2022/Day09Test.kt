package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day09Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-09-part-1-input-example.txt, 2,  13",
        "/inputs/year2022/day-09-input-puzzle.txt,         2,  6_044",
        "/inputs/year2022/day-09-part-2-input-example.txt, 10, 36",
        "/inputs/year2022/day-09-input-puzzle.txt,         10, 2_384",
    )
    fun `finds number of positions the tail visited`(
        inputFile: String,
        numberOfKnots: Int,
        expectedNumberOfPositions: Int,
    ) {
        val input = resourceText(inputFile)

        val numberOfPositionsTailVisited = Day09.numberOfPositionsTailVisited(input, numberOfKnots)

        assertEquals(expectedNumberOfPositions, numberOfPositionsTailVisited)
    }
}
