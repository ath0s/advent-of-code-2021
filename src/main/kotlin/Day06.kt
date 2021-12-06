import org.intellij.lang.annotations.Language
import kotlin.io.path.readText

fun lanternfish(@Language("file-reference") filename: String, printState: Boolean = false): Int {
    val state = filename.asPath().readText().splitToSequence(',').map { it.toInt() }.toMutableList()
    if (printState) {
        println("Initial state:\t${state.display}")
    }

    for (day in 1..80) {
        val newborn = mutableListOf<Int>()
        for (i in state.indices) {
            var age = state[i]
            if (age == 0) {
                newborn += 8
                age = 6
            } else {
                age -= 1
            }
            state[i] = age
        }
        state += newborn
        if (printState) {
            println("After ${day.toString().padStart(2)} days:\t${state.display}")
        }
    }

    return state.size
}

private val List<Int>.display
    get() =
        joinToString(",")

fun main() {
    val filename = "Day06.txt"

    fun partOne() =
        lanternfish(filename)

    fun partTwo() =
        lanternfish(filename)

    println("Part One:\t${partOne()}")
    //println("Part Two:\t${partTwo()}")
}
