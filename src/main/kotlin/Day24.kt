import Day.Main
import java.util.*
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

fun largestModelNumber(filename: String, verbose: Boolean): Long {
    val lines = filename.asPath().readLines()
    val keys = calculateKeys(lines)
    val output = sortedMapOf<Int, Int>()
    keys.forEach { k, (x, y) ->
        output[k] = min(9, 9 + y)
        output[x] = min(9, 9 - y)
    }
    return output.toLong()
}

fun smallestModelNumber(filename: String, verbose: Boolean): Long {
    val lines = filename.asPath().readLines()
    val keys = calculateKeys(lines)
    val output = sortedMapOf<Int, Int>()
    keys.forEach { k, (x, y) ->
        output[k] = max(1, 1 + y)
        output[x] = max(1, 1 - y)
    }
    return output.toLong()
}

private fun calculateKeys(lines: List<String>): Map<Int, Pair<Int, Int>> {
    val pairs = (0..13).map { i ->
        lines[i * 18 + 5].substring(6).toInt() to lines[i * 18 + 15].substring(6).toInt()
    }
    val stack = mutableListOf<Pair<Int, Int>>()
    val keys = mutableMapOf<Int, Pair<Int, Int>>()
    pairs.forEachIndexed { i, (a, b) ->
        if (a > 0) {
            stack += (i to b)
        } else {
            val (j, bj) = stack.removeLast()
            keys[i] = j to bj + a
        }
    }
    return keys
}

private fun SortedMap<Int, Int>.toLong() =
    values.joinToString("") { it.toString() }.toLong()

class Day24 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        largestModelNumber(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        smallestModelNumber(filename, verbose)

    companion object : Main("Day24.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}