import org.intellij.lang.annotations.Language
import kotlin.io.path.readText

fun lanternfish(@Language("file-reference") filename: String, days: Int, printState: Boolean = false): Long {
    val input = filename.asPath().readText().split(',').map { it.toInt() }

    val state = Array(9) { index ->
        input.count { it == index }.toLong()
    }

    if (printState)
        println(state.joinToString(","))

    for (day in 1..days) {
        val newBorn = state[0]

        for (index in 1..8) {
            state[index - 1] = state[index]
        }
        state[6] += newBorn
        state[8] = newBorn

        if (printState)
            println(state.joinToString(","))
    }

    return state.sum()
}

private val List<Int>.display
    get() =
        joinToString(",")

fun main() {
    val filename = "Day06.txt"

    fun partOne() =
        lanternfish(filename, 80)

    fun partTwo() =
        lanternfish(filename, 256)

    println("Part One:\t${partOne()}")
    println("Part Two:\t${partTwo()}")
}
