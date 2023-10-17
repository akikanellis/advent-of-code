package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day23Test {
    @ParameterizedTest
    @CsvSource(
        "/day-23-input-example.txt, 110",
        "/day-23-input-puzzle.txt,  3_966"
    )
    fun `calculates number of empty ground tiles`(
        inputFile: String,
        expectedNumberOfEmptyGroundTiles: Int
    ) {
        val input = resourceText(inputFile)

        val numberOfEmptyGroundTiles = Day23.numberOfEmptyGroundTiles(input)

        assertEquals(expectedNumberOfEmptyGroundTiles, numberOfEmptyGroundTiles)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-23-input-example.txt, 20",
        "/day-23-input-puzzle.txt,  933"
    )
    fun `finds round where no Elf moves`(
        inputFile: String,
        expectedRound: Int
    ) {
        val input = resourceText(inputFile)

        val roundWhereNoElfMoves = Day23.roundWhereNoElfMoves(input)

        assertEquals(expectedRound, roundWhereNoElfMoves)
    }
}
