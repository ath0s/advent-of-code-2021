import Day.Main
import kotlin.io.path.useLines

fun numberOfIncreases(filename: String, windowSize: Int = 1): Int =
    filename.asPath()
        .useLines { lines ->
            lines.toInts()
                .sumWindow(windowSize)
                .mapToIsIncreasing()
                .count { it }
        }

private fun Sequence<String>.toInts() =
    map { it.toInt() }

private fun Sequence<Int>.sumWindow(windowSize: Int) =
    if (windowSize == 1) this else windowed(windowSize) { it.sum() }

private fun <T : Comparable<T>> Sequence<T>.mapToIsIncreasing() =
    zipWithNext { first, second -> second > first }

class Day01 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        numberOfIncreases(filename)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        numberOfIncreases(filename, 3)

    companion object : Main("Day01.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}