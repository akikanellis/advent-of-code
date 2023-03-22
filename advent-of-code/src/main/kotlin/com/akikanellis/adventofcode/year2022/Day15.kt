package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.utils.Point
import kotlin.math.abs

object Day15 {
    private val SENSORS_TO_BEACONS_REGEX =
        "Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)".toRegex()
    private const val DISTRESS_BEACON_MIN_COORDINATE = 0
    private const val TUNING_FREQUENCY_MULTIPLIER = 4_000_000

    fun numberOfPositionsWithoutBeacon(input: String, targetRow: Int): Int {
        val sensorsToClosestBeacons = sensorsToClosestBeacons(input)

        val pointsWithoutABeaconInRow = mutableSetOf<Point>()
        val beaconsInRow = mutableSetOf<Point>()

        for ((sensor, closestBeacon) in sensorsToClosestBeacons) {
            if (closestBeacon.y == targetRow) beaconsInRow += closestBeacon

            val sensorToBeaconDistance = sensor.manhattanDistance(closestBeacon)
            val xOffset = sensorToBeaconDistance - abs(sensor.y - targetRow)

            (sensor.x - xOffset..sensor.x + xOffset).forEach { x ->
                pointsWithoutABeaconInRow += Point(x, targetRow)
            }
        }

        return pointsWithoutABeaconInRow.size - beaconsInRow.size
    }

    fun tuningFrequencyOfDistressBeacon(input: String, distressBeaconMaxCoordinate: Int): Long {
        val sensorsToClosestBeacons = sensorsToClosestBeacons(input)

        for (y in DISTRESS_BEACON_MIN_COORDINATE..distressBeaconMaxCoordinate) {
            var x = DISTRESS_BEACON_MIN_COORDINATE

            while (x <= distressBeaconMaxCoordinate) {
                val candidateDistressBeacon = Point(x, y)

                val xAfterSkippingPointsWithoutBeacon = xAfterSkippingPointsWithoutBeacon(
                    sensorsToClosestBeacons,
                    candidateDistressBeacon
                )

                if (xAfterSkippingPointsWithoutBeacon != null) {
                    x = xAfterSkippingPointsWithoutBeacon
                } else {
                    return (candidateDistressBeacon.x.toLong() * TUNING_FREQUENCY_MULTIPLIER) +
                        candidateDistressBeacon.y
                }
            }
        }

        error("Could not find distress beacon")
    }

    private fun sensorsToClosestBeacons(input: String) = input.lines()
        .filter { it.isNotBlank() }
        .map { line ->
            val coordinates = SENSORS_TO_BEACONS_REGEX.matchEntire(line)!!.groupValues
            Pair(
                Point(coordinates[1].toInt(), coordinates[2].toInt()),
                Point(coordinates[3].toInt(), coordinates[4].toInt())
            )
        }

    private fun xAfterSkippingPointsWithoutBeacon(
        sensorsToClosestBeacons: List<Pair<Point, Point>>,
        candidateDistressBeacon: Point
    ): Int? = sensorsToClosestBeacons
        .firstOrNull { (sensor, closestBeacon) ->
            sensor.manhattanDistance(closestBeacon) >=
                sensor.manhattanDistance(candidateDistressBeacon)
        }?.let { (sensor, closestBeacon) ->
            sensor.x +
                sensor.manhattanDistance(closestBeacon) -
                abs(sensor.y - candidateDistressBeacon.y) +
                1
        }

    private fun Point.manhattanDistance(other: Point) = abs(x - other.x) + abs(y - other.y)
}
