package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.Day17.Direction.LEFT
import com.akikanellis.adventofcode.year2022.Day17.Direction.RIGHT
import com.akikanellis.adventofcode.year2022.utils.CircularListIterator
import com.akikanellis.adventofcode.year2022.utils.circularListIterator

object Day17 {
    fun computeTowerHeight(input: String, amountOfRocksToFall: Long) =
        Cave(amountOfRocksToFall, gasDirectionsCircularListIterator(input))
            .computeTowerHeight()
            .towerHeight

    private fun gasDirectionsCircularListIterator(input: String) = input
        .toList()
        .filter { !it.isWhitespace() }
        .map { Direction.of(it) }
        .circularListIterator()

    private enum class Direction {
        LEFT,
        RIGHT;

        companion object {
            fun of(representation: Char) = when (representation) {
                '<' -> LEFT
                '>' -> RIGHT
                else -> error("Unknown direction '$representation'")
            }
        }
    }

    private class Cave(
        private val amountOfRocksToFall: Long,
        private val gasDirectionsCircularListIterator: CircularListIterator<Direction>
    ) {

        var towerHeight = 0L
        private val rocksToFallCircularListIterator = rocksToFallCircularListIterator()
        private val occupiedFragmentPositions = mutableSetOf<Point>()

        private val precomputedSystemStateToResult = mutableMapOf<SystemState, SystemResult>()

        fun computeTowerHeight(): Cave {
            for (amountOfRocksThatHaveFallen in 0 until amountOfRocksToFall) {
                val rock = moveNextRock()

                towerHeight = maxOf(rock.maxFragmentY + 1, towerHeight)
                occupiedFragmentPositions += rock.fragmentPositions

                val systemState = systemState()

                if (systemState in precomputedSystemStateToResult) {
                    computeTowerHeightForLoop(systemState, amountOfRocksThatHaveFallen)
                    return this
                }

                precomputedSystemStateToResult += systemState to SystemResult(
                    amountOfRocksThatHaveFallen = amountOfRocksThatHaveFallen,
                    towerHeight = towerHeight
                )
            }

            return this
        }

        private fun rocksToFallCircularListIterator() = listOf(
            Rock.horizontalLineRock(),
            Rock.crossRock(),
            Rock.lRock(),
            Rock.verticalLineRock(),
            Rock.squareRock()
        ).circularListIterator()

        private fun moveNextRock(): Rock {
            var rock = rocksToFallCircularListIterator
                .next()
                .startingToFall(towerHeight)

            while (true) {
                val rockMovedByGas = rock.movedByGas(
                    gasDirectionsCircularListIterator.next(),
                    occupiedFragmentPositions
                )
                val rockMovedDown = rockMovedByGas.movedDown(occupiedFragmentPositions)

                rock = rockMovedDown

                if (rockMovedDown == rockMovedByGas) break
            }
            return rock
        }

        private fun systemState() = occupiedFragmentPositions
            .groupBy { fragmentPosition -> fragmentPosition.x }
            .map { (_, fragmentPositionsOfColumn) -> fragmentPositionsOfColumn.maxBy { it.y } }
            .map { fragmentPosition -> fragmentPosition.copy(y = fragmentPosition.y - towerHeight) }
            .toSet()
            .let { topFragmentPositionsPerColumn ->
                SystemState(
                    rockId = rocksToFallCircularListIterator.previousIndex(),
                    gasDirectionId = gasDirectionsCircularListIterator.previousIndex(),
                    topFragmentPositionsPerColumn = topFragmentPositionsPerColumn
                )
            }

        private fun computeTowerHeightForLoop(
            systemState: SystemState,
            amountOfRocksThatHaveFallen: Long
        ) {
            val (initialAmountOfRocksThatHaveFallen, initialTowerHeight) =
                precomputedSystemStateToResult.getValue(systemState)

            val rocksPerLoop = amountOfRocksThatHaveFallen - initialAmountOfRocksThatHaveFallen
            val heightPerLoop = towerHeight - initialTowerHeight

            val remainingAmountOfRocksToFall = amountOfRocksToFall - amountOfRocksThatHaveFallen
            val remainingLoops = remainingAmountOfRocksToFall / rocksPerLoop
            val remainingAmountOfRocksToFallAfterLoops = remainingAmountOfRocksToFall % rocksPerLoop

            val finalAmountOfRocksThatWillFall =
                initialAmountOfRocksThatHaveFallen + remainingAmountOfRocksToFallAfterLoops - 1

            val remainingHeight = precomputedSystemStateToResult.values
                .first { (amountOfRocksThatHaveFallen, _) ->
                    amountOfRocksThatHaveFallen == finalAmountOfRocksThatWillFall
                }.towerHeight - initialTowerHeight

            towerHeight += (remainingLoops * heightPerLoop) + remainingHeight
        }
    }

    private data class Rock(val fragmentPositions: Set<Point>) {
        private val minFragmentX = fragmentPositions.minOf { it.x }
        private val maxFragmentX = fragmentPositions.maxOf { it.x }
        private val minFragmentY = fragmentPositions.minOf { it.y }
        val maxFragmentY = fragmentPositions.maxOf { it.y }

        constructor(fragmentPositions: List<Point>) : this(fragmentPositions.toSet())
        constructor(vararg fragmentPositions: Point) : this(fragmentPositions.toSet())

        fun startingToFall(towerHeight: Long) = Rock(
            fragmentPositions.map { it.copy(x = it.x + 2, y = it.y + towerHeight + 3) }
        )

        fun movedByGas(gasDirection: Direction, occupiedFragmentPositions: Set<Point>): Rock {
            val updatedRock = Rock(
                fragmentPositions.map {
                    it.copy(
                        x = it.x + when (gasDirection) {
                            LEFT -> -1
                            RIGHT -> 1
                        }
                    )
                }
            )

            if (updatedRock.outOfBounds(occupiedFragmentPositions)) return this

            return updatedRock
        }

        fun movedDown(occupiedFragmentPositions: Set<Point>): Rock {
            val updatedRock = Rock(fragmentPositions.map { it.copy(y = it.y - 1) })

            if (updatedRock.outOfBounds(occupiedFragmentPositions)) return this

            return updatedRock
        }

        private fun outOfBounds(occupiedFragmentPositions: Set<Point>) =
            minFragmentX < 0L ||
                maxFragmentX > 6L ||
                minFragmentY < 0L ||
                fragmentPositions.any { it in occupiedFragmentPositions }

        companion object {
            // ####
            fun horizontalLineRock() = Rock(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(3, 0)
            )

            // .#.
            // ###
            // .#.
            fun crossRock() = Rock(
                Point(1, 2),
                Point(0, 1),
                Point(1, 1),
                Point(2, 1),
                Point(1, 0)
            )

            // ..#
            // ..#
            // ###
            fun lRock() = Rock(
                Point(2, 2),
                Point(2, 1),
                Point(0, 0),
                Point(1, 0),
                Point(2, 0)
            )

            // #
            // #
            // #
            // #
            fun verticalLineRock() = Rock(
                Point(0, 3),
                Point(0, 2),
                Point(0, 1),
                Point(0, 0)
            )

            // ##
            // ##
            fun squareRock() = Rock(
                Point(0, 1),
                Point(1, 1),
                Point(0, 0),
                Point(1, 0)
            )
        }
    }

    private data class Point(val x: Long, val y: Long)

    private data class SystemState(
        val rockId: Int,
        val gasDirectionId: Int,
        val topFragmentPositionsPerColumn: Set<Point>
    )

    private data class SystemResult(
        val amountOfRocksThatHaveFallen: Long,
        val towerHeight: Long
    )
}
