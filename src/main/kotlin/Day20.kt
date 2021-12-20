import Day.Main
import kotlin.io.path.readLines

private val pixelToBit = mapOf(
    '#' to "1",
    '.' to "0"
)

fun enhanceImage(filename: String, times: Int, verbose: Boolean): Int {
    val lines = filename.asPath().readLines()
    val imageEnhancementAlgorithm = lines.first().toCharArray()

    var matrix = InfiniteMatrix(lines.drop(2).map { it.toList() })

    if (verbose) {
        println("Start")
        println(matrix)
        println()
    }

    repeat(times) {
        matrix = matrix.enhance(imageEnhancementAlgorithm)
        if (verbose) {
            println("After ${it + 1} enhance:")
            println(matrix)
            println()
        }
    }

    return matrix.count { it == '#' }
}

private class InfiniteMatrix(
    private val rows: List<List<Char>>,
    private val infinitePixel: Char = '.'
) {
    fun enhance(imageEnhancementAlgorithm: CharArray): InfiniteMatrix {
        val maxY = rows.lastIndex + 1
        val maxX = rows.maxOf { it.size }
        val newRows = buildList(maxY + 1) {
            (-1..maxY).forEach { y ->
                add(buildList(maxX + 1) {
                    (-1..maxX).forEach { x ->
                        val lookupIndex = charArrayOf(
                            rows[y - 1, x - 1], rows[y - 1, x], rows[y - 1, x + 1],
                            rows[y + 0, x - 1], rows[y + 0, x], rows[y + 0, x + 1],
                            rows[y + 1, x - 1], rows[y + 1, x], rows[y + 1, x + 1],
                        ).toLookupIndex()
                        add(imageEnhancementAlgorithm[lookupIndex])
                    }
                })
            }
        }
        val newInfinitePixel = imageEnhancementAlgorithm[CharArray(9).apply { fill(infinitePixel) }.toLookupIndex()]
        return InfiniteMatrix(newRows, newInfinitePixel)
    }

    override fun toString() =
        rows.joinToString("\n") { it.joinToString("") }

    fun count(predicate: (Char) -> Boolean) =
        rows.flatten().count(predicate)

    private fun CharArray.toLookupIndex() =
        joinToString("") { pixelToBit[it]!! }.toInt(2)

    private operator fun List<List<Char>>.get(y: Int, x: Int): Char =
        rows.getOrNull(y)?.getOrNull(x) ?: infinitePixel
}

class Day20 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        enhanceImage(filename, 2, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        enhanceImage(filename, 50, verbose)

    companion object : Main("Day20.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}