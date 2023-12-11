package com.akikanellis.adventofcode.utils

import com.akikanellis.adventofcode.year2022.utils.Point
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class PointTest {
    @ParameterizedTest
    @MethodSource("points")
    fun `calculates next positions`(
        point: Point,
        expectedPointAfterZeroX: Point,
        expectedPointAfterMinusX: Point,
        expectedPointAfterPlusX: Point,
        expectedPointAfterZeroY: Point,
        expectedPointAfterMinusY: Point,
        expectedPointAfterPlusY: Point,
    ) {
        assertEquals(expectedPointAfterZeroX, point.zeroX())
        assertEquals(expectedPointAfterMinusX, point.minusX())
        assertEquals(expectedPointAfterPlusX, point.plusX())

        assertEquals(expectedPointAfterZeroY, point.zeroY())
        assertEquals(expectedPointAfterMinusY, point.minusY())
        assertEquals(expectedPointAfterPlusY, point.plusY())
    }

    @ParameterizedTest
    @MethodSource("manhattanDistances")
    fun `calculates Manhattan distances`(
        point: Point,
        otherPoint: Point,
        expectedManhattanDistance: Int,
    ) {
        val manhattanDistance = point.manhattanDistance(otherPoint)

        assertEquals(expectedManhattanDistance, manhattanDistance)
    }

    companion object {
        @JvmStatic
        fun points() = listOf(
            Arguments.of(
                Point.ZERO,

                Point(0, 0),
                Point(-1, 0),
                Point(1, 0),

                Point(0, 0),
                Point(0, -1),
                Point(0, 1),
            ),
            Arguments.of(
                Point(2, 3),

                Point(0, 3),
                Point(1, 3),
                Point(3, 3),

                Point(2, 0),
                Point(2, 2),
                Point(2, 4),
            ),
            Arguments.of(
                Point(-2, -3),

                Point(0, -3),
                Point(-3, -3),
                Point(-1, -3),

                Point(-2, 0),
                Point(-2, -4),
                Point(-2, -2),
            ),
            Arguments.of(
                Point(-5, 5),

                Point(0, 5),
                Point(-6, 5),
                Point(-4, 5),

                Point(-5, 0),
                Point(-5, 4),
                Point(-5, 6),
            ),
        )

        @JvmStatic
        fun manhattanDistances() = listOf(
            // Zero distance
            Arguments.of(Point.ZERO, Point.ZERO, 0),
            Arguments.of(
                Point(Int.MIN_VALUE, Int.MIN_VALUE),
                Point(Int.MIN_VALUE, Int.MIN_VALUE),
                0,
            ),
            Arguments.of(
                Point(Int.MAX_VALUE, Int.MAX_VALUE),
                Point(Int.MAX_VALUE, Int.MAX_VALUE),
                0,
            ),

            // Single axis
            Arguments.of(Point.ZERO, Point(1, 0), 1),
            Arguments.of(Point.ZERO, Point(0, 1), 1),
            Arguments.of(Point.ZERO, Point(-1, 0), 1),
            Arguments.of(Point.ZERO, Point(0, -1), 1),

            // Diagonal
            Arguments.of(Point(1, 2), Point(2, 1), 2),
            Arguments.of(Point(2, 1), Point(1, 2), 2),
            Arguments.of(Point(-1, -2), Point(-2, -1), 2),
            Arguments.of(Point(-2, -1), Point(-1, -2), 2),
            Arguments.of(Point(-1, 1), Point(1, -1), 4),

            // Large distance
            Arguments.of(Point(1, 2), Point(60, 74), 131),
            Arguments.of(Point(15, 29), Point(60, 74), 90),
            Arguments.of(Point(-15, -29), Point(-60, -74), 90),
        )
    }
}
