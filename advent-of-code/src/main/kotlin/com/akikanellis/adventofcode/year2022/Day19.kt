package com.akikanellis.adventofcode.year2022

import java.util.*
import kotlin.math.ceil
import kotlin.math.max

object Day19 {
    private val BLUEPRINT_REGEX = """\d+""".toRegex()

    fun qualityLevelsOfBlueprints(input: String) = blueprints(input)
        .map { blueprint -> Pair(blueprint.id, blueprint.maxGeodeThatCanBeOpened(24)) }
        .sumOf { (id, maxGeodeThatCanBeOpened) -> id * maxGeodeThatCanBeOpened }

    fun maxGeodeThatCanBeOpenedForTopThreeBlueprints(input: String) = blueprints(input)
        .take(3)
        .map { blueprint -> blueprint.maxGeodeThatCanBeOpened(32) }
        .reduce(Int::times)

    private fun blueprints(input: String) = input.lines()
        .filter { it.isNotBlank() }
        .map { line -> BLUEPRINT_REGEX.findAll(line).map { it.value.toInt() }.toList() }
        .map { idAndCosts ->
            Blueprint(
                id = idAndCosts[0],
                robots = listOf(
                    OreRobot(Materials(ore = idAndCosts[1])),
                    ClayRobot(Materials(ore = idAndCosts[2])),
                    ObsidianRobot(Materials(ore = idAndCosts[3], clay = idAndCosts[4])),
                    GeodeRobot(Materials(ore = idAndCosts[5], obsidian = idAndCosts[6])),
                ),
            )
        }

    private data class Blueprint(val id: Int, val robots: List<Robot>) {
        val maxMaterialCosts = Materials(
            ore = robots.maxOf { it.materialCosts.ore },
            clay = robots.maxOf { it.materialCosts.clay },
            obsidian = robots.maxOf { it.materialCosts.obsidian },
        )

        fun maxGeodeThatCanBeOpened(minutes: Int): Int {
            var currentMaxGeodeThatCanBeOpened = 0
            val statesQueue = PriorityQueue(
                listOf(
                    SystemState(
                        remainingMinutes = minutes,
                        materials = Materials(ore = 1),
                        robotInventory = RobotInventory(listOf(robots.first { it is OreRobot })),
                    ),
                ),
            )

            while (statesQueue.isNotEmpty()) {
                val currentState = statesQueue.poll()

                if (currentState.canNotOutperform(currentMaxGeodeThatCanBeOpened)) continue

                currentMaxGeodeThatCanBeOpened = max(
                    currentMaxGeodeThatCanBeOpened,
                    currentState.geodeThatCanBeOpened,
                )
                statesQueue += currentState.nextStates(this)
            }

            return currentMaxGeodeThatCanBeOpened
        }
    }

    private interface Robot {
        val materialCosts: Materials

        fun moreRobotsOfTypeNeeded(
            robotInventory: RobotInventory,
            maxMaterialCosts: Materials,
        ): Boolean
    }

    private data class OreRobot(override val materialCosts: Materials) : Robot {
        override fun moreRobotsOfTypeNeeded(
            robotInventory: RobotInventory,
            maxMaterialCosts: Materials,
        ) = maxMaterialCosts.ore > robotInventory.oreRobots
    }

    private data class ClayRobot(override val materialCosts: Materials) : Robot {
        override fun moreRobotsOfTypeNeeded(
            robotInventory: RobotInventory,
            maxMaterialCosts: Materials,
        ) = maxMaterialCosts.clay > robotInventory.clayRobots
    }

    private data class ObsidianRobot(override val materialCosts: Materials) : Robot {
        override fun moreRobotsOfTypeNeeded(
            robotInventory: RobotInventory,
            maxMaterialCosts: Materials,
        ) = maxMaterialCosts.obsidian > robotInventory.obsidianRobots
    }

    private data class GeodeRobot(override val materialCosts: Materials) : Robot {
        override fun moreRobotsOfTypeNeeded(
            robotInventory: RobotInventory,
            maxMaterialCosts: Materials,
        ) = robotInventory.obsidianRobots > 0
    }

    private data class Materials(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0,
    ) {
        operator fun plus(other: Materials) = Materials(
            ore = ore + other.ore,
            clay = clay + other.clay,
            obsidian = obsidian + other.obsidian,
            geode = geode + other.geode,
        )

        operator fun minus(other: Materials) = Materials(
            ore = (ore - other.ore).coerceAtLeast(0),
            clay = (clay - other.clay).coerceAtLeast(0),
            obsidian = (obsidian - other.obsidian).coerceAtLeast(0),
            geode = (geode - other.geode).coerceAtLeast(0),
        )
    }

    private data class RobotInventory(val robots: List<Robot>) {
        val oreRobots = robots.count { it is OreRobot }
        val clayRobots = robots.count { it is ClayRobot }
        val obsidianRobots = robots.count { it is ObsidianRobot }
        val geodeRobots = robots.count { it is GeodeRobot }

        operator fun plus(robot: Robot) = RobotInventory(robots + robot)
    }

    private data class SystemState(
        val remainingMinutes: Int,
        val materials: Materials,
        val robotInventory: RobotInventory,
    ) : Comparable<SystemState> {
        private val maxGeodeThatCanBeOpened: Int
            get() {
                val n = remainingMinutes - 2
                val sumOfRemainingMinutes = n * (n + 1) / 2
                val potentialGeode = sumOfRemainingMinutes + robotInventory.geodeRobots * (n + 1)

                return materials.geode + potentialGeode
            }
        val geodeThatCanBeOpened =
            materials.geode + robotInventory.geodeRobots * (remainingMinutes - 1)

        override fun compareTo(other: SystemState) =
            other.materials.geode.compareTo(materials.geode)

        fun canNotOutperform(geode: Int) = maxGeodeThatCanBeOpened <= geode

        fun nextStates(blueprint: Blueprint) = blueprint.robots
            .filter { it.moreRobotsOfTypeNeeded(robotInventory, blueprint.maxMaterialCosts) }
            .mapNotNull { nextStateWithRobotCreated(it) }

        private fun nextStateWithRobotCreated(robot: Robot): SystemState? {
            val minutesToBuild = minutesToBuild(robot)
            val remainingMinutesAfterRobotBuilt = remainingMinutes - minutesToBuild

            if (remainingMinutesAfterRobotBuilt <= 0) return null

            val robotProducedMaterial = Materials(
                ore = robotInventory.oreRobots * minutesToBuild,
                clay = robotInventory.clayRobots * minutesToBuild,
                obsidian = robotInventory.obsidianRobots * minutesToBuild,
                geode = robotInventory.geodeRobots * minutesToBuild,
            )

            return SystemState(
                remainingMinutes = remainingMinutesAfterRobotBuilt,
                materials = materials + robotProducedMaterial - robot.materialCosts,
                robotInventory = robotInventory + robot,
            )
        }

        private fun minutesToBuild(robot: Robot): Int {
            val remainingMaterial = robot.materialCosts - materials

            return maxOf(
                ceil(remainingMaterial.ore / robotInventory.oreRobots.toFloat()).toInt(),
                ceil(remainingMaterial.clay / robotInventory.clayRobots.toFloat()).toInt(),
                ceil(remainingMaterial.obsidian / robotInventory.obsidianRobots.toFloat()).toInt(),
                ceil(remainingMaterial.geode / robotInventory.geodeRobots.toFloat()).toInt(),
            ) + 1
        }
    }
}
