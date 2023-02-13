package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.utils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day02Test {
    @ParameterizedTest
    @CsvSource(
        "/day-02-input-example.txt, 15",
        "/day-02-input-puzzle.txt,    11_449"
    )
    fun `finds total score for hands`(
        inputFile: String,
        expectedTotalScore: Int
    ) {
        val input = resourceText(inputFile)

        val totalScoreForHands = Day02.totalScoreForHands(input)

        assertEquals(expectedTotalScore, totalScoreForHands)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-02-input-example.txt, 12",
        "/day-02-input-puzzle.txt,    13_187"
    )
    fun `finds total score for first hand and round result`(
        inputFile: String,
        expectedTotalScore: Int
    ) {
        val input = resourceText(inputFile)

        val totalScoreForFirstHandAndRoundResult = Day02.totalScoreForFirstHandAndRoundResult(input)

        assertEquals(expectedTotalScore, totalScoreForFirstHandAndRoundResult)
    }
}
