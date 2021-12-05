import AnsiColor.RESET
import AnsiColor.WHITE_BOLD_BRIGHT
import org.intellij.lang.annotations.Language
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

private val lineFormat = Regex("""(?<x1>\d+),\s*(?<y1>\d+)\s*->\s*(?<x2>\d+),\s*(?<y2>\d+)""")

fun hydroThermalVents(@Language("file-reference") filename: String): Int =
    filename.asPath()
        .readLines()
        .let { input ->
            val lines = input.mapNotNull { lineFormat.find(it)?.toLine() }
            val maxX = lines.maxOf { it.maxX }
            val maxY = lines.maxOf { it.maxY }

            var count = 0
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    val coordinate = Coordinate(x, y)
                    val linesAtCoordinate = lines.count { it.contains(coordinate) }
                    if(linesAtCoordinate > 1) count++
                    print(
                        when (linesAtCoordinate) {
                            0 -> "."
                            1 -> "$linesAtCoordinate"
                            else -> "$WHITE_BOLD_BRIGHT$linesAtCoordinate$RESET"
                        }
                    )
                }
                println()
            }
            return count
        }


private class Coordinate(
    val x: Int,
    val y: Int
) :Comparable<Coordinate>  {

    override fun compareTo(other: Coordinate) =
        compareValuesBy(this, other, { x }, { y })

    override fun toString() = "$x,$y"

}

private interface Line {
    val maxX: Int
    val maxY: Int
    fun contains(coordinate: Coordinate): Boolean
    override fun toString(): String
}

private class HorizontalLine(
    private val x: IntRange,
    private val y: Int
) : Line {
    override val maxX = x.last
    override val maxY = y

    override fun contains(coordinate: Coordinate) =
        coordinate.y == y && x.contains(coordinate.x)

    override fun toString() =
      "${Coordinate(x.first,y)}-${Coordinate(x.last,y)}"

}

private class VerticalLine(
    private val x: Int,
    private val y: IntRange
) : Line {
    override val maxX = x
    override val maxY = y.last

    override fun contains(coordinate: Coordinate) =
        coordinate.x == x && y.contains(coordinate.y)

    override fun toString() =
          "${Coordinate(x,y.first)}-${Coordinate(x,y.last)}"

}

private fun MatchResult.toLine() =
    destructured.let { (x1, y1, x2, y2) ->
        arrayOf(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
    }.let { (x1, y1, x2, y2) ->
        when {
            x1 == x2 -> VerticalLine(x1, min(y1, y2)..max(y1, y2))
            y1 == y2 -> HorizontalLine(min(x1, x2)..max(x1, x2), y1)
            else -> null
        }
    }

fun main() {
    val filename = "Day05.txt"

    fun partOne() =
        hydroThermalVents(filename)

    fun partTwo() =
        hydroThermalVents(filename)

    println("Part One:\t${partOne()}")
    //println("Part Two:\t${partTwo()}")
}