import AnsiColor.RESET
import AnsiColor.WHITE_BOLD_BRIGHT
import Day.Main
import kotlin.io.path.readLines

private val separator = Regex("""\s+""")

private val digits = arrayOf(
    /* 0 */ setOf('a', 'b', 'c', 'e', 'f', 'g'),
    /* 1 */ setOf('c', 'f'),
    /* 2 */ setOf('a', 'c', 'd', 'e', 'g'),
    /* 3 */ setOf('a', 'c', 'd', 'f', 'g'),
    /* 4 */ setOf('b', 'c', 'd', 'f'),
    /* 5 */ setOf('a', 'b', 'd', 'f', 'g'),
    /* 6 */ setOf('a', 'b', 'd', 'e', 'f', 'g'),
    /* 7 */ setOf('a', 'c', 'f'),
    /* 8 */ setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
    /* 9 */ setOf('a', 'b', 'c', 'd', 'f', 'g')
)

private val unambiguousPatterns = listOf(1, 4, 7, 8).map { digits[it] }.associateBy { it.size }

private fun countUniqueMatchesInOutputValues(filename: String, verbose: Boolean): Int =
    filename.parseInput()
        .let { input ->
            val uniqueDigitLengths = unambiguousPatterns.keys

            if (verbose) {
                input.forEach { (signalPatterns, outputValues) ->
                    val highlighted = outputValues.joinToString(" ") {
                        if (it.length in uniqueDigitLengths) {
                            "$WHITE_BOLD_BRIGHT$it$RESET"
                        } else {
                            it
                        }
                    }
                    println("${signalPatterns.joinToString(" ")} | $highlighted")
                }
            }

            val outputValues = input.flatMap { (_, outputValues) -> outputValues }
            return outputValues.count { it.length in uniqueDigitLengths }
        }

private fun resolveOutput(filename: String, verbose: Boolean): Int =
    filename.parseInput().sumOf { (signalPatterns, outputValues) ->

        val one = signalPatterns.single { it.size == digits[1].size }
        val four = signalPatterns.single { it.size == digits[4].size }
        val seven = signalPatterns.single { it.size == digits[7].size }
        val eight = signalPatterns.single { it.size == digits[8].size }

        val six = signalPatterns.filter { it.size == 6 }
            .single {
                (one - it).size == 1
            }
        val zero = signalPatterns.filter { it.size == 6 }
            .filter { it != six }
            .single {
                (four - it).size == 1
            }
        val nine = signalPatterns.filter { it.size == 6 }
            .single { it !in setOf(six, zero) }

        val five = signalPatterns.filter { it.size == 5 }
            .single {
                (six - it).size == 1
            }

        val three = signalPatterns.filter { it.size == 5 }
            .filter { it != five }
            .single {
                (nine - it).size == 1
            }

        val two = signalPatterns.filter { it.size == 5 }
            .single { it !in setOf(five, three) }

        val resolved = arrayOf(
            zero, one, two, three, four, five, six, seven, eight, nine
        )

        outputValues.map { outputValue ->
            resolved.indexOf(outputValue.toSet())
        }.joinToString("").toInt()
    }


private fun String.parseInput() =
    asPath().readLines()
        .let { lines ->
            lines
                .map { it.split(Regex("""\s*\|\s*""")) }
                .map { (signalPatterns, outputValues) -> signalPatterns.split(separator).map{it.toSet()} to outputValues.split(separator) }
        }

class Day08 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        countUniqueMatchesInOutputValues(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        resolveOutput(filename, verbose)

    companion object : Main("Day08.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}