package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.testutils.resourceText
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {
    @ParameterizedTest
    @CsvSource(
        "mjqjpqmgbljsphdztnvjfqwrcgsmlb,    7",
        "bvwbjplbgvbhsrlpgdmjqwftvncz,      5",
        "nppdvjthqldpwncqszvftbrmjlhg,      6",
        "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg, 10",
        "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw,  11",
    )
    fun `finds the position of the first packet marker for examples`(
        input: String,
        expectedPosition: Int,
    ) {
        val firstPacketMarkerPosition = Day06.firstPacketMarkerPosition(input)

        assertEquals(expectedPosition, firstPacketMarkerPosition)
    }

    @Test
    fun `finds the position of the first packet marker for puzzle input`() {
        val input = resourceText("/inputs/year2022/day-06-input-puzzle.txt")

        val firstPacketMarkerPosition = Day06.firstPacketMarkerPosition(input)

        assertEquals(1_702, firstPacketMarkerPosition)
    }

    @ParameterizedTest
    @CsvSource(
        "mjqjpqmgbljsphdztnvjfqwrcgsmlb,    19",
        "bvwbjplbgvbhsrlpgdmjqwftvncz,      23",
        "nppdvjthqldpwncqszvftbrmjlhg,      23",
        "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg, 29",
        "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw,  26",
    )
    fun `finds the position of the first message marker for examples`(
        input: String,
        expectedPosition: Int,
    ) {
        val firstMessageMarkerPosition = Day06.firstMessageMarkerPosition(input)

        assertEquals(expectedPosition, firstMessageMarkerPosition)
    }

    @Test
    fun `finds the position of the first message marker for puzzle`() {
        val input = resourceText("/inputs/year2022/day-06-input-puzzle.txt")

        val firstMessageMarkerPosition = Day06.firstMessageMarkerPosition(input)

        assertEquals(3_559, firstMessageMarkerPosition)
    }
}
