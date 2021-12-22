import Day.Main
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

private val rulePattern = Regex("""(on|off) x=(.+)\.\.(.+),y=(.+)\.\.(.+),z=(.+)\.\.(.+)""")

fun countCubes(filename: String, verbose: Boolean): Long {
    val rules = filename.asPath().readLines().map { it.toCubicle() }
        .filter { it.x.first >= -50 && it.x.last <= 50 }
        .filter { it.y.first >= -50 && it.y.last <= 50 }
        .filter { it.z.first >= -50 && it.z.last <= 50 }

    return rules.countEnabledCubicles()
}

fun countAllCubes(filename: String, verbose: Boolean): Long {
    val rules = filename.asPath().readLines().map { it.toCubicle() }

    return rules.countEnabledCubicles()
}

private fun Iterable<Cubicle>.countEnabledCubicles(): Long {
    val cubicles = mutableListOf<Cubicle>()

    forEach { rule ->
        val newCubicles = mutableListOf<Cubicle>()
        cubicles.forEach { cubicle ->
            rule.invertedOverlappingCubicle(cubicle)?.also {
                newCubicles += it
            }
        }
        cubicles += newCubicles
        if (rule.on) {
            cubicles += rule
        }
    }

    return cubicles.fold(0L) { sum, it ->
        sum + it.volume * it.polarityAsLong
    }
}

private data class Cubicle(
    val on: Boolean,
    val x: LongRange,
    val y: LongRange,
    val z: LongRange
) {

    val volume =
        (x.last - x.first + 1) * (y.last - y.first + 1) * (z.last - z.first + 1)

    val polarityAsLong =
        if (on) 1L else -1L

    fun invertedOverlappingCubicle(other: Cubicle): Cubicle? {
        val minX = max(x.first, other.x.first)
        val maxX = min(x.last, other.x.last)
        val minY = max(y.first, other.y.first)
        val maxY = min(y.last, other.y.last)
        val minZ = max(z.first, other.z.first)
        val maxZ = min(z.last, other.z.last)
        return if (minX <= maxX && minY <= maxY && minZ <= maxZ) {
            Cubicle(!other.on, minX..maxX, minY..maxY, minZ..maxZ)
        } else {
            null
        }
    }

    override fun toString() =
        "${if (on) "on" else "off"} x=$x,y=$y,z=$z"

}

private fun String.toCubicle(): Cubicle {
    val (on, xFrom, xTo, yFrom, yTo, zFrom, zTo) = rulePattern.matchEntire(this)!!.destructured
    return Cubicle(on == "on", (xFrom.toLong()..xTo.toLong()), (yFrom.toLong()..yTo.toLong()), (zFrom.toLong()..zTo.toLong()))
}

class Day22 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        countCubes(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        countAllCubes(filename, verbose)

    companion object : Main("Day22.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}