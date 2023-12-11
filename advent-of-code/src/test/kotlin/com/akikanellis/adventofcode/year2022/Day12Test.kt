package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day12Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-12-input-example.txt, true,  31",
        "/inputs/year2022/day-12-input-puzzle.txt,  true,  440",
        "/inputs/year2022/day-12-input-example.txt, false, 29",
        "/inputs/year2022/day-12-input-puzzle.txt,  false, 439",
    )
    fun `finds shortest path steps`(inputFile: String, ascending: Boolean, expectedSteps: Int) {
        val input = resourceText(inputFile)

        val shortestPathSteps = Day12.shortestPathSteps(input, ascending)

        assertEquals(expectedSteps, shortestPathSteps)
    }
}
