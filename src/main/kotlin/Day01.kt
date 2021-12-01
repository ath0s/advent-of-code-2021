import org.intellij.lang.annotations.Language
import kotlin.io.path.useLines

fun numberOfIncreases(@Language("file-reference") filename: String, windowSize: Int = 1): Int =
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
    windowed(windowSize) { it.sum() }

private fun <T : Comparable<T>> Sequence<T>.mapToIsIncreasing() =
    windowed(2) { (first, second) -> second > first }

fun main() {
    val filename = "Day01.txt"

    fun partOne() =
        numberOfIncreases(filename)

    fun partTwo() =
        numberOfIncreases(filename, 3)

    println("Part One:\t${partOne()}")
    println("Part Two:\t${partTwo()}")
}
