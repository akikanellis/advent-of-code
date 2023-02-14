package com.akikanellis.adventofcode.year2022

object Day06 {
    fun firstPacketMarkerPosition(input: String) = firstMarkerPosition(input, 4)

    fun firstMessageMarkerPosition(input: String) = firstMarkerPosition(input, 14)

    private fun firstMarkerPosition(input: String, distinctCharactersNeeded: Int) = input
        .windowed(distinctCharactersNeeded)
        .withIndex()
        .first { (_, characterSequence) -> characterSequence.allCharactersAreUnique() }
        .let { (index, _) -> index + distinctCharactersNeeded }

    private fun String.allCharactersAreUnique() = toSet().size == length
}
