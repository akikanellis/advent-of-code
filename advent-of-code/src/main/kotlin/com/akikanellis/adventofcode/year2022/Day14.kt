package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.utils.Point

object Day14 {
    private val SAND_STARTING_POINT = Point(500, 0)

    fun unitsOfSandRestingBeforeFreeFall(input: String) =
        unitsOfSandResting(input, sandFreeFalls = true)

    fun unitsOfSandRestingWhenFull(input: String) = unitsOfSandResting(input, sandFreeFalls = false)

    private fun unitsOfSandResting(input: String, sandFreeFalls: Boolean): Int {
        val rockAndSandPoints = scannedRockPoints(input)

        val minRockXWithoutFloor = rockAndSandPoints.minOf { it.x }
        val maxRockXWithoutFloor = rockAndSandPoints.maxOf { it.x }
        val maxRockYWithoutFloor = rockAndSandPoints.maxOf { it.y }

        rockAndSandPoints += floorRockPoints(
            minRockXWithoutFloor,
            maxRockXWithoutFloor,
            maxRockYWithoutFloor,
        )

        var moreSandCanFit = true
        var amountOfRestingSand = 0

        while (moreSandCanFit) {
            var currentSandPoint = SAND_STARTING_POINT
            var sandIsMoving = true

            while (sandIsMoving) {
                if (sandFreeFalls && currentSandPoint.y > maxRockYWithoutFloor) {
                    sandIsMoving = false
                    moreSandCanFit = false
                } else if (currentSandPoint.plusY() !in rockAndSandPoints) {
                    currentSandPoint = currentSandPoint.plusY()
                } else if (currentSandPoint.minusX().plusY() !in rockAndSandPoints) {
                    currentSandPoint = currentSandPoint.minusX().plusY()
                } else if (currentSandPoint.plusX().plusY() !in rockAndSandPoints) {
                    currentSandPoint = currentSandPoint.plusX().plusY()
                } else {
                    rockAndSandPoints += currentSandPoint
                    amountOfRestingSand++
                    sandIsMoving = false
                }
            }

            if (SAND_STARTING_POINT in rockAndSandPoints) {
                moreSandCanFit = false
            }
        }

        return amountOfRestingSand
    }

    private fun scannedRockPoints(input: String) = input
        .lines()
        .filter { it.isNotBlank() }
        .flatMap {
            it
                .split(" -> ")
                .map { coordinates ->
                    coordinates
                        .split(",")
                        .let { xAndY -> Point(xAndY[0].toInt(), xAndY[1].toInt()) }
                }
                .zipWithNext()
                .flatMap { (leftPoint, rightPoint) ->
                    (leftPoint.x..rightPoint.x).map { x -> leftPoint.copy(x = x) } +
                        (leftPoint.y..rightPoint.y).map { y -> leftPoint.copy(y = y) } +
                        (rightPoint.x..leftPoint.x).map { x -> rightPoint.copy(x = x) } +
                        (rightPoint.y..leftPoint.y).map { y -> rightPoint.copy(y = y) }
                }
        }.toMutableSet()

    private fun floorRockPoints(minX: Int, maxX: Int, maxY: Int) =
        (minX - maxY..maxX + maxY).map { x -> Point(x, maxY + 2) }
}
