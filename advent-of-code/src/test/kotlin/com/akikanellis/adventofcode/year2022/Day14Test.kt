package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day14Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-14-input-example.txt, 24",
        "/inputs/year2022/day-14-input-puzzle.txt,  799",
    )
    fun `finds units of sand resting before free fall`(
        inputFile: String,
        expectedUnitsOfSand: Int,
    ) {
        val input = resourceText(inputFile)

        val unitsOfSandRestingUntilFreeFall = Day14.unitsOfSandRestingBeforeFreeFall(input)

        assertEquals(expectedUnitsOfSand, unitsOfSandRestingUntilFreeFall)
    }

    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-14-input-example.txt, 93",
        "/inputs/year2022/day-14-input-puzzle.txt,  29_076",
    )
    fun `finds units of sand resting when full`(inputFile: String, expectedUnitsOfSand: Int) {
        val input = resourceText(inputFile)

        val unitsOfSandRestingWhenFull = Day14.unitsOfSandRestingWhenFull(input)

        assertEquals(expectedUnitsOfSand, unitsOfSandRestingWhenFull)
    }
}
