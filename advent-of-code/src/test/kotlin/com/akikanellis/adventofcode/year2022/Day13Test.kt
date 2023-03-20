package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class Day13Test {
    @ParameterizedTest
    @CsvSource(
        "/day-13-input-example.txt, 13",
        "/day-13-input-puzzle.txt,  5_503"
    )
    fun `calculates sum of packet pair indices in right order`(
        inputFile: String,
        expectedSum: Int
    ) {
        val input = resourceText(inputFile)

        val sumOfPacketPairIndicesInRightOrder = Day13.sumOfPacketPairIndicesInRightOrder(input)

        assertEquals(expectedSum, sumOfPacketPairIndicesInRightOrder)
    }

    @ParameterizedTest
    @CsvSource(
        "/day-13-input-example.txt, 140",
        "/day-13-input-puzzle.txt,  20_952"
    )
    fun `calculates distress signal decoder key`(
        inputFile: String,
        expectedDecoderKey: Int
    ) {
        val input = resourceText(inputFile)

        val distressSignalDecoderKey = Day13.distressSignalDecoderKey(input)

        assertEquals(expectedDecoderKey, distressSignalDecoderKey)
    }
}
