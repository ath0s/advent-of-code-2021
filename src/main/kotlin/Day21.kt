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

fun diracDice(filename: String, verbose: Boolean): Long {
    val (player1, player2) = filename.asPath().readLines().map { it.toPlayerSnapshot() }
    val cache = mutableMapOf<Pair<PlayerSnapshot, PlayerSnapshot>, Pair<Long, Long>>()
    val (player1Wins, player2Wins) = diracDice(player1, player2, cache)
    return maxOf(player1Wins, player2Wins)
}

private fun diracDice(playerA: PlayerSnapshot, playerB: PlayerSnapshot, cache: MutableMap<Pair<PlayerSnapshot, PlayerSnapshot>, Pair<Long, Long>>): Pair<Long, Long> {
    if (playerA.score >= 21) {
        return 1L to 0L
    }
    if (playerB.score >= 21) {
        return 0L to 1L
    }
    val cacheKey = playerA to playerB
    cache[cacheKey]?.let {
        return it
    }
    var wins = 0L to 0L

    (1..3).forEach { roll1 ->
        (1..3).forEach { roll2 ->
            (1..3).forEach { roll3 ->
                val newPosition = (playerA.position + roll1 + roll2 + roll3) % 10
                val newPlayerA = PlayerSnapshot(newPosition, (playerA.score + newPosition) + 1)

                val newWins = diracDice(playerB, newPlayerA, cache)

                wins = wins.copy(
                    wins.first + newWins.second,
                    wins.second + newWins.first
                )
            }
        }
    }

    cache[cacheKey] = wins
    return wins
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

private data class PlayerSnapshot(
    val position: Int,
    val score: Int = 0
)

private fun String.toPlayerSnapshot() =
    playerPattern.matchEntire(this)!!.destructured.let { (_, position) ->
        PlayerSnapshot(position.toInt() - 1)
    }


class Day21 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        deterministicDice(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        diracDice(filename, verbose)

    companion object : Main("Day21.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}