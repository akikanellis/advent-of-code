package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.utils.Point
import com.akikanellis.adventofcode.year2022.utils.circularListIterator

object Day23 {
    fun numberOfEmptyGroundTiles(input: String): Int {
        var grove = Grove.of(input)
        val directions = Direction.orderedDirections.circularListIterator()

        repeat(10) { grove = grove.moveElves(directions.next()) }

        return grove.emptyGroundTiles()
    }

    fun roundWhereNoElfMoves(input: String): Int {
        var grove = Grove.of(input)
        val directions = Direction.orderedDirections.circularListIterator()
        var round = 0

        do {
            round++
            grove = grove.moveElves(directions.next())
        } while (grove.numberOfElvesThatMoved != 0)

        return round
    }

    private data class Grove(
        val elves: List<Elf>,
        val numberOfElvesThatMoved: Int = 0
    ) {
        private val positionToElf = elves.associateBy { it.position }

        private val minX = elves.minOf { it.x }
        private val maxX = elves.maxOf { it.x }
        private val minY = elves.minOf { it.y }
        private val maxY = elves.maxOf { it.y }

        fun hasElfInAny(positions: List<Point>) = positions.any { it in positionToElf }

        fun emptyGroundTiles(): Int {
            val totalNumberOfTiles = (maxX - minX + 1) * (maxY - minY + 1)
            return totalNumberOfTiles - elves.size
        }

        fun moveElves(direction: Direction): Grove {
            val elvesToProposedPositions = elves
                .map { elf -> Pair(elf, elf.proposedPosition(direction, this)) }

            val numberOfElvesThatMoved = elvesToProposedPositions
                .count { (elf, proposedPosition) -> elf.position != proposedPosition }

            val duplicatePositions = elvesToProposedPositions
                .map { (_, proposedPosition) -> proposedPosition }
                .groupingBy { proposedPosition -> proposedPosition }
                .eachCount()
                .filter { (_, numberOfDuplicates) -> numberOfDuplicates > 1 }
                .keys

            val (movingElves, stationaryElves) = elvesToProposedPositions
                .partition { (_, proposedPosition) -> proposedPosition !in duplicatePositions }

            val movedElves = movingElves.map { (_, proposedPosition) -> Elf(proposedPosition) }
            val stationedElves = stationaryElves.map { (originalElf, _) -> originalElf }

            return Grove(
                elves = movedElves + stationedElves,
                numberOfElvesThatMoved = numberOfElvesThatMoved
            )
        }

        companion object {
            fun of(representation: String) = representation
                .lines()
                .flatMapIndexed { y, row ->
                    row.mapIndexedNotNull { x, tileType ->
                        if (tileType == '#') {
                            Elf(Point(x, y))
                        } else {
                            null
                        }
                    }
                }
                .let { Grove(it) }
        }
    }

    private data class Elf(val position: Point) {
        val x = position.x
        val y = position.y

        fun proposedPosition(startingDirection: Direction, grove: Grove): Point {
            if (!grove.hasElfInAny(Direction.allAdjacentPositions(position))) return position

            val directions = startingDirection.directionsOrder

            for (direction in directions) {
                if (!grove.hasElfInAny(direction.adjacentPositions(position))) {
                    return direction.nextPosition(position)
                }
            }

            return position
        }
    }

    private enum class Direction {
        N {
            override val directionsOrder by lazy { listOf(N, S, W, E) }

            override fun adjacentPositions(position: Point) = listOf(
                position.minusY(),
                position.minusY().plusX(),
                position.minusY().minusX()
            )

            override fun nextPosition(position: Point) = position.minusY()
        },
        S {
            override val directionsOrder by lazy { listOf(S, W, E, N) }

            override fun adjacentPositions(position: Point) = listOf(
                position.plusY(),
                position.plusY().plusX(),
                position.plusY().minusX()
            )

            override fun nextPosition(position: Point) = position.plusY()
        },
        W {
            override val directionsOrder by lazy { listOf(W, E, N, S) }

            override fun adjacentPositions(position: Point) = listOf(
                position.minusX(),
                position.minusY().minusX(),
                position.plusY().minusX()
            )

            override fun nextPosition(position: Point) = position.minusX()
        },
        E {
            override val directionsOrder by lazy { listOf(E, N, S, W) }

            override fun adjacentPositions(position: Point) = listOf(
                position.plusX(),
                position.minusY().plusX(),
                position.plusY().plusX()
            )

            override fun nextPosition(position: Point) = position.plusX()
        };

        abstract val directionsOrder: List<Direction>

        abstract fun adjacentPositions(position: Point): List<Point>
        abstract fun nextPosition(position: Point): Point

        companion object {
            val orderedDirections = N.directionsOrder

            fun allAdjacentPositions(position: Point) = orderedDirections
                .flatMap { it.adjacentPositions(position) }
                .distinct()
        }
    }
}
