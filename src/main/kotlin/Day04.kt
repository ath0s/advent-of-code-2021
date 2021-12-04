import AnsiColor.RESET
import AnsiColor.WHITE_BOLD_BRIGHT
import org.intellij.lang.annotations.Language
import kotlin.io.path.readLines


fun bingoScore(@Language("file-reference") filename: String): Int =
    filename.asPath()
    .readLines()
    .let { lines ->
        val drawnNumbers = lines.first().split(',').map { it.toInt() }

        val boards = lines.drop(1).fold(mutableListOf<MutableList<String>>()) { accumulator, line ->
            if (line.isBlank()) {
                accumulator.add(mutableListOf())
            } else {
                accumulator.last().add(line)
            }
            accumulator
        }.map {
            Board(it.map { line ->
                line.trim().split(Regex("\\D+")).map { number ->
                    number.toInt()
                }
            })
        }

        drawnNumbers.forEach { drawnNumber ->
            boards.forEach { board -> board.check(drawnNumber) }

            val winner = boards.firstOrNull { it.bingo }
            if (winner != null) {
                println("Winner:")
                println(winner)
                println("Last drawn number: $drawnNumber")
                println("sumUnmarked: ${winner.sumUnmarked}")
                return drawnNumber * winner.sumUnmarked
            }
        }
        return 0
    }


private class Board(numbers: List<List<Int>>) {
    private val rows = numbers.map { row -> row.map { number -> Position(number) } }
    private val cols = rows[0].mapIndexed { index, _ -> rows.map { it[index] } }
    private val all = rows.flatten()

    fun check(number: Int) {
        all.filter { it.number == number }.forEach { it.checked = true }
    }

    val bingo: Boolean
        get() {
            return rows.any { it.allChecked } or cols.any { it.allChecked }
        }

    val sumUnmarked: Int get() =
        all.filter { !it.checked }.sumOf { it.number }

    override fun toString() =
        rows.joinToString("\n") {
            it.joinToString(" ") + " = " + it.filter { pos -> !pos.checked }.sumOf { pos -> pos.number }
        }

    private val List<Position>.allChecked: Boolean
        get() = all { it.checked }

    private class Position(val number: Int, var checked: Boolean = false)    {
        override fun toString() =
            number.toString().padStart(2).let {
                if(checked) {
                    "$WHITE_BOLD_BRIGHT$it$RESET"
                } else {
                    it
                }
            }
    }
}


fun main() {
    val filename = "Day04.txt"

    fun partOne() =
        bingoScore(filename)

    fun partTwo() =
        bingoScore(filename)

    println("Part One:\t${partOne()}")
    //println("Part Two:\t${partTwo()}")
}
