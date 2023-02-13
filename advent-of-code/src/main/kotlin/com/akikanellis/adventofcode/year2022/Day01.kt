package com.akikanellis.adventofcode.year2022

object Day01 {
    fun totalCaloriesOfTopElf(input: String) = totalCaloriesPerElf(input).max()

    fun totalCaloriesOfTopThreeElves(input: String) = totalCaloriesPerElf(input)
        .sorted()
        .takeLast(3)
        .sum()

    private fun totalCaloriesPerElf(input: String) = input
        .split("\n\n")
        .map { itemisedCaloriesPerElf ->
            itemisedCaloriesPerElf
                .lines()
                .filterNot { it.isBlank() }
                .sumOf { it.toInt() }
        }
}
