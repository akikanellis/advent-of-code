package com.akikanellis.adventofcode.year2022

object Day05 {
    fun cratesOnTheTopOfEachStack(input: String, craneMovesMultipleCratesAtOnce: Boolean): String {
        val stacks = stacks(input)
        val moves = moves(input)

        val stacksAfterMoves = stacksAfterMoves(
            stacks,
            moves,
            craneMovesMultipleCratesAtOnce,
        )

        return stacksAfterMoves
            .toSortedMap()
            .map { (_, crates) -> crates.last() }
            .joinToString("")
    }

    private fun stacks(input: String): Stacks = input
        .substringBefore("\n\n")
        .lines()
        .reversed()
        .drop(1)
        .flatMap {
            it.chunked(4)
                .map { crate -> crate.trim() }
                .withIndex()
                .filter { (_, crate) -> crate.isNotBlank() }
                .map { (index, crate) -> IndexedValue(index + 1, crate[1]) }
        }.groupBy(
            { (index, _) -> index },
            { (_, crates) -> crates },
        )

    private fun moves(input: String) = input
        .substringAfter("\n\n")
        .lines()
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .map { Move(it[1].toInt(), it[3].toInt(), it[5].toInt()) }

    private fun stacksAfterMoves(
        stacks: Stacks,
        moves: List<Move>,
        craneMovesMultipleCratesAtOnce: Boolean,
    ) = moves
        .fold(stacks) { latestStacks, (numberOfCrates, sourceStackId, targetStackId) ->
            val sourceStackCrates = latestStacks[sourceStackId]!!
            val targetStackCrates = latestStacks[targetStackId]!!

            val movedCrates = sourceStackCrates
                .takeLast(numberOfCrates)
                .let { if (craneMovesMultipleCratesAtOnce) it else it.reversed() }
            val updatedSourceStackCrates = sourceStackCrates.dropLast(numberOfCrates)
            val updatedTargetStackCrates = targetStackCrates + movedCrates

            return@fold latestStacks +
                Pair(sourceStackId, updatedSourceStackCrates) +
                Pair(targetStackId, updatedTargetStackCrates)
        }
}

private typealias Move = Triple<Int, Int, Int>
private typealias Stacks = Map<Int, List<Char>>
