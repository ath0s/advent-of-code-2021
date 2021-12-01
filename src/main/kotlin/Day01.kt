import org.intellij.lang.annotations.Language
import kotlin.io.path.useLines

fun numberOfIncreases(@Language("file-reference") filename: String): Int =
    filename.asPath()
        .useLines { lines ->
            lines.toInts()
                .mapToIsIncreasing()
                .count { it }
        }

private fun Sequence<String>.toInts() =
    map { it.toInt() }

private fun <T: Comparable<T>> Sequence<T>.mapToIsIncreasing() =
    windowed(2) { (first, second) -> second > first }

fun main() {
    val result = numberOfIncreases("Day01.txt")
    println(result)
}
