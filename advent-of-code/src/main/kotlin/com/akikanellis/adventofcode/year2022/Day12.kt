package com.akikanellis.adventofcode.year2022

object Day12 {
    fun shortestPathSteps(input: String, ascending: Boolean): Int {
        val squares = squares(input)

        val startSquare = squares.single { if (ascending) it.startSquare else it.endSquare }

        val shortestPath =
            shortestPath(
                ascending = ascending,
                startSquare = startSquare
            ) { square -> if (ascending) square.endSquare else square.lowestElevation }

        return shortestPath.size - 1
    }

    private fun squares(input: String): List<Square> {
        val squaresGrid = input
            .lines()
            .filter { it.isNotBlank() }
            .mapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, heightmapCharacter ->
                    Square(
                        x = columnIndex,
                        y = rowIndex,
                        heightmapCharacter = heightmapCharacter
                    )
                }
            }

        val squares = squaresGrid.flatten()

        for (square in squares) {
            square.addNeighbours(
                listOfNotNull(
                    squaresGrid[square.y].elementAtOrNull(square.x + 1),
                    squaresGrid[square.y].elementAtOrNull(square.x - 1),
                    squaresGrid.elementAtOrNull(square.y + 1)?.get(square.x),
                    squaresGrid.elementAtOrNull(square.y - 1)?.get(square.x)
                )
            )
        }

        return squares
    }

    private fun shortestPath(
        ascending: Boolean,
        startSquare: Square,
        endSquareFound: (Square) -> Boolean
    ): List<Square> {
        val endSquare = endSquare(ascending, startSquare, endSquareFound)

        val shortestPath = mutableListOf<Square>()
        var nextParentSquare: Square? = endSquare

        while (nextParentSquare != null) {
            shortestPath.add(0, nextParentSquare)
            nextParentSquare = nextParentSquare.parent
        }

        return shortestPath
    }

    private fun endSquare(
        ascending: Boolean,
        startSquare: Square,
        endSquareFound: (Square) -> Boolean
    ): Square {
        val squaresToVisit = mutableListOf(startSquare)

        while (squaresToVisit.isNotEmpty()) {
            val square = squaresToVisit.removeFirst()
            square.visit()

            if (endSquareFound(square)) return square

            square.accessibleNeighbours(ascending)
                .filterNot { it.visited }
                .forEach { neighbour ->
                    neighbour.visit()
                    neighbour.parent = square
                    squaresToVisit += neighbour
                }
        }

        error("Could not find end square")
    }

    private data class Square(
        val x: Int,
        val y: Int,
        val heightmapCharacter: Char,
        var visited: Boolean = false,
        var parent: Square? = null
    ) {
        val neighbours = mutableListOf<Square>()
        val elevation = when (heightmapCharacter) {
            'S' -> 'a'
            'E' -> 'z'
            else -> heightmapCharacter
        }

        val startSquare = heightmapCharacter == 'S'
        val endSquare = heightmapCharacter == 'E'
        val lowestElevation = elevation == 'a'

        fun visit() {
            visited = true
        }

        fun accessibleNeighbours(ascending: Boolean) = neighbours
            .filter { neighbour ->
                if (ascending) {
                    neighbour.elevation - elevation <= 1
                } else {
                    elevation - neighbour.elevation <= 1
                }
            }

        fun addNeighbours(neighbours: List<Square>) {
            this.neighbours += neighbours
        }
    }
}
