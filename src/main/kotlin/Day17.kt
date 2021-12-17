import Day.Main
import kotlin.io.path.readText
import kotlin.math.max
import kotlin.math.min

private val targetAreaPattern = Regex("""target area:\s*x=(-?\d+)\.\.(-?\d+),\s*y=(-?\d+)\.\.(-?\d+)""")

fun highestYPosition(filename: String, verbose: Boolean): Int {
    val targetArea = filename.parseTargetArea()

    val paths = targetArea.calculatePaths().map { (_, path) -> path }
    val path = paths.maxByOrNull { path -> path.maxOf { it.y } }!!

    if (verbose) {
        val minX = 0
        val maxX = targetArea.maxX
        val maxY = path.maxOf { it.y }
        val minY = targetArea.minY

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

private fun numberOfUniqueInitialVelocityValues(filename: String, verbose: Boolean): Int {
    val targetArea = filename.parseTargetArea()

    val uniqueVelocityValues = targetArea.calculatePaths().map { (velocity, _) -> velocity }

    if (verbose) {
        uniqueVelocityValues.chunked(9).forEach { line ->
            line.forEach { velocityValue ->
                print("$velocityValue".padEnd(8))
            }
            println()
        }
    }

    return uniqueVelocityValues.size
}

private fun String.parseTargetArea(): Pair<Coordinate, Coordinate> {
    val matchResult = targetAreaPattern.find(asPath().readText())
    val (xFrom, xTo, yFrom, yTo) = matchResult!!.destructured
    return Coordinate(xFrom.toInt(), yFrom.toInt()) to Coordinate(xTo.toInt(), yTo.toInt())
}

private fun Pair<Coordinate, Coordinate>.calculatePaths(start: Coordinate = Coordinate(0, 0)) =
    (1..maxX).flatMap { x ->
        (1_000 downTo minY).map { y ->
            Coordinate(x, y)
        }
    }.mapNotNull { velocity ->
        val path = calculatePath(start, velocity, this)
        if (path.last() !in this) {
            null
        } else {
            velocity to path
        }
    }

private fun calculatePath(start: Coordinate, velocity: Coordinate, targetArea: Pair<Coordinate, Coordinate>): List<Coordinate> =
    generateSequence(start to velocity) { (currentCoordinate, currentVelocity) ->
        if(currentCoordinate < targetArea) {
            val nextCoordinate = Coordinate(currentCoordinate.x + currentVelocity.x, currentCoordinate.y + currentVelocity.y)
            val nextVelocity = Coordinate(
                currentVelocity.x + when {
                    currentVelocity.x < 0 -> 1
                    currentVelocity.x > 0 -> -1
                    else -> 0
                }, currentVelocity.y - 1
            )
            nextCoordinate to nextVelocity
        } else {
            null
        }
    }.map { (coordinate, _) -> coordinate }
        .toList()

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
        numberOfUniqueInitialVelocityValues(filename, verbose)

    companion object : Main("Day17.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}