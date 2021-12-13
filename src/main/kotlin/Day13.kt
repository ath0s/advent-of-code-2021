import Day.Main
import kotlin.io.path.readLines

private val foldInstructionPattern = Regex("fold along (\\w)=(\\d+)")

fun foldPaper(filename: String, maxFolds: Int, verbose: Boolean) : Int {
     val lines = filename.asPath().readLines()

    val separatorIndex = lines.indexOfFirst { it.isBlank() }
    val dotCoordinates = lines.take(separatorIndex).map {
        val (x, y) = it.split(",")
        Coordinate(x.toInt(), y.toInt())
    }
    val foldInstructions = lines.drop(separatorIndex).mapNotNull { foldInstructionPattern.matchEntire(it)?.destructured }
        .map { (axis, value) -> FoldInstruction(axis[0], value.toInt()) }

    val matrix = dotCoordinates.toMatrix()



    val folded = foldInstructions.take(maxFolds).fold(matrix) { currentMatrix, foldInstruction ->
        when (foldInstruction.axis) {
            'x' -> currentMatrix.foldX(foldInstruction.value)
            'y' -> currentMatrix.foldY(foldInstruction.value)
            else -> throw IllegalArgumentException("Illegal axis ${foldInstruction.axis}")
        }
    }

    if(verbose) {
        folded.forEach { row ->
                row.forEach { present ->
                    print(if(present) '#' else '.')
                }
                println()
            }
            println()
        }


    return folded.flatten().count { it }
}

private data class FoldInstruction(val axis: Char, val value: Int)

private fun Collection<Coordinate>.toMatrix(): List<List<Boolean>> {
    val maxX = maxOf { it.x }
    val maxY = maxOf { it.y }

    return (0..maxY).map { y ->
        (0..maxX).map { x -> Coordinate(x, y) in this }
    }
}

private fun List<List<Boolean>>.foldX(x: Int): List<List<Boolean>> {
    val (topMatrix, bottomMatrix) = splitX(x)
    return topMatrix.mergeWith(bottomMatrix.map { it.reversed() })
}

private fun List<List<Boolean>>.foldY(y: Int): List<List<Boolean>> {
    val (topMatrix, bottomMatrix) = splitY(y)
    return topMatrix.mergeWith(bottomMatrix.reversed())
}

private fun <T> List<List<T>>.splitX(x: Int) =
    map { it.take(x) } to map { it.drop(x) }


private fun <T> List<List<T>>.splitY(y: Int) =
    take(y) to drop(y)

private fun List<List<Boolean>>.mergeWith(other: List<List<Boolean>>) =
    zip(other) { first, second -> first.zip(second) { one, two -> one || two} }

class Day13 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        foldPaper(filename, 1, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        foldPaper(filename, Int.MAX_VALUE, true)

    companion object : Main("Day13.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}