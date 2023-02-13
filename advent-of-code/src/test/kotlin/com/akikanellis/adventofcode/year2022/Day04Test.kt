package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.utils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day04Test {
    @ParameterizedTest
    @CsvSource(
        "/day-04-input-example.txt, 2",
        "/day-04-input-puzzle.txt,  509"
    )
    fun `calculates number of fully overlapping pairs`(
        inputFile: String,
        expectedNumberOfPairs: Int
    ) {
        val input = resourceText(inputFile)

        val numberOfFullyOverlappingPairs = Day04.numberOfFullyOverlappingPairs(input)

        assertEquals(expectedNumberOfPairs, numberOfFullyOverlappingPairs)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-04-input-example.txt, 4",
        "/day-04-input-puzzle.txt,  870"
    )
    fun `calculates number of partially overlapping pairs`(
        inputFile: String,
        expectedNumberOfPairs: Int
    ) {
        val input = resourceText(inputFile)

        val numberOfPartiallyOverlappingPairs = Day04.numberOfPartiallyOverlappingPairs(input)

        assertEquals(expectedNumberOfPairs, numberOfPartiallyOverlappingPairs)
    }
}
