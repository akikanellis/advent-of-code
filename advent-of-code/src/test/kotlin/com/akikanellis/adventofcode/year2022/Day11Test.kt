package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day11Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-11-input-example.txt, 20,     3, 10_605",
        "/inputs/year2022/day-11-input-puzzle.txt,  20,     3, 61_503",
        "/inputs/year2022/day-11-input-example.txt, 10_000, 1, 2_713_310_158",
        "/inputs/year2022/day-11-input-puzzle.txt,  10_000, 1, 14_081_365_540",
    )
    fun `calculates monkey business level`(
        inputFile: String,
        rounds: Int,
        itemDivisor: Int,
        expectedLevel: Long,
    ) {
        val input = resourceText(inputFile)

        val monkeyBusinessLevel = Day11.monkeyBusinessLevel(input, rounds, itemDivisor)

        assertEquals(expectedLevel, monkeyBusinessLevel)
    }
}
