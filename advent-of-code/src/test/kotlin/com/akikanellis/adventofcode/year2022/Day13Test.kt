package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.utils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day13Test {
    // @ParameterizedTest
    // @CsvSource(
    //     "/day-13-input-example.txt, 13",
    //     "/day-13-input-puzzle.txt,  5_503"
    // )
    // fun `calculates sum of indices in right order`(
    //     inputFile: String,
    //     expectedSum: Int
    // ) {
    //     val input = resourceText(inputFile)

    //     val sumOfIndicesInRightOrder = Day13.sumOfIndicesInRightOrder(input)

    //     assertEquals(expectedSum, sumOfIndicesInRightOrder)
    // }

    @ParameterizedTest
    @CsvSource(
        "/day-13-input-example.txt, 140",
        "/day-13-input-puzzle.txt,  20_952"
    )
    fun `calculates decoder key`(
        inputFile: String,
        expectedDecoderKey: Int
    ) {
        val input = resourceText(inputFile)

        val decoderKey = Day13.decoderKey(input)

        assertEquals(expectedDecoderKey, decoderKey)
    }
}
