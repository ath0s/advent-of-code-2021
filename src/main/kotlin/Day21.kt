import Day.Main
import kotlin.io.path.readLines

private val playerPattern = Regex("""Player (.+) starting position: (\d+)""")

fun deterministicDice(filename: String, verbose: Boolean): Int {
    val players = filename.asPath().readLines().map { it.toPlayer() }
    var die = -1
    var rolls = 0

    outer@ do {
        for (it in players) {
            if (verbose) {
                print("Player ${it.name} rolls ")
            }
            var steps = 0
            repeat(3) {
                rolls++
                die++
                val step = (die % 100) + 1
                if (verbose) {
                    print("$step ")
                }
                steps += step
            }
            it.position += steps
            it.score += (it.position % 10) + 1
            if (verbose) {
                println("and moves to space ${(it.position % 10) + 1} for a total score of ${it.score}")
            }
            if (it.score >= 1_000) {
                break@outer
            }
        }

    } while (players.all { it.score < 1_000 })

    if (verbose) {
        players.forEach {
            println("Player ${it.name}, position=${(it.position % 10) + 1}, score=${it.score}")
        }
        println("Die rolls $rolls")
    }

    return players.minOfOrNull { it.score }!! * rolls
}

private class Player(
    val name: String,
    var position: Int,
    var score: Int = 0
)

private fun String.toPlayer() =
    playerPattern.matchEntire(this)!!.destructured.let { (name, position) ->
        Player(name, position.toInt() - 1)
    }

class Day21 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        deterministicDice(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        deterministicDice(filename, verbose)

    companion object : Main("Day21.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}