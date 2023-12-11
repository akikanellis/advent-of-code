package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day15Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-15-input-example.txt, 10,        26",
        "/inputs/year2022/day-15-input-puzzle.txt,  2_000_000, 4_951_427",
    )
    fun `finds number of positions without a beacon in row`(
        inputFile: String,
        targetRow: Int,
        expectedNumberOfPositions: Int,
    ) {
        val input = resourceText(inputFile)

        val numberOfPositionsWithoutBeacon = Day15.numberOfPositionsWithoutBeacon(input, targetRow)

        assertEquals(expectedNumberOfPositions, numberOfPositionsWithoutBeacon)
    }

    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-15-input-example.txt, 20,        56_000_011",
        "/inputs/year2022/day-15-input-puzzle.txt,  4_000_000, 13_029_714_573_243",
    )
    fun `finds tuning frequency of distress beacon`(
        inputFile: String,
        distressBeaconMaxCoordinate: Int,
        expectedTuningFrequency: Long,
    ) {
        val input = resourceText(inputFile)

        val tuningFrequencyOfDistressBeacon =
            Day15.tuningFrequencyOfDistressBeacon(input, distressBeaconMaxCoordinate)

        assertEquals(expectedTuningFrequency, tuningFrequencyOfDistressBeacon)
    }
}
