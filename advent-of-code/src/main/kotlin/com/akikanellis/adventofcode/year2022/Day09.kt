package com.akikanellis.adventofcode.year2022

import kotlin.math.absoluteValue

object Day09 {
    fun numberOfPositionsTailVisited(input: String, numberOfKnots: Int) = input.lines()
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .flatMap { (direction, times) -> (1..times.toInt()).map { Direction.valueOf(direction) } }
        .fold(Knots(numberOfKnots)) { knots, direction -> knots.move(direction) }
        .tail
        .positionsVisited
        .size

    private enum class Direction { U, D, L, R }

    private data class Knots(private val knots: List<Knot>) {
        constructor(numberOfKnots: Int) : this((1..numberOfKnots).map { Knot() })

        val head = knots.first()
        val tail = knots.last()

        fun move(direction: Direction): Knots {
            val newHead = head.move(direction)

            val newKnots = knots
                .drop(1)
                .fold(listOf(newHead)) { latestKnots, tail ->
                    latestKnots + tail.follow(latestKnots.last())
                }

            return Knots(newKnots)
        }
    }

    private data class Knot(
        val currentPosition: Pair<Int, Int> = Pair(0, 0),
        val positionsVisited: Set<Pair<Int, Int>> = setOf(currentPosition)
    ) {
        val x = currentPosition.first
        val y = currentPosition.second

        fun move(direction: Direction): Knot {
            val newPosition = when (direction) {
                Direction.U -> currentPosition.copy(second = y + 1)
                Direction.D -> currentPosition.copy(second = y - 1)
                Direction.L -> currentPosition.copy(first = x - 1)
                Direction.R -> currentPosition.copy(first = x + 1)
            }

            return Knot(
                currentPosition = newPosition,
                positionsVisited = positionsVisited + newPosition
            )
        }

        fun follow(otherKnot: Knot): Knot {
            if (touches(otherKnot)) return this

            val newPosition =
                Pair(
                    first = when {
                        otherKnot.x > x -> x + 1
                        otherKnot.x < x -> x - 1
                        else -> x
                    },
                    second = when {
                        otherKnot.y > y -> y + 1
                        otherKnot.y < y -> y - 1
                        else -> y
                    }
                )

            return Knot(
                currentPosition = newPosition,
                positionsVisited = positionsVisited + newPosition
            )
        }

        private fun touches(otherKnot: Knot) =
            (otherKnot.x - x).absoluteValue <= 1 && (otherKnot.y - y).absoluteValue <= 1
    }
}
