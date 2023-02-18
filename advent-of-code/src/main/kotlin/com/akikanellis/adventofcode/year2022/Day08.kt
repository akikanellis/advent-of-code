package com.akikanellis.adventofcode.year2022

object Day08 {
    fun numberOfVisibleTrees(input: String): Int {
        val trees = trees(input)

        return trees(input)
            .flatMap { (rowIndex, row) ->
                row.map { (columnIndex, tree) ->
                    val hiddenFromTheLeft = row.take(columnIndex)
                        .any { (_, otherTree) -> otherTree >= tree }
                    val hiddenFromTheRight = row.drop(columnIndex + 1)
                        .any { (_, otherTree) -> otherTree >= tree }
                    val hiddenFromTheTop = trees.take(rowIndex)
                        .any { (_, otherRow) -> otherRow[columnIndex].value >= tree }
                    val hiddenFromTheBottom = trees.drop(rowIndex + 1)
                        .any { (_, otherRow) -> otherRow[columnIndex].value >= tree }

                    return@map !(
                        hiddenFromTheLeft &&
                            hiddenFromTheRight &&
                            hiddenFromTheTop &&
                            hiddenFromTheBottom
                        )
                }
            }.count { visible -> visible }
    }

    fun highestScenicScore(input: String): Int {
        val trees = trees(input)

        return trees.flatMap { (rowIndex, row) ->
            row.map { (columnIndex, tree) ->
                val scenicScoreFromTheLeft = row.take(columnIndex)
                    .reversed()
                    .takeWhileInclusive { (_, otherTree) -> otherTree < tree }
                    .count()
                val scenicScoreFromTheRight = row.drop(columnIndex + 1)
                    .takeWhileInclusive { (_, otherTree) -> otherTree < tree }
                    .count()
                val scenicScoreFromTheTop = trees.take(rowIndex)
                    .reversed()
                    .takeWhileInclusive { (_, otherRow) -> otherRow[columnIndex].value < tree }
                    .count()
                val scenicScoreFromTheBottom = trees.drop(rowIndex + 1)
                    .takeWhileInclusive { (_, otherRow) -> otherRow[columnIndex].value < tree }
                    .count()

                return@map scenicScoreFromTheLeft *
                    scenicScoreFromTheRight *
                    scenicScoreFromTheTop *
                    scenicScoreFromTheBottom
            }
        }.max()
    }

    private fun trees(input: String) = input
        .lines()
        .filter { it.isNotBlank() }
        .map {
            it
                .map { height -> height.digitToInt() }
                .withIndex()
                .toList()
        }
        .withIndex()
        .toList()

    private fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
        var shouldContinue = true
        return takeWhile {
            val result = shouldContinue
            shouldContinue = predicate(it)
            result
        }
    }
}
