import Day.Main
import kotlin.io.path.readLines

fun moveSeaCucumbers(filename: String, verbose: Boolean): Int {
    val board = SeaCucumberBoard(filename.asPath().readLines().map { row ->
        row.map { it.toSeaCucumberType() }
    })

    return generateSequence(board) {
        it.move()
    }.onEachIndexed { index, newBoard ->
        if (verbose) {
            if (index == 0) {
                println("Initial state:")
            } else {
                println("After $index step:")
            }
            println(newBoard)
            println()
        }
    }.count()
}

@Suppress("EnumEntryName")
private enum class SeaCucumberType(val char: Char) {
    east('>'),
    south('v'),
    empty('.')
}

private fun Char.toSeaCucumberType() =
    enumValues<SeaCucumberType>().first { it.char == this }

private class SeaCucumberBoard(
    private val rows: List<List<SeaCucumberType>>
) {

    fun move(): SeaCucumberBoard? {
        val newBoard = rows.map { it.toTypedArray() }.toTypedArray()
        val eastMoves = newBoard.listEastMoves()
        newBoard.applyMoves(eastMoves)
        val southMoves = newBoard.listSouthMoves()
        newBoard.applyMoves(southMoves)

        return if (eastMoves.isEmpty() && southMoves.isEmpty()) {
            null
        } else {
            SeaCucumberBoard(newBoard.map { it.toList() })
        }
    }

    private fun Array<Array<SeaCucumberType>>.listEastMoves(): List<Pair<Coordinate, Coordinate>> =
        flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, seaCucumberType ->
                if (seaCucumberType != SeaCucumberType.east) {
                    return@mapIndexedNotNull null
                }
                val nextX = if (x < row.lastIndex) x + 1 else 0
                val replacement = row[nextX]
                if (replacement == SeaCucumberType.empty) {
                    Coordinate(x, y) to Coordinate(nextX, y)
                } else {
                    null
                }
            }
        }

    private fun Array<Array<SeaCucumberType>>.listSouthMoves(): List<Pair<Coordinate, Coordinate>> =
        flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, seaCucumberType ->
                if (seaCucumberType != SeaCucumberType.south) {
                    return@mapIndexedNotNull null
                }
                val nextY = if (y < this.lastIndex) y + 1 else 0
                val replacement = this[nextY][x]
                if (replacement == SeaCucumberType.empty) {
                    Coordinate(x, y) to Coordinate(x, nextY)
                } else {
                    null
                }
            }
        }

    private fun <T> Array<Array<T>>.applyMoves(moves: List<Pair<Coordinate, Coordinate>>) {
        moves.forEach { (from, to) ->
            val toMove = this[from]
            this[from] = this[to]
            this[to] = toMove
        }
    }

    private operator fun <T> Array<Array<T>>.get(coordinate: Coordinate) =
        this[coordinate.y][coordinate.x]

    private operator fun <T> Array<Array<T>>.set(coordinate: Coordinate, value: T) {
        this[coordinate.y][coordinate.x] = value
    }

    override fun toString() =
        rows.joinToString("\n") { row ->
            String(row.map { it.char }.toCharArray())
        }

}

class Day25 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        moveSeaCucumbers(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        moveSeaCucumbers(filename, verbose)

    companion object : Main("Day25.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}