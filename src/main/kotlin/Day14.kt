import Day.Main
import kotlin.io.path.readLines

private val rulePattern = Regex("""(\w)(\w)\s*->\s*(\w)""")

fun polymerization(filename: String, steps: Int, verbose: Boolean): Int {
    val lines = filename.asPath().readLines()
    val polymerTemplate = lines.first()

    val pairInsertions = lines.drop(2).associate {
        val (a, b, c) = rulePattern.matchEntire(it)!!.destructured
        (a[0] to b[0]) to "$a$c"
    }

    if (verbose) {
        println("Template:\t$polymerTemplate")
    }

    var result = polymerTemplate
    repeat(steps) {
        result = result.zipWithNext { a, b -> pairInsertions[a to b] }.joinToString("") + result.last()
        if (verbose) {
            println("After step ${it + 1}:\t$result")
        }
    }

    val elementCounts = result.groupingBy { it }.eachCount()
        .toList()
        .sortedByDescending { (_, count) -> count }

    return elementCounts.first().second - elementCounts.last().second
}

class Day14 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        polymerization(filename, 10, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        polymerization(filename, 0, verbose)

    companion object : Main("Day14.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}