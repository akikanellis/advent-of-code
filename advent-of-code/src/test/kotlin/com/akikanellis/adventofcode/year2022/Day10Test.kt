package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    @ParameterizedTest
    @CsvSource(
        "/day-10-input-example.txt, 13_140",
        "/day-10-input-puzzle.txt,  15_220"
    )
    fun `calculates sum of signal strengths`(
        inputFile: String,
        expectedSum: Int
    ) {
        val input = resourceText(inputFile)

        val sumOfSignalStrengths = Day10.sumOfSignalStrengths(input)

        assertEquals(expectedSum, sumOfSignalStrengths)
    }

    @Test
    fun `shows display output`() {
        val input = resourceText("/day-10-input-puzzle.txt")

        val displayOutput = Day10.displayOutput(input)

        assertEquals(
            """
                ###..####.####.####.#..#.###..####..##..
                #..#.#.......#.#....#.#..#..#.#....#..#.
                #..#.###....#..###..##...###..###..#..#.
                ###..#.....#...#....#.#..#..#.#....####.
                #.#..#....#....#....#.#..#..#.#....#..#.
                #..#.#....####.####.#..#.###..#....#..#.
            """.trimIndent(),
            displayOutput
        )
    }
}
