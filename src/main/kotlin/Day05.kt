import AnsiColor.RESET
import AnsiColor.WHITE_BOLD_BRIGHT
import Day.Main
import kotlin.io.path.readLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val lineFormat = Regex("""(?<x1>\d+),\s*(?<y1>\d+)\s*->\s*(?<x2>\d+),\s*(?<y2>\d+)""")

fun hydroThermalVents(filename: String, includeDiagonal : Boolean = false, verbose: Boolean = false): Int =
    filename.asPath()
        .readLines()
        .let { input ->
            val lines = input.mapNotNull { lineFormat.find(it)?.toLine() }.filter { includeDiagonal || !it.diagonal }

            val maxX = lines.maxOf { it.maxX }
            val maxY = lines.maxOf { it.maxY }

            var count = 0
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    val coordinate = Coordinate(x, y)
                    val linesAtCoordinate = lines.count { it.contains(coordinate) }
                    if (linesAtCoordinate > 1) count++
                    if(verbose) {
                        print(
                            when (linesAtCoordinate) {
                                0 -> "."
                                1 -> "$linesAtCoordinate"
                                else -> "$WHITE_BOLD_BRIGHT$linesAtCoordinate$RESET"
                            }
                        )
                    }
                }
                if(verbose) {
                    println()
                }
            }
            return count
        }


private class Line(
    private val start: Coordinate,
    private val end: Coordinate
) {
    private val x = min(start.x, end.x)..max(start.x, end.x)
    private val y = min(start.y, end.y)..max(start.y, end.y)

    val maxX = x.last
    val maxY = y.last

    val diagonal = start.x != end.x && start.y != end.y

    fun contains(coordinate: Coordinate): Boolean {
        val inRange = coordinate.x in x && coordinate.y in y
        return if (diagonal) {
            val distanceToX = abs(coordinate.x - start.x)
            val distanceToY = abs(coordinate.y - start.y)
            inRange && distanceToX == distanceToY
        } else {
            inRange
        }
    }

    override fun toString() =
        "$start-$end"
}

private fun MatchResult.toLine() =
    destructured.let { (x1, y1, x2, y2) ->
        Line(Coordinate(x1.toInt(), y1.toInt()), Coordinate(x2.toInt(), y2.toInt()))
    }

class Day05 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        hydroThermalVents(filename, verbose = verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        hydroThermalVents(filename, includeDiagonal = true, verbose = verbose)

    companion object : Main("Day05.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}