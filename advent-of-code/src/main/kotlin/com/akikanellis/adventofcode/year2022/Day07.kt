package com.akikanellis.adventofcode.year2022

object Day07 {
    private val CD_REGEX = "\\$ cd (.+)".toRegex()
    private val DIR_REGEX = "dir (.+)".toRegex()
    private val FILE_REGEX = "([0-9]+) (.+)".toRegex()

    fun sumOfAllDirectoriesWithTotalSizeLessThan100000(input: String) = rootDirectory(input)
        .allSubdirectories()
        .map { it.size() }
        .filter { it <= 100_000 }
        .sum()

    fun sizeOfDirectoryToDelete(input: String): Int {
        val rootDirectory = rootDirectory(input)

        val unusedSpace = 70_000_000 - rootDirectory.size()
        val extraUnusedSpaceNeeded = 30_000_000 - unusedSpace

        return rootDirectory.allSubdirectories()
            .map { it.size() }
            .sortedDescending()
            .last { it >= extraUnusedSpaceNeeded }
    }

    private fun rootDirectory(input: String): Directory {
        val rootDirectory = Directory(name = "/")

        input
            .lines()
            .fold(rootDirectory) { currentDirectory, terminalOutputLine ->
                if (terminalOutputLine.matches(CD_REGEX)) {
                    val directoryName = CD_REGEX
                        .matchEntire(terminalOutputLine)!!
                        .groupValues[1]

                    return@fold when (directoryName) {
                        "/" -> rootDirectory
                        ".." -> currentDirectory.parent!!
                        else -> currentDirectory.childDirectory(directoryName)
                    }
                } else if (terminalOutputLine.matches(DIR_REGEX)) {
                    val newDirectoryName = DIR_REGEX
                        .matchEntire(terminalOutputLine)!!
                        .groupValues[1]

                    currentDirectory.addChildDirectory(newDirectoryName)
                } else if (terminalOutputLine.matches(FILE_REGEX)) {
                    val newFile = FILE_REGEX
                        .matchEntire(terminalOutputLine)!!
                        .groupValues
                        .let { File(it[2], it[1].toInt()) }

                    currentDirectory.addChildFile(newFile)
                }

                return@fold currentDirectory
            }

        return rootDirectory
    }

    data class Directory(
        val name: String,
        val parent: Directory? = null,
        val childDirectories: MutableList<Directory> = mutableListOf(),
        val childFiles: MutableList<File> = mutableListOf()
    ) {
        fun childDirectory(name: String) = childDirectories.single { it.name == name }

        fun allSubdirectories(): List<Directory> =
            childDirectories + childDirectories.flatMap { it.allSubdirectories() }

        fun size(): Int = childDirectories.sumOf { it.size() } + childFiles.sumOf { it.size }

        fun addChildDirectory(name: String) {
            childDirectories += Directory(name = name, parent = this)
        }

        fun addChildFile(file: File) {
            childFiles += file
        }
    }

    data class File(val name: String, val size: Int)
}
