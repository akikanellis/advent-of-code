package com.akikanellis.adventofcode.year2022

object Day16 {
    private val VALVES_REGEX =
        "Valve (.+) has flow rate=(.+); tunnels? leads? to valves? (.+)".toRegex()

    fun highestPressureThanCanBeReleased(input: String, elephantWillHelp: Boolean) =
        valves(input, elephantWillHelp)
            .highestPressureThanCanBeReleased()

    private fun valves(input: String, elephantWillHelp: Boolean): Valves {
        val valvesToNeighbourNames = valvesToNeighbourNames(input)
        val valves = valvesToNeighbourNames.map { (valve, _) -> valve }

        valvesToNeighbourNames.forEach { (valve, neighbourNames) ->
            valve.neighbours = valves.filter { it.name in neighbourNames }
        }
        valves.forEach { valve -> initialiseDistancesToOtherValves(valve) }

        return Valves(
            valves = valves,
            elephantWillHelp = elephantWillHelp
        )
    }

    private fun valvesToNeighbourNames(input: String) = input.lines()
        .filter { it.isNotBlank() }
        .map { line ->
            val groupValues = VALVES_REGEX.matchEntire(line)!!.groupValues
            Pair(
                Valve(
                    name = groupValues[1],
                    flowRate = groupValues[2].toInt()
                ),
                groupValues[3].split(", ")
            )
        }

    private fun initialiseDistancesToOtherValves(valve: Valve) {
        val valveNameToShortestDistance = mutableMapOf<String, Int>()
            .apply { put(valve.name, 0) }

        val remainingValves = mutableListOf(valve)
        while (remainingValves.isNotEmpty()) {
            val currentValve = remainingValves.removeFirst()
            val updatedDistanceToCurrentValveNeighbours =
                valveNameToShortestDistance[currentValve.name]!! + 1

            currentValve.neighbours.forEach { currentValveNeighbour ->
                val precomputedDistanceToCurrentValveNeighbour =
                    valveNameToShortestDistance.getOrDefault(
                        currentValveNeighbour.name,
                        Int.MAX_VALUE
                    )

                if (
                    updatedDistanceToCurrentValveNeighbours <
                    precomputedDistanceToCurrentValveNeighbour
                ) {
                    valveNameToShortestDistance[currentValveNeighbour.name] =
                        updatedDistanceToCurrentValveNeighbours
                    remainingValves += currentValveNeighbour
                }
            }
        }

        valve.valveNameToShortestDistance = valveNameToShortestDistance
    }

    private class Valves(
        val startValve: Valve,
        val openableValves: Set<Valve>,
        val elephantWillHelp: Boolean,
        val elephantIsHelping: Boolean
    ) {
        constructor(
            valves: List<Valve>,
            elephantWillHelp: Boolean
        ) : this(
            startValve = valves.single { it.name == "AA" },
            openableValves = valves.filter { it.flowRate > 0 }.toSet(),
            elephantWillHelp = elephantWillHelp,
            elephantIsHelping = false
        )

        val totalMinutes = if (elephantWillHelp || elephantIsHelping) 26 else 30
        val precomputedSystemStateToMaxPressure = mutableMapOf<SystemState, Int>()

        fun highestPressureThanCanBeReleased() = highestPressureThanCanBeReleased(
            remainingMinutes = totalMinutes,
            currentValve = startValve,
            remainingValves = openableValves
        )

        private fun highestPressureThanCanBeReleased(
            remainingMinutes: Int,
            currentValve: Valve,
            remainingValves: Set<Valve>
        ): Int {
            val currentValvePressure = remainingMinutes * currentValve.flowRate
            val currentSystemState = SystemState(
                currentValveName = currentValve.name,
                remainingMinutes = remainingMinutes,
                remainingValves = remainingValves
            )

            return currentValvePressure + precomputedSystemStateToMaxPressure
                .getOrPut(currentSystemState) {
                    val remainingValvesMaxPressure = remainingValves
                        .filter { nextValve ->
                            currentValve.distanceTo(nextValve) < remainingMinutes
                        }
                        .maxOfOrNull { nextValve ->
                            val remainingMinutesForNextValve =
                                remainingMinutes - currentValve.distanceTo(nextValve) - 1

                            highestPressureThanCanBeReleased(
                                remainingMinutes = remainingMinutesForNextValve,
                                currentValve = nextValve,
                                remainingValves = remainingValves - nextValve
                            )
                        } ?: 0

                    if (elephantWillHelp && !elephantIsHelping) {
                        maxOf(
                            remainingValvesMaxPressure,
                            Valves(
                                startValve = startValve,
                                openableValves = remainingValves,
                                elephantWillHelp = false,
                                elephantIsHelping = true
                            ).highestPressureThanCanBeReleased()
                        )
                    } else {
                        remainingValvesMaxPressure
                    }
                }
        }
    }

    private data class Valve(val name: String, val flowRate: Int) {
        lateinit var neighbours: List<Valve>
        lateinit var valveNameToShortestDistance: Map<String, Int>

        fun distanceTo(other: Valve) = valveNameToShortestDistance[other.name]!!
    }

    private data class SystemState(
        val currentValveName: String,
        val remainingMinutes: Int,
        val remainingValves: Set<Valve>
    )
}
