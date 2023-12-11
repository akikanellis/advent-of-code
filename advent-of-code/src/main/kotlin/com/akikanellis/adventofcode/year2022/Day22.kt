package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.Day22.TileType.WALL
import com.akikanellis.adventofcode.year2022.utils.Point

object Day22 {
    fun password(input: String, cubeProcessing: Boolean): Int {
        val (boardInput, pathInput) = input.split("\n\n")
        val board = Board.of(boardInput, cubeProcessing)
        val pathInstructions = pathInstructions(pathInput)

        val finalBoardPosition = pathInstructions
            .fold(
                BoardPosition(board.startingTile, Direction.R),
            ) { currentPosition, pathInstruction ->
                currentPosition.followInstruction(pathInstruction, board)
            }

        return finalBoardPosition.passwordValue()
    }

    private fun pathInstructions(pathInput: String) = "([0-9]+|[A-Z])".toRegex()
        .findAll(pathInput)
        .map { PathInstruction.of(it.value) }
        .toList()

    private data class Board(val tiles: List<Tile>, val cubeProcessing: Boolean) {
        private val positionToTile = tiles.associateBy { it.position }
        private val firstCubeSideIsUp: Boolean
        private val cubeSides: List<CubeSide>

        val startingTile = tiles.minBy { it.y }

        init {
            val maxX = tiles.maxOf { it.x }
            val maxY = tiles.maxOf { it.y }

            val rowWithLeastAmountOfTiles = tiles
                .groupBy { it.y }
                .minBy { it.value.size }
            val sizeOfEachCubeSide = rowWithLeastAmountOfTiles.value.size

            firstCubeSideIsUp = rowWithLeastAmountOfTiles.key in 0 until sizeOfEachCubeSide

            cubeSides = (0..maxY step sizeOfEachCubeSide)
                .flatMap { y ->
                    (0..maxX step sizeOfEachCubeSide).mapNotNull { x ->
                        tiles
                            .filter {
                                it.x in x until x + sizeOfEachCubeSide &&
                                    it.y in y until y + sizeOfEachCubeSide
                            }
                            .takeIf { it.isNotEmpty() }
                            ?.let { CubeSide(it) }
                    }
                }

            zipTilesTogether()
        }

        private fun zipTilesTogether() {
            if (cubeProcessing) {
                zipForCube()
            } else {
                zipAdjacent()
            }
        }

        private fun zipForCube() {
            if (firstCubeSideIsUp) {
                cubeSides[0].zipWith(cubeSides[1], Direction.U, Direction.U)
                cubeSides[0].zipWith(cubeSides[3], Direction.D, Direction.U)
                cubeSides[0].zipWith(cubeSides[2], Direction.L, Direction.U)
                cubeSides[0].zipWith(cubeSides[5], Direction.R, Direction.R)

                cubeSides[1].zipWith(cubeSides[0], Direction.U, Direction.U)
                cubeSides[1].zipWith(cubeSides[4], Direction.D, Direction.D)
                cubeSides[1].zipWith(cubeSides[5], Direction.L, Direction.D)
                cubeSides[1].zipWith(cubeSides[2], Direction.R, Direction.L)

                cubeSides[2].zipWith(cubeSides[0], Direction.U, Direction.L)
                cubeSides[2].zipWith(cubeSides[4], Direction.D, Direction.L)
                cubeSides[2].zipWith(cubeSides[1], Direction.L, Direction.R)
                cubeSides[2].zipWith(cubeSides[3], Direction.R, Direction.L)

                cubeSides[3].zipWith(cubeSides[0], Direction.U, Direction.D)
                cubeSides[3].zipWith(cubeSides[4], Direction.D, Direction.U)
                cubeSides[3].zipWith(cubeSides[2], Direction.L, Direction.R)
                cubeSides[3].zipWith(cubeSides[5], Direction.R, Direction.U)

                cubeSides[4].zipWith(cubeSides[3], Direction.U, Direction.D)
                cubeSides[4].zipWith(cubeSides[1], Direction.D, Direction.D)
                cubeSides[4].zipWith(cubeSides[2], Direction.L, Direction.D)
                cubeSides[4].zipWith(cubeSides[5], Direction.R, Direction.L)

                cubeSides[5].zipWith(cubeSides[3], Direction.U, Direction.R)
                cubeSides[5].zipWith(cubeSides[1], Direction.D, Direction.L)
                cubeSides[5].zipWith(cubeSides[4], Direction.L, Direction.R)
                cubeSides[5].zipWith(cubeSides[0], Direction.R, Direction.R)
            } else {
                cubeSides[0].zipWith(cubeSides[5], Direction.U, Direction.L)
                cubeSides[0].zipWith(cubeSides[2], Direction.D, Direction.U)
                cubeSides[0].zipWith(cubeSides[3], Direction.L, Direction.L)
                cubeSides[0].zipWith(cubeSides[1], Direction.R, Direction.L)

                cubeSides[1].zipWith(cubeSides[5], Direction.U, Direction.D)
                cubeSides[1].zipWith(cubeSides[2], Direction.D, Direction.R)
                cubeSides[1].zipWith(cubeSides[0], Direction.L, Direction.R)
                cubeSides[1].zipWith(cubeSides[4], Direction.R, Direction.R)

                cubeSides[2].zipWith(cubeSides[0], Direction.U, Direction.D)
                cubeSides[2].zipWith(cubeSides[4], Direction.D, Direction.U)
                cubeSides[2].zipWith(cubeSides[3], Direction.L, Direction.U)
                cubeSides[2].zipWith(cubeSides[1], Direction.R, Direction.D)

                cubeSides[3].zipWith(cubeSides[2], Direction.U, Direction.L)
                cubeSides[3].zipWith(cubeSides[5], Direction.D, Direction.U)
                cubeSides[3].zipWith(cubeSides[0], Direction.L, Direction.L)
                cubeSides[3].zipWith(cubeSides[4], Direction.R, Direction.L)

                cubeSides[4].zipWith(cubeSides[2], Direction.U, Direction.D)
                cubeSides[4].zipWith(cubeSides[5], Direction.D, Direction.R)
                cubeSides[4].zipWith(cubeSides[3], Direction.L, Direction.R)
                cubeSides[4].zipWith(cubeSides[1], Direction.R, Direction.R)

                cubeSides[5].zipWith(cubeSides[3], Direction.U, Direction.D)
                cubeSides[5].zipWith(cubeSides[1], Direction.D, Direction.U)
                cubeSides[5].zipWith(cubeSides[0], Direction.L, Direction.U)
                cubeSides[5].zipWith(cubeSides[4], Direction.R, Direction.D)
            }
        }

        private fun zipAdjacent() {
            cubeSides.forEach { cubeSide -> cubeSide.zipAdjacent(this) }
        }

        operator fun get(position: Point) = positionToTile[position]

        companion object {
            fun of(representation: String, cubeProcessing: Boolean) = representation
                .lines()
                .flatMapIndexed { y: Int, row: String ->
                    row.mapIndexedNotNull { x, tileType ->
                        TileType.of(tileType)?.let { Tile(position = Point(x, y), type = it) }
                    }
                }
                .let { Board(it, cubeProcessing) }
        }
    }

    private data class Tile(val position: Point, val type: TileType) {
        val x = position.x
        val y = position.y

        private val nextBoardPositions = mutableMapOf<Direction, BoardPosition>()

        fun addNextBoardPosition(
            nextTile: Tile,
            exitDirection: Direction,
            entryDirection: Direction = exitDirection,
        ) {
            nextBoardPositions += exitDirection to BoardPosition(nextTile, entryDirection)
        }

        fun nextBoardPosition(exitDirection: Direction) = nextBoardPositions[exitDirection]!!
    }

    private enum class TileType {
        OPEN,
        WALL,
        ;

        companion object {
            fun of(representation: Char) = when (representation) {
                '.' -> OPEN
                '#' -> WALL
                ' ' -> null
                else -> error("Unknown representation '$representation'")
            }
        }
    }

    private data class CubeSide(val tiles: Collection<Tile>) {
        private val topRow: List<Tile>
        private val bottomRow: List<Tile>
        private val leftColumn: List<Tile>
        private val rightColumn: List<Tile>

        init {
            val minX = tiles.minOf { it.x }
            val maxX = tiles.maxOf { it.x }
            val minY = tiles.minOf { it.y }
            val maxY = tiles.maxOf { it.y }

            topRow = tiles.filter { it.y == minY }
            bottomRow = tiles.filter { it.y == maxY }
            leftColumn = tiles.filter { it.x == minX }
            rightColumn = tiles.filter { it.x == maxX }

            val positionToTile = tiles.associateBy { it.position }

            tiles.forEach { tile ->
                positionToTile[tile.position.minusY()]
                    ?.also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.U) }
                positionToTile[tile.position.plusY()]
                    ?.also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.D) }
                positionToTile[tile.position.minusX()]
                    ?.also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.L) }
                positionToTile[tile.position.plusX()]
                    ?.also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.R) }
            }
        }

        fun zipWith(otherCubeSide: CubeSide, exitDirection: Direction, entryDirection: Direction) {
            val exitTiles = tiles(exitDirection)
            val entryTiles = otherCubeSide
                .tiles(entryDirection)
                .let {
                    if (exitDirection == entryDirection) return@let it.reversed()

                    return@let when (Pair(exitDirection, entryDirection)) {
                        Pair(Direction.U, Direction.R),
                        Pair(Direction.D, Direction.L),
                        Pair(Direction.L, Direction.D),
                        Pair(Direction.R, Direction.U),
                        -> it.reversed()
                        else -> it
                    }
                }

            val newDirection = entryDirection.oppositeDirection()

            exitTiles
                .zip(entryTiles)
                .forEach { (exitTile, entryTile) ->
                    exitTile.addNextBoardPosition(
                        entryTile,
                        exitDirection,
                        newDirection,
                    )
                }
        }

        private fun tiles(exitDirection: Direction) = when (exitDirection) {
            Direction.U -> topRow
            Direction.D -> bottomRow
            Direction.L -> leftColumn
            Direction.R -> rightColumn
        }

        fun zipAdjacent(board: Board) {
            topRow.forEach { tile ->
                (
                    board[tile.position.minusY()]
                        ?: board.tiles.filter { it.x == tile.x }.maxBy { it.y }
                    )
                    .also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.U) }
            }
            bottomRow.forEach { tile ->
                (
                    board[tile.position.plusY()]
                        ?: board.tiles.filter { it.x == tile.x }.minBy { it.y }
                    )
                    .also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.D) }
            }
            leftColumn.forEach { tile ->
                (
                    board[tile.position.minusX()]
                        ?: board.tiles.filter { it.y == tile.y }.maxBy { it.x }
                    )
                    .also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.L) }
            }
            rightColumn.forEach { tile ->
                (
                    board[(tile.position.plusX())]
                        ?: board.tiles.filter { it.y == tile.y }.minBy { it.x }
                    )
                    .also { nextTile -> tile.addNextBoardPosition(nextTile, Direction.R) }
            }
        }
    }

    private sealed interface PathInstruction {
        companion object {
            fun of(representation: String) = if (representation.toIntOrNull() == null) {
                TurnInstruction.of(representation)
            } else {
                MoveInstruction.of(representation)
            }
        }
    }

    private class TurnInstruction(val turnDirection: TurnDirection) : PathInstruction {
        companion object {
            fun of(representation: String) = TurnInstruction(TurnDirection.valueOf(representation))
        }
    }

    private enum class TurnDirection { L, R }

    private class MoveInstruction(val steps: Int) : PathInstruction {
        companion object {
            fun of(representation: String) = MoveInstruction(representation.toInt())
        }
    }

    private data class BoardPosition(val tile: Tile, val direction: Direction) {
        fun followInstruction(pathInstruction: PathInstruction, board: Board) =
            when (pathInstruction) {
                is TurnInstruction -> turn(pathInstruction.turnDirection)
                is MoveInstruction -> {
                    var newBoardPosition = this
                    for (i in 0 until pathInstruction.steps) {
                        val potentialNewPosition = newBoardPosition.move()

                        if (board[potentialNewPosition.tile.position]!!.type == WALL) break

                        newBoardPosition = potentialNewPosition
                    }

                    newBoardPosition
                }
            }

        fun move() = tile.nextBoardPosition(direction)

        fun turn(turnDirection: TurnDirection) = when (direction) {
            Direction.U -> when (turnDirection) {
                TurnDirection.L -> Direction.L
                TurnDirection.R -> Direction.R
            }

            Direction.D -> when (turnDirection) {
                TurnDirection.L -> Direction.R
                TurnDirection.R -> Direction.L
            }

            Direction.L -> when (turnDirection) {
                TurnDirection.L -> Direction.D
                TurnDirection.R -> Direction.U
            }

            Direction.R -> when (turnDirection) {
                TurnDirection.L -> Direction.U
                TurnDirection.R -> Direction.D
            }
        }.let { copy(direction = it) }

        fun passwordValue() = ((tile.y + 1) * 1000) + ((tile.x + 1) * 4) + direction.passwordValue()
    }

    private enum class Direction {
        U {
            override fun oppositeDirection() = D
            override fun passwordValue() = 3
        },
        D {
            override fun oppositeDirection() = U
            override fun passwordValue() = 1
        },
        L {
            override fun oppositeDirection() = R
            override fun passwordValue() = 2
        },
        R {
            override fun oppositeDirection() = L
            override fun passwordValue() = 0
        },
        ;

        abstract fun oppositeDirection(): Direction
        abstract fun passwordValue(): Int
    }
}
