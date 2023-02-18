package com.akikanellis.adventofcode.year2022

object Day10 {
    fun sumOfSignalStrengths(input: String): Int {
        var x = 1
        var sumOfSignalStrengths = 0
        var cycle = 0

        val cyclesOfInterest = IntProgression.fromClosedRange(20, 220, 40)

        input.lines()
            .filter { it.isNotBlank() }
            .forEach { instruction ->
                cycle++
                if (cyclesOfInterest.contains(cycle)) sumOfSignalStrengths += (x * cycle)

                if (instruction.startsWith("addx")) {
                    cycle++
                    if (cyclesOfInterest.contains(cycle)) sumOfSignalStrengths += (x * cycle)
                    x += instruction.split(" ")[1].toInt()
                }
            }

        return sumOfSignalStrengths
    }


    // for each command
    // tick++
    // if tick divisible by 40, add X to sum
    // if command is addx, increase X and tick++ and if
    // map to command, cycle count
    //

}
