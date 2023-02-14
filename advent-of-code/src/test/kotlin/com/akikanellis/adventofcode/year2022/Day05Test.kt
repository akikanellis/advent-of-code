package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.utils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day05Test {
    @ParameterizedTest
    @CsvSource(
        "/day-05-input-example.txt, CMZ",
        "/day-05-input-puzzle.txt,  ZBDRNPMVH"
    )
    fun `finds crates on the top of each stack for CrateMover 9000`(
        inputFile: String,
        expectedTopCrates: String
    ) {
        val input = resourceText(inputFile)

        val cratesOnTheTopOfEachStack = Day05.cratesOnTheTopOfEachStack(
            input,
            craneMovesMultipleCratesAtOnce = false
        )

        assertEquals(expectedTopCrates, cratesOnTheTopOfEachStack)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-05-input-example.txt, MCD",
        "/day-05-input-puzzle.txt,  WDLPFNNNB"
    )
    fun `finds crates on the top of each stack for CrateMover 9001`(
        inputFile: String,
        expectedTopCrates: String
    ) {
        val input = resourceText(inputFile)

        val cratesOnTheTopOfEachStack = Day05.cratesOnTheTopOfEachStack(
            input,
            craneMovesMultipleCratesAtOnce = true
        )

        assertEquals(expectedTopCrates, cratesOnTheTopOfEachStack)
    }
}
