import Day.Main

fun lowPointRiskLevels(filename: String, verbose: Boolean): Int =
    filename.parseMatrix().let { heightMap ->
        val lowPoints = heightMap.lowPoints()

        if (verbose) {
            heightMap.print { it in lowPoints }
            println()
        }

        lowPoints.sumOf { heightMap[it] + 1 }
    }

fun threeLargestBasins(filename: String, verbose: Boolean): Int =
    filename.parseMatrix().let { heightMap ->
        val lowPoints = heightMap.lowPoints()
        val basins = lowPoints.map { heightMap.getBasin(it) }

        if (verbose) {
            val allBasinCoordinates = basins.flattenToSet()
            if (verbose) {
                heightMap.print { it in allBasinCoordinates }
                println()
            }
        }
        return basins.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

private fun Matrix.lowPoints() =
    mapIndexedNotNull { coordinate, height ->
        val neighbors = getOrthogonalNeighbors(coordinate)
        if (neighbors.all { get(it) > height }) {
            coordinate
        } else {
            null
        }
    }

private fun Matrix.getBasin(coordinate: Coordinate, visited: MutableSet<Coordinate> = mutableSetOf()): Set<Coordinate> {
    if (get(coordinate) == 9) {
        return emptySet()
    }

    val neighbors = getOrthogonalNeighbors(coordinate).filter { it !in visited }
    visited += coordinate
    visited += neighbors
    return neighbors.flatMapToSet { getBasin(it, visited) } + coordinate
}

class Day09 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        lowPointRiskLevels(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        threeLargestBasins(filename, verbose)

    companion object : Main("Day09.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}