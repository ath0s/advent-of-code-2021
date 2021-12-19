import Day.Main
import kotlin.io.path.readLines
import kotlin.math.abs

fun numberOfBeacons(filename: String, verbose: Boolean): Int {
    val input = parse(filename.asPath().readLines())

    val matched = alignAllScanners(input)

    val uniquePoints = matched.map { it.absoluteBeacons }
        .reduce { a, b -> a union b }
        .toList()

    return uniquePoints.size
}

fun largestManhattanDistance(filename: String, verbose: Boolean): Int {
    val input = parse(filename.asPath().readLines())

    val matched = alignAllScanners(input)

    val locations = matched.map { it.position}

    return locations.flatMap { first ->
        locations.filterNot { it == first }.map { second ->
            first.manhattanDistance(second)
        }
    }.maxOrNull() ?: 0
}

private fun parse(input: List<String>): List<Scanner> =
    buildList {
        var id = -1
        val beacons = mutableListOf<Point3d>()
        input.forEach { line ->
            if (line.startsWith("---")) {
                if (beacons.isNotEmpty()) {
                    addAll(createOrientations(id, beacons))
                }
                beacons.clear()
                id++
            } else if (id >= 0 && line.isNotEmpty()) {
                val (x, y, z) = line.split(',')
                beacons.add(Point3d(x.toInt(), y.toInt(), z.toInt()))
            }

        }
        if (beacons.isNotEmpty()) {
            addAll(createOrientations(id, beacons))
        }
    }

private fun alignAllScanners(scanners: List<Scanner>): List<Scanner> {
    val unmatched = scanners.groupByTo(mutableMapOf()) { it.id }
    val matched = mutableMapOf(0 to unmatched.remove(0)!!.first())
    val queue = mutableListOf(0)

    while (queue.isNotEmpty() && unmatched.isNotEmpty()) {
        val id = queue.removeFirst()
        val matches = alignAnyScanners(matched[id]!!, unmatched.values)
        matches.forEach {
            matched[it.id] = it
            queue.add(it.id)
            unmatched.remove(it.id)
        }
    }

    return matched.values.toList()
}

private fun alignAnyScanners(target: Scanner, scanners: Iterable<List<Scanner>>) =
    scanners.flatMap { alignAnyOrientation(target, it) }

private fun alignAnyOrientation(target: Scanner, scanners: Iterable<Scanner>) =
    scanners.flatMap { alignSingleOrientation(target, it) }
        .take(1)

private fun alignSingleOrientation(target: Scanner, scanner: Scanner) =
    target.absoluteBeacons.flatMap { t ->
        scanner.relativeBeacons.mapNotNull { s ->
            val offset = t - s
            scanner.copy(position = offset).takeIf { moved ->
                (target.absoluteBeacons intersect moved.absoluteBeacons).size >= 12
            }
        }
    }.take(1)

private data class Point3d(
    val x: Int,
    val y: Int,
    val z: Int
) {

    operator fun minus(other: Point3d) =
        Point3d(x - other.x, y - other.y, z - other.z)

    operator fun plus(other: Point3d) =
        Point3d(x + other.x, y + other.y, z + other.z)

    fun manhattanDistance(other: Point3d) =
        abs(other.x - x) + abs(other.y - y) + abs(other.z - z)

    fun directions() =
        generateSequence(this) { Point3d(it.y, it.z, it.x) }
            .flatMap { sequenceOf(it, Point3d(-it.x, -it.y, it.z)) }
            .take(6)
            .toList()

    fun rotations() =
        generateSequence(this) { Point3d(it.x, -it.z, it.y) }.take(4).toList()

    fun orientations() =
        directions().flatMap { it.rotations() }
}

private data class Scanner(
    val id: Int,
    val relativeBeacons: List<Point3d>,
    val position: Point3d = Point3d(0, 0, 0)
) {
    val absoluteBeacons = relativeBeacons.mapToSet { it + position }
}

private fun createOrientations(id: Int, beacons: List<Point3d>): List<Scanner> =
    beacons.flatMap { it.orientations().withIndex() }
        .groupBy({ (index, _) -> index }, { (_, vector) -> vector })
        .values
        .map { Scanner(id, it) }

class Day19 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        numberOfBeacons(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        largestManhattanDistance(filename, verbose)

    companion object : Main("Day19.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}