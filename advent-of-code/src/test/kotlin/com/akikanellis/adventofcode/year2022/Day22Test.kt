package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day22Test {
    @ParameterizedTest
    @CsvSource(
        "/day-22-input-example.txt, false, 6_032",
        "/day-22-input-puzzle.txt,  false, 164_014",
        "/day-22-input-example.txt, true,  5_031",
        "/day-22-input-puzzle.txt,  true,  47_525"
    )
    fun `calculates the device password`(
        inputFile: String,
        cubeProcessing: Boolean,
        expectedPassword: Int
    ) {
        val input = resourceText(inputFile)

        val password = Day22.password(input, cubeProcessing)

        assertEquals(expectedPassword, password)
    }
}
