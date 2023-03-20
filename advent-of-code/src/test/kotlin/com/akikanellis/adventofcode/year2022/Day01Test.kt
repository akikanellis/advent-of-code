package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day01Test {
    @ParameterizedTest
    @CsvSource(
        "/day-01-input-example.txt, 24_000",
        "/day-01-input-puzzle.txt,  70_296"
    )
    fun `finds total calories of top elf`(
        inputFile: String,
        expectedCalories: Int
    ) {
        val input = resourceText(inputFile)

        val totalCaloriesOfTopElf = Day01.totalCaloriesOfTopElf(input)

        assertEquals(expectedCalories, totalCaloriesOfTopElf)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-01-input-example.txt, 45_000",
        "/day-01-input-puzzle.txt,  205_381"
    )
    fun `finds total calories of top three elves`(
        inputFile: String,
        expectedCalories: Int
    ) {
        val input = resourceText(inputFile)

        val totalCaloriesOfTopThreeElves = Day01.totalCaloriesOfTopThreeElves(input)

        assertEquals(expectedCalories, totalCaloriesOfTopThreeElves)
    }
}
