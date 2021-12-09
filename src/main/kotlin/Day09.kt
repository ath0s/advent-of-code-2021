import AnsiColor.RESET
import AnsiColor.WHITE_BOLD_BRIGHT
import Day.Main
import kotlin.io.path.readLines

fun lowPointRiskLevels(filename: String, verbose: Boolean): Int =
    filename.parseHeightMap().let { heightMap ->
        val lowPoints = heightMap.lowPoints()

        if (verbose) {
            heightMap.print { it in lowPoints }
        }

        lowPoints.sumOf { heightMap[it] + 1 }
    }

fun threeLargestBasins(filename: String, verbose: Boolean): Int =
    filename.parseHeightMap().let { heightMap ->
        val lowPoints = heightMap.lowPoints()
        val basins = lowPoints.map { heightMap.getBasin(it) }

        if (verbose) {
            val allBasinCoordinates = basins.flattenToSet()
            if (verbose) {
                heightMap.print { it in allBasinCoordinates }
            }
        }
        return basins.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

private fun String.parseHeightMap() =
    asPath()
        .readLines()
        .map { line -> line.map { char -> char.digitToInt() } }

private fun List<List<Int>>.lowPoints() =
    flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, height ->
            val coordinate = Coordinate(x, y)
            val neighbors = getNeighbors(coordinate)
            if (neighbors.all { get(it) > height }) {
                coordinate
            } else {
                null
            }
        }
    }

private fun List<List<Int>>.getNeighbors(coordinate: Coordinate) =
    listOf(
        Coordinate(coordinate.x, coordinate.y - 1),
        Coordinate(coordinate.x - 1, coordinate.y),
        Coordinate(coordinate.x + 1, coordinate.y),
        Coordinate(coordinate.x, coordinate.y + 1)
    ).filter {it in this }

private fun List<List<Int>>.getBasin(coordinate: Coordinate, visited: MutableSet<Coordinate> = mutableSetOf()): Set<Coordinate> {
    if (get(coordinate) == 9) {
        return emptySet()
    }

    val neighbors = getNeighbors(coordinate).filter { it !in visited }
    visited += coordinate
    visited += neighbors
    return neighbors.flatMapToSet { getBasin(it, visited) } + coordinate
}

private fun List<List<Int>>.print(highlight: (Coordinate) -> Boolean) {
    forEachIndexed { y, row ->
        row.forEachIndexed { x, height ->
            if (highlight(Coordinate(x, y))) {
                print("$WHITE_BOLD_BRIGHT$height$RESET")
            } else {
                print("$height")
            }
        }
        println()
    }
    println()
}

private operator fun List<List<Int>>.get(coordinate: Coordinate) =
    this[coordinate.y][coordinate.x]

private operator fun List<List<Int>>.contains(coordinate: Coordinate) =
    coordinate.y in (0..lastIndex) && coordinate.x in (0..get(coordinate.y).lastIndex)

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