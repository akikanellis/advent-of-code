package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day18Test {
    @ParameterizedTest
    @CsvSource(
        "/day-18-input-example.txt, 64",
        "/day-18-input-puzzle.txt,  4_242"
    )
    fun `calculates surface area of scanned lava droplet`(
        inputFile: String,
        expectedSurfaceArea: Int
    ) {
        val input = resourceText(inputFile)

        val surfaceAreaOfScannedLavaDroplet =
            Day18.surfaceAreaOfScannedLavaDroplet(input)

        assertEquals(expectedSurfaceArea, surfaceAreaOfScannedLavaDroplet)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-18-input-example.txt, 58",
        "/day-18-input-puzzle.txt,  2_428"
    )
    fun `calculates surface area of scanned lava droplet without air pockets`(
        inputFile: String,
        expectedSurfaceArea: Int
    ) {
        val input = resourceText(inputFile)

        val surfaceAreaOfScannedLavaDroplet =
            Day18.surfaceAreaOfScannedLavaDropletWithoutAirPockets(input)

        assertEquals(expectedSurfaceArea, surfaceAreaOfScannedLavaDroplet)
    }
}
