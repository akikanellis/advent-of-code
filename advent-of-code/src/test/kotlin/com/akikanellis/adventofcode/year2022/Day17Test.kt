package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day17Test {
    @ParameterizedTest
    @CsvSource(
        "/day-17-input-example.txt, 2_022,             3_068",
        "/day-17-input-puzzle.txt,  2_022,             3_081",
        "/day-17-input-example.txt, 1_000_000_000_000, 1_514_285_714_288",
        "/day-17-input-puzzle.txt,  1_000_000_000_000, 1_524_637_681_145"
    )
    fun `finds tower height`(
        inputFile: String,
        amountOfRocksToFall: Long,
        expectedTowerHeight: Long
    ) {
        val input = resourceText(inputFile)

        val towerHeight = Day17.computeTowerHeight(input, amountOfRocksToFall)

        assertEquals(expectedTowerHeight, towerHeight)
    }
}
