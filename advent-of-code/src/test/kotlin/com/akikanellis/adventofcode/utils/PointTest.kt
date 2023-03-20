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
        expectedPointAfterPlusY: Point
    ) {
        assertEquals(expectedPointAfterZeroX, point.zeroX())
        assertEquals(expectedPointAfterMinusX, point.minusX())
        assertEquals(expectedPointAfterPlusX, point.plusX())

        assertEquals(expectedPointAfterZeroY, point.zeroY())
        assertEquals(expectedPointAfterMinusY, point.minusY())
        assertEquals(expectedPointAfterPlusY, point.plusY())
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
                Point(0, 1)
            ),
            Arguments.of(
                Point(2, 3),

                Point(0, 3),
                Point(1, 3),
                Point(3, 3),

                Point(2, 0),
                Point(2, 2),
                Point(2, 4)
            ),
            Arguments.of(
                Point(-2, -3),

                Point(0, -3),
                Point(-3, -3),
                Point(-1, -3),

                Point(-2, 0),
                Point(-2, -4),
                Point(-2, -2)
            ),
            Arguments.of(
                Point(-5, 5),

                Point(0, 5),
                Point(-6, 5),
                Point(-4, 5),

                Point(-5, 0),
                Point(-5, 4),
                Point(-5, 6)
            )
        )
    }
}
