package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day19Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-19-input-example.txt, 33",
        "/inputs/year2022/day-19-input-puzzle.txt,  1_650",
    )
    fun `calculates quality level of blueprints`(inputFile: String, expectedQualityLevels: Int) {
        val input = resourceText(inputFile)

        val qualityLevelsOfBlueprints = Day19.qualityLevelsOfBlueprints(input)

        assertEquals(expectedQualityLevels, qualityLevelsOfBlueprints)
    }

    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-19-input-example.txt, 3_472",
        "/inputs/year2022/day-19-input-puzzle.txt,  5_824",
    )
    fun `calculates max geode that can be opened for top three blueprints`(
        inputFile: String,
        expectedMaxGeode: Int,
    ) {
        val input = resourceText(inputFile)

        val maxGeodeThatCanBeOpenedForTopThreeBlueprints =
            Day19.maxGeodeThatCanBeOpenedForTopThreeBlueprints(input)

        assertEquals(expectedMaxGeode, maxGeodeThatCanBeOpenedForTopThreeBlueprints)
    }
}
