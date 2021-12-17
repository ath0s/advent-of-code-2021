import Day.Main
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.min

private val targetAreaPattern = Regex("""target area:\s*x=(-?\d+)\.\.(-?\d+),\s*y=(-?\d+)\.\.(-?\d+)""")

fun highestYPosition(filename: String, verbose: Boolean): Int {
    val targetArea = filename.parseTargetArea()


    val paths = targetArea.calculatePaths()
    val path = paths.maxByOrNull { path -> path.maxOf { it.y } }!!

    if (verbose) {
        val minX = 0
        val maxX = targetArea.maxX
        val maxY = path.maxOf { it.y }
        val minY = targetArea.minY

        println("minX=$minX,maxX=$maxX,maxY=$maxY,minY=$minY")
        (maxY downTo minY).forEach { y ->
            (minX..maxX).forEach { x ->
                when (Coordinate(x, y)) {
                    Coordinate(0, 0) -> print('S')
                    in path -> print('#')
                    in targetArea -> print('T')
                    else -> print('.')
                }
            }
            println()
        }
    }

    return path.maxOf { it.y }
}

private fun String.parseTargetArea(): Pair<Coordinate, Coordinate> {
    val matchResult = targetAreaPattern.find(asPath().readText())
    val (xFrom, xTo, yFrom, yTo) = matchResult!!.destructured
    return Coordinate(xFrom.toInt(), yFrom.toInt()) to Coordinate(xTo.toInt(), yTo.toInt())
}

private fun Pair<Coordinate, Coordinate>.calculatePaths(start: Coordinate = Coordinate(0,0)) =
    (1..maxX).flatMap { x ->
        (1_000 downTo minY).map { y ->
            Coordinate(x, y)
        }
    }.mapNotNull {
        calculatePath(start, it, this)
    }

private fun calculatePath(coordinate: Coordinate, velocity: Coordinate, targetArea: Pair<Coordinate, Coordinate>) : List<Coordinate>? {
    return when {
        coordinate in targetArea -> listOf(coordinate)
        coordinate > targetArea -> null
        else -> {
            val nextCoordinate = Coordinate(coordinate.x + velocity.x, coordinate.y + velocity.y)
            val nextVelocity = Coordinate(velocity.x + (if(velocity.x > 0) -1 else 1), velocity.y - 1)

            val remainingPath = calculatePath(nextCoordinate, nextVelocity, targetArea)
            if(remainingPath == null) {
                null
            } else {
                listOf(coordinate) + remainingPath
            }
        }
    }
}

private operator fun Pair<Coordinate, Coordinate>.contains(coordinate: Coordinate) : Boolean =
    coordinate.x in minX..maxX &&
            coordinate.y in minY..maxY

private operator fun Coordinate.compareTo(targetArea: Pair<Coordinate, Coordinate>): Int = when {
    (x > targetArea.maxX || y < targetArea.minY) -> 1
    (x < targetArea.minX || y > targetArea.maxY) -> -1
    else -> 0
}

private val Pair<Coordinate, Coordinate>.minX
    get() = min(first.x, second.x)

private val Pair<Coordinate, Coordinate>.maxX
    get() = max(first.x, second.x)

private val Pair<Coordinate, Coordinate>.minY
    get() = min(first.y, second.y)

private val Pair<Coordinate, Coordinate>.maxY
    get() = max(first.y, second.y)


class Day17 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        highestYPosition(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        highestYPosition(filename, verbose)

    companion object : Main("Day17.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}