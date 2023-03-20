package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day08Test {
    @ParameterizedTest
    @CsvSource(
        "/day-08-input-example.txt, 21",
        "/day-08-input-puzzle.txt,  1_829"
    )
    fun `finds number of visible trees`(
        inputFile: String,
        expectedNumberOfVisibleTrees: Int
    ) {
        val input = resourceText(inputFile)

        val numberOfVisibleTrees = Day08.numberOfVisibleTrees(input)

        assertEquals(expectedNumberOfVisibleTrees, numberOfVisibleTrees)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-08-input-example.txt, 8",
        "/day-08-input-puzzle.txt,  291_840"
    )
    fun `finds highest scenic score`(
        inputFile: String,
        expectedHighestScenicScore: Int
    ) {
        val input = resourceText(inputFile)

        val highestScenicScore = Day08.highestScenicScore(input)

        assertEquals(expectedHighestScenicScore, highestScenicScore)
    }
}
