package com.akikanellis.adventofcode.year2022

import java.lang.Math.floorMod

object Day20 {
    fun sumOfGroveCoordinatesWithWrongDecryptionRoutine(input: String) = sumOfGroveCoordinates(
        input = input,
        decryptionKey = 1,
        mixes = 1,
    )

    fun sumOfGroveCoordinatesWithCorrectDecryptionRoutine(input: String) = sumOfGroveCoordinates(
        input = input,
        decryptionKey = 811_589_153,
        mixes = 10,
    )

    private fun sumOfGroveCoordinates(input: String, decryptionKey: Int, mixes: Int): Long {
        val originalFileNumbers = originalFileNumbers(input, decryptionKey)
        val mixedFileNumbers = originalFileNumbers.toMutableList()
        val fileNumbersSize = originalFileNumbers.size - 1

        repeat(mixes) {
            for (fileNumber in originalFileNumbers) {
                val currentFileNumberIndex = mixedFileNumbers.indexOf(fileNumber)

                val newFileNumberIndex = floorMod(
                    currentFileNumberIndex + fileNumber.value,
                    fileNumbersSize,
                )

                mixedFileNumbers.removeAt(currentFileNumberIndex)
                mixedFileNumbers.add(newFileNumberIndex, fileNumber)
            }
        }

        val zeroFileNumberIndex = mixedFileNumbers
            .withIndex()
            .single { (_, fileNumber) -> fileNumber.value == 0L }
            .index

        return listOf(
            1_000,
            2_000,
            3_000,
        ).sumOf { groveCoordinateOffset ->
            val groveCoordinateFileNumberIndex =
                (zeroFileNumberIndex + groveCoordinateOffset) % mixedFileNumbers.size
            val groveCoordinateFileNumber = mixedFileNumbers[groveCoordinateFileNumberIndex]

            groveCoordinateFileNumber.value
        }
    }

    private fun originalFileNumbers(input: String, decryptionKey: Int) = input.lines()
        .filter { it.isNotBlank() }
        .mapIndexed { index, line -> FileNumber(index, line.toLong() * decryptionKey) }

    private data class FileNumber(val id: Int, val value: Long)
}
