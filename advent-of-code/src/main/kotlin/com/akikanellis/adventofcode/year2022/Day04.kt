package com.akikanellis.adventofcode.year2022

object Day04 {
    fun numberOfFullyOverlappingPairs(input: String) = firstToSecondSectionRange(input)
        .count { (firstSectionRange, secondSectionRange) ->
            firstSectionRange.containsFully(secondSectionRange) ||
                secondSectionRange.containsFully(firstSectionRange)
        }

    fun numberOfPartiallyOverlappingPairs(input: String) = firstToSecondSectionRange(input)
        .count { (firstSectionRange, secondSectionRange) ->
            firstSectionRange.containsPartially(secondSectionRange) ||
                secondSectionRange.containsPartially(firstSectionRange)
        }

    private fun firstToSecondSectionRange(input: String) = input
        .lines()
        .filter { it.isNotBlank() }
        .map { Pair(it.substringBefore(","), it.substringAfter(",")) }
        .map { (firstSectionRange, secondSectionRange) ->
            Pair(
                IntRange(
                    firstSectionRange.substringBefore("-").toInt(),
                    firstSectionRange.substringAfter("-").toInt(),
                ),
                IntRange(
                    secondSectionRange.substringBefore("-").toInt(),
                    secondSectionRange.substringAfter("-").toInt(),
                ),
            )
        }

    private fun IntRange.containsFully(other: IntRange) =
        contains(other.first) && contains(other.last)

    private fun IntRange.containsPartially(other: IntRange) = contains(other.first)
}
