package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day07Test {
    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-07-input-example.txt, 95_437",
        "/inputs/year2022/day-07-input-puzzle.txt,  1_770_595",
    )
    fun `calculates sum of all directories with a total size of less than 100_000`(
        inputFile: String,
        expectedSum: Int,
    ) {
        val input = resourceText(inputFile)

        val sumOfAllDirectoriesWithTotalSizeLessThan100000 =
            Day07.sumOfAllDirectoriesWithTotalSizeLessThan100000(input)

        assertEquals(expectedSum, sumOfAllDirectoriesWithTotalSizeLessThan100000)
    }

    @ParameterizedTest
    @CsvSource(
        "/inputs/year2022/day-07-input-example.txt, 24_933_642",
        "/inputs/year2022/day-07-input-puzzle.txt,  2_195_372",
    )
    fun `finds size of directory to delete`(inputFile: String, expectedSize: Int) {
        val input = resourceText(inputFile)

        val sizeOfDirectoryToDelete = Day07.sizeOfDirectoryToDelete(input)

        assertEquals(expectedSize, sizeOfDirectoryToDelete)
    }
}
