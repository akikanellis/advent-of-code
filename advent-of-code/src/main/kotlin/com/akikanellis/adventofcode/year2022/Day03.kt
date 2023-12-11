package com.akikanellis.adventofcode.year2022

object Day03 {
    fun sumOfPrioritiesForMisplacedItemTypes(input: String) = lines(input)
        .map { Pair(it.take(it.length / 2).toSet(), it.takeLast(it.length / 2).toSet()) }
        .map { it.first.intersect(it.second).single() }
        .sumOf { priority(it) }

    fun sumOfPrioritiesForBadges(input: String) = lines(input)
        .chunked(3)
        .map { Triple(it[0].toSet(), it[1].toSet(), it[2].toSet()) }
        .map { it.first.intersect(it.second).intersect(it.third).single() }
        .sumOf { priority(it) }

    private fun lines(input: String) = input
        .lines()
        .filter { it.isNotBlank() }

    private fun priority(itemType: Char) = when {
        itemType.isLowerCase() -> itemType - 'a' + 1
        else -> itemType - 'A' + 27
    }
}
