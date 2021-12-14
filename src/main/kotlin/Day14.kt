import Day.Main
import kotlin.io.path.readLines

private val rulePattern = Regex("""(\w)(\w)\s*->\s*(\w)""")

fun polymerization(filename: String, steps: Int, verbose: Boolean): Long {
    val lines = filename.asPath().readLines()
    val polymerTemplate = lines.first()

    val pairInsertions = lines.drop(2).associate {
        val (a, b, c) = rulePattern.matchEntire(it)!!.destructured
        (a.single() to b.single()) to c.single()
    }

    if (verbose) {
        println("Template:\t$polymerTemplate")
    }

    var pairCounts = buildMap {
        polymerTemplate.zipWithNext().forEach {
            add(it, 1)
        }
    }

    repeat(steps) {
        pairCounts = pairCounts.applyInsertions(pairInsertions)
    }

    val elementCounts = pairCounts.countElements(polymerTemplate.first()).values.sorted()

    return elementCounts.last() -elementCounts.first()
}

private fun Map<Pair<Char, Char>, Long>.applyInsertions(pairInsertions: Map<Pair<Char, Char>, Char>) =
    let { source ->
        buildMap {
            source.forEach { (pair, count) ->
                val (a, b) = pair
                val c = pairInsertions[pair]!!
                add(a to c, count)
                add(c to b, count)
            }
        }
    }

private fun Map<Pair<Char, Char>, Long>.countElements(firstChar: Char) =
    (map { (pair, count) -> pair.second to count} + (firstChar to 1L))
        .groupBy ({(char, _) -> char}, { (_, count) -> count})
        .mapValues { (_, counts) -> counts.sum() }

private fun <K> MutableMap<K, Long>.add(key: K, value: Long) =
    compute(key) { _, previousValue -> (previousValue ?: 0) + value }

class Day14 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        polymerization(filename, 10, true)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        polymerization(filename, 40, verbose)

    companion object : Main("Day14.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}