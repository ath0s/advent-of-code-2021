import AnsiColor.RESET
import AnsiColor.WHITE_BOLD_BRIGHT
import Day.Main
import kotlin.io.path.readLines


private fun sevenSegmentSearch(filename: String, digitLengths: Set<Int>, verbose: Boolean): Int =
    filename.asPath()
        .readLines()
        .let { lines ->
            val input = lines
                .map { it.split(Regex("""\s*\|\s*""")) }
                .map { (signalPatterns, outputValues) -> signalPatterns to outputValues.split(Regex("""\s+""")) }

            if(verbose) {
                input.forEach { (signalPatterns, outputValues) ->
                    val highlighted = outputValues.joinToString(" ") {
                        if (it.length in digitLengths) {
                            "$WHITE_BOLD_BRIGHT$it$RESET"
                        } else {
                            it
                        }
                    }
                    println("$signalPatterns | $highlighted")
                }
            }

            val outputValues = input.flatMap { (_, outputValues) -> outputValues }
            return outputValues.count { it.length in digitLengths }
        }

class Day08 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        sevenSegmentSearch(filename, setOf(2, 3, 4, 7), verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        sevenSegmentSearch(filename, setOf(), verbose)

    companion object : Main("Day08.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}