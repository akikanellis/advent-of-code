package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day21Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-21-input-example.txt, root, 152",
        "/inputs/year2022/day-21-input-puzzle.txt,  root, 379_578_518_396_784",
        "/inputs/year2022/day-21-input-example.txt, humn, 301",
        "/inputs/year2022/day-21-input-puzzle.txt,  humn, 3_353_687_996_514",
    )
    fun `calculates the number the monkey yells`(
        inputFile: String,
        monkeyName: String,
        expectedNumber: Long,
    ) {
        val input = resourceText(inputFile)

        val monkeyNumber = Day21.monkeyNumber(input = input, monkeyName = monkeyName)

        assertEquals(expectedNumber, monkeyNumber)
    }
}
