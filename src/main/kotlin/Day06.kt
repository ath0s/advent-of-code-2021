import Day.Main
import kotlin.io.path.readText

fun lanternfish(filename: String, days: Int, verbose: Boolean = false): Long {
    val input = filename.asPath().readText().split(',').map { it.toInt() }

    val state = Array(9) { index ->
        input.count { it == index }.toLong()
    }

    if (verbose)
        println(state.display)

    for (day in 1..days) {
        val newBorn = state[0]

        for (index in 1..8) {
            state[index - 1] = state[index]
        }
        state[6] += newBorn
        state[8] = newBorn

        if (verbose)
            println(state.display)
    }

    return state.sum()
}

private val Array<Long>.display
    get() =
        joinToString(",")

class Day06 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        lanternfish(filename, 80, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        lanternfish(filename, 256, verbose)

    companion object : Main("Day06.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}