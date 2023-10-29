package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.utils.Point
import java.util.*
import kotlin.math.min

object Day24 {
    fun numberOfMinutesToReachGoal(input: String): Int {
        val valleyIdsToValleys = valleyIdsToValleys(input)
        val initialValley = valleyIdsToValleys.getValue(0)
        val entrance = initialValley.entrance
        val exit = initialValley.exit

        return numberOfMinutesToReachAllGoals(
            valleyIdsToValleys,
            listOf(
                Pair(entrance, exit)
            )
        )
    }

    fun numberOfMinutesToReachAllGoals(input: String): Int {
        val valleyIdsToValleys = valleyIdsToValleys(input)
        val initialValley = valleyIdsToValleys.getValue(0)
        val entrance = initialValley.entrance
        val exit = initialValley.exit

        return numberOfMinutesToReachAllGoals(
            valleyIdsToValleys,
            listOf(
                Pair(entrance, exit),
                Pair(exit, entrance),
                Pair(entrance, exit)
            )
        )
    }

    private fun valleyIdsToValleys(input: String): Map<Int, Valley> {
        val initialValley = Valley.of(input)

        return generateSequence(initialValley.moveBlizzards()) { it.moveBlizzards() }
            .takeWhile { nextValley -> nextValley.blizzards != initialValley.blizzards }
            .plusElement(initialValley)
            .associateBy { it.id }
    }

    private fun numberOfMinutesToReachAllGoals(
        valleyIdsToValleys: Map<Int, Valley>,
        startsToGoals: List<Pair<Point, Point>>
    ) = startsToGoals
        .fold(0) { minutesPassed, startToGoal ->
            minutesPassed + numberOfMinutesToReachGoal(
                initialValley = valleyIdsToValleys
                    .getValue(minutesPassed % valleyIdsToValleys.size),
                valleyIdsToValleys = valleyIdsToValleys,
                start = startToGoal.first,
                goal = startToGoal.second
            )
        }

    private fun numberOfMinutesToReachGoal(
        initialValley: Valley,
        valleyIdsToValleys: Map<Int, Valley>,
        start: Point,
        goal: Point
    ): Int {
        val seenStates = mutableSetOf<SystemState>()
        val statesQueue = PriorityQueue(
            listOf(
                SystemState(
                    minutes = 0,
                    currentPosition = start,
                    valley = initialValley,
                    goal = goal
                )
            )
        )
        var fewestMinutesDiscovered = Int.MAX_VALUE

        while (statesQueue.isNotEmpty()) {
            val currentState = statesQueue.poll()

            if (
                currentState in seenStates ||
                currentState.canNotOutperform(fewestMinutesDiscovered)
            ) {
                continue
            }

            if (currentState.goalReached()) {
                fewestMinutesDiscovered = min(
                    fewestMinutesDiscovered,
                    currentState.minutes
                )
                continue
            }

            statesQueue += currentState.nextStates(valleyIdsToValleys)
            seenStates += currentState
        }

        return fewestMinutesDiscovered
    }

    private data class Valley(val id: Int, val tiles: List<Tile>) {
        private val positionToTiles = tiles.associateBy { it.position }

        private val walls = tiles.filterIsInstance<Wall>()
        private val emptyGrounds = tiles.filterIsInstance<Empty>()
        val blizzards = tiles.filterIsInstance<Blizzard>()

        val minX = tiles.minOf { it.position.x }
        val maxX = tiles.maxOf { it.position.x }
        val minY = tiles.minOf { it.position.y }
        val maxY = tiles.maxOf { it.position.y }

        val entrance = emptyGrounds.first().position
        val exit = emptyGrounds.last().position

        fun moveBlizzards(): Valley {
            val newBlizzards = blizzards.map { blizzard -> blizzard.move(this) }
            val wallsAndNewBlizzards = (walls + newBlizzards)
            val positionToWallsAndNewBlizzards = wallsAndNewBlizzards.associateBy { it.position }

            val newEmptyGrounds = (minY..maxY).flatMap { y ->
                (minX..maxX).mapNotNull { x ->
                    val possibleEmptyGroundPosition = Point(x, y)

                    if (possibleEmptyGroundPosition !in positionToWallsAndNewBlizzards) {
                        Empty(possibleEmptyGroundPosition)
                    } else {
                        null
                    }
                }
            }

            return Valley(id = id + 1, tiles = wallsAndNewBlizzards + newEmptyGrounds)
        }

        fun isWall(position: Point) = tile(position) is Wall
        fun isEmptyGround(position: Point) = tile(position) is Empty

        private fun tile(position: Point) = positionToTiles[position]

        companion object {
            fun of(representation: String) = representation
                .lines()
                .filterNot { it.isBlank() }
                .flatMapIndexed { y: Int, row: String ->
                    row.mapIndexed { x, tileType -> Tile.of(Point(x, y), tileType) }
                }
                .let { Valley(0, it) }
        }
    }

    private interface Tile {
        val position: Point

        companion object {
            fun of(position: Point, representation: Char) = when (representation) {
                '.' -> Empty(position)
                '#' -> Wall(position)
                '^', 'v', '>', '<' -> Blizzard(
                    position = position,
                    direction = Direction.of(representation)
                )

                else -> error("Unknown representation '$representation'")
            }
        }
    }

    private data class Empty(override val position: Point) : Tile
    private data class Wall(override val position: Point) : Tile

    private data class Blizzard(
        override val position: Point,
        private val direction: Direction
    ) : Tile {
        fun move(valley: Valley): Blizzard {
            val newPosition = when (direction) {
                Direction.N -> position.minusY()
                Direction.S -> position.plusY()
                Direction.W -> position.minusX()
                Direction.E -> position.plusX()
            }.takeIf { !valley.isWall(it) } ?: when (direction) {
                Direction.N -> position.copy(y = valley.maxY - 1)
                Direction.S -> position.copy(y = valley.minY + 1)
                Direction.W -> position.copy(x = valley.maxX - 1)
                Direction.E -> position.copy(x = valley.minX + 1)
            }

            return copy(position = newPosition)
        }
    }

    private enum class Direction {
        N, S, W, E;

        companion object {
            fun of(representation: Char) = when (representation) {
                '^' -> N
                'v' -> S
                '<' -> W
                '>' -> E
                else -> error("Unknown representation '$representation'")
            }
        }
    }

    private data class SystemState(
        val minutes: Int,
        private val currentPosition: Point
    ) : Comparable<SystemState> {
        private lateinit var valley: Valley
        private lateinit var goal: Point

        constructor(
            minutes: Int,
            currentPosition: Point,
            valley: Valley,
            goal: Point
        ) : this(
            minutes = minutes,
            currentPosition = currentPosition
        ) {
            this.valley = valley
            this.goal = goal
        }

        override fun compareTo(other: SystemState) =
            distanceToGoal().compareTo(other.distanceToGoal())

        private fun distanceToGoal() = currentPosition.manhattanDistance(goal)

        fun canNotOutperform(minutes: Int) = this.minutes >= minutes

        fun nextStates(valleyIdsToValleys: Map<Int, Valley>): List<SystemState> {
            val nextValley = valleyIdsToValleys.getValue((valley.id + 1) % valleyIdsToValleys.size)
            val nextMinutes = minutes + 1

            return currentPositionNeighbours()
                .filter { nextPosition -> nextValley.isEmptyGround(nextPosition) }
                .map { nextPosition ->
                    SystemState(
                        minutes = nextMinutes,
                        currentPosition = nextPosition,
                        valley = nextValley,
                        goal = goal
                    )
                }
        }

        private fun currentPositionNeighbours() = listOf(
            currentPosition.minusY(),
            currentPosition.plusY(),
            currentPosition.minusX(),
            currentPosition.plusX(),
            currentPosition
        )

        fun goalReached() = currentPosition == goal
    }
}
