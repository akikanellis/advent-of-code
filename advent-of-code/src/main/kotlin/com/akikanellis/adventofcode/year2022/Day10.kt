package com.akikanellis.adventofcode.year2022

object Day10 {
    fun sumOfSignalStrengths(input: String): Int {
        val cyclesOfInterest = IntProgression.fromClosedRange(20, 220, 40)

        return systemSnapshots(input)
            .filter { cyclesOfInterest.contains(it.cycle) }
            .sumOf { it.signalStrength }
    }

    fun displayOutput(input: String) = systemSnapshots(input)
        .last()
        .crt
        .displayOutput()

    private fun systemSnapshots(input: String): List<SystemSnapshot> {
        val instructions = input
            .lines()
            .filter { it.isNotBlank() }
            .map { Instruction(it) }
            .toMutableList()

        return (1..240)
            .fold(
                listOf(
                    SystemSnapshot(
                        cycle = 0,
                        sprite = (0..2),
                        signalStrength = 0,
                        crt = Crt(Pair(40, 6))
                    )
                )
            ) { snapshots, cycle ->
                val lastSnapshot = snapshots.last()
                val lastSprite = lastSnapshot.sprite
                val signalStrength = (lastSprite.first + 1) * cycle
                val crt = lastSnapshot.crt.drawNextPixel(lastSprite)

                val instruction = instructions[0].decreasedCost()
                instructions[0] = instruction

                val sprite = if (instruction.remainingCost == 0) {
                    instruction.updatedSprite(lastSprite).also { instructions.removeFirst() }
                } else {
                    lastSprite
                }

                snapshots + SystemSnapshot(
                    cycle = cycle,
                    sprite = sprite,
                    signalStrength = signalStrength,
                    crt = crt
                )
            }
    }

    private data class SystemSnapshot(
        val cycle: Int,
        val sprite: IntRange,
        val signalStrength: Int,
        val crt: Crt
    )

    private data class Instruction(
        private val instruction: String,
        val name: String = instruction.split(" ")[0],
        val startingCost: Int = if (name == "addx") 2 else 1,
        val remainingCost: Int = startingCost
    ) {
        fun decreasedCost() = copy(remainingCost = remainingCost - 1)

        fun updatedSprite(sprite: IntRange) = if (name == "addx") {
            val valueToIncreaseSpriteBy = instruction.split(" ")[1].toInt()
            IntRange(
                sprite.first + valueToIncreaseSpriteBy,
                sprite.last + valueToIncreaseSpriteBy
            )
        } else {
            sprite
        }
    }

    data class Crt(
        private val size: Pair<Int, Int>,
        private val drawnPixels: List<Pixel> = emptyList()
    ) {
        private val lastDrawnPixel = drawnPixels.lastOrNull()

        fun drawNextPixel(sprite: IntRange): Crt {
            val nextPixelPosition = nextPixelPosition()
            val nextPixelCharacter = if (sprite.contains(nextPixelPosition.first)) '#' else '.'

            return copy(drawnPixels = drawnPixels + Pixel(nextPixelPosition, nextPixelCharacter))
        }

        private fun nextPixelPosition() = when {
            lastDrawnPixel == null -> Pair(0, 0)
            reachedTheEdge(lastDrawnPixel) -> Pair(0, lastDrawnPixel.position.second + 1)
            else -> Pair(lastDrawnPixel.position.first + 1, lastDrawnPixel.position.second)
        }

        private fun reachedTheEdge(lastDrawnPixel: Pixel) =
            size.first - lastDrawnPixel.position.first == 1

        fun displayOutput() = drawnPixels
            .chunked(size.first)
            .joinToString("\n") { row ->
                row.joinToString("") { pixel ->
                    pixel.character.toString()
                }
            }
    }

    data class Pixel(val position: Pair<Int, Int>, val character: Char = ' ')
}
