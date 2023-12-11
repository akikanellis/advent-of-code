package com.akikanellis.adventofcode.year2022

object Day18 {
    fun surfaceAreaOfScannedLavaDroplet(input: String): Int {
        val cubes = cubes(input)

        return cubes
            .flatMap { cube -> cube.neighbours }
            .count { potentialNeighbour -> potentialNeighbour !in cubes }
    }

    fun surfaceAreaOfScannedLavaDropletWithoutAirPockets(input: String): Int {
        val cubes = cubes(input)

        val minToMaxDimensionBounds = minToMaxDimensionBounds(cubes)

        val visitedCubes = mutableSetOf<Point3D>()
        val cubesToVisit = mutableListOf(
            Point3D(
                minToMaxDimensionBounds.first,
                minToMaxDimensionBounds.first,
                minToMaxDimensionBounds.first,
            ),
        )
        var surfaceArea = 0

        while (cubesToVisit.isNotEmpty()) {
            val nextCube = cubesToVisit.removeFirst()

            if (nextCube in visitedCubes) continue

            visitedCubes += nextCube

            nextCube
                .neighbours
                .filter { potentialNeighbour ->
                    potentialNeighbour.withinBounds(minToMaxDimensionBounds)
                }
                .forEach { potentialNeighbour ->
                    if (potentialNeighbour in cubes) {
                        surfaceArea++
                    } else {
                        cubesToVisit += potentialNeighbour
                    }
                }
        }

        return surfaceArea
    }

    private fun cubes(input: String) = input.lines()
        .filter { it.isNotBlank() }
        .map { it.split(",") }
        .map {
            Point3D(
                it[0].toInt(),
                it[1].toInt(),
                it[2].toInt(),
            )
        }.toSet()

    private fun minToMaxDimensionBounds(cubes: Set<Point3D>): IntRange {
        val minDimension = cubes.minOf { it.minDimension }
        val maxDimension = cubes.maxOf { it.maxDimension }
        return minDimension - 1..maxDimension + 1
    }

    private data class Point3D(val x: Int, val y: Int, val z: Int) {
        val minDimension = minOf(x, y, z)
        val maxDimension = maxOf(x, y, z)
        val neighbours by lazy {
            listOf(
                copy(x = x - 1),
                copy(y = y - 1),
                copy(z = z - 1),
                copy(x = x + 1),
                copy(y = y + 1),
                copy(z = z + 1),
            )
        }

        fun withinBounds(bounds: IntRange) = x in bounds && y in bounds && z in bounds
    }
}
