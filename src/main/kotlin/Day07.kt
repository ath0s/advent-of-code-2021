import org.intellij.lang.annotations.Language
import kotlin.io.path.readLines
import kotlin.math.abs

fun crabs(@Language("file-reference") filename: String): Int =
    filename.asPath()
        .readLines()
        .let { lines ->
            val positions = lines[0].split(",").map { it.toInt() }
            val min = positions.minOrNull()!!
            val max = positions.maxOrNull()!!
            val (position, moves) = (min..max).fold(-1 to Integer.MAX_VALUE) { (bestPosition, bestMoves), currentPosition ->
                val currentMoves = calculateMovesIfLessThan(positions, currentPosition, bestMoves)
                if (currentMoves != null) {
                    currentPosition to currentMoves
                } else {
                    bestPosition to bestMoves
                }
            }
            println("Best position:\t$position")
            println("Moves:\t$moves")

            return moves
        }

private fun calculateMovesIfLessThan(positions: Iterable<Int>, currentPosition: Int, bestMoves: Int): Int? {
    var sum = 0
    for (position in positions) {
        sum += abs(currentPosition - position)
        if (sum >= bestMoves) {
            return null
        }
    }
    return sum
}

fun main() {
    val filename = "Day07.txt"

    fun partOne() =
        crabs(filename)

    fun partTwo() =
        crabs(filename)

    println("Part One:\t${partOne()}")
    //println("Part Two:\t${partTwo()}")
}
