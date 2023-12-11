package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day20Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-20-input-example.txt, 3",
        "/inputs/year2022/day-20-input-puzzle.txt,  4_267",
    )
    fun `calculates the sum of grove coordinates with the wrong decryption routine`(
        inputFile: String,
        expectedSum: Long,
    ) {
        val input = resourceText(inputFile)

        val sumOfGroveCoordinates = Day20.sumOfGroveCoordinatesWithWrongDecryptionRoutine(input)

        assertEquals(expectedSum, sumOfGroveCoordinates)
    }

    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-20-input-example.txt, 1_623_178_306",
        "/inputs/year2022/day-20-input-puzzle.txt,  6_871_725_358_451",
    )
    fun `calculates the sum of grove coordinates with the correct decryption routine`(
        inputFile: String,
        expectedSum: Long,
    ) {
        val input = resourceText(inputFile)

        val sumOfGroveCoordinates = Day20.sumOfGroveCoordinatesWithCorrectDecryptionRoutine(input)

        assertEquals(expectedSum, sumOfGroveCoordinates)
    }
}
