import AnsiColor.GREEN_BOLD_BRIGHT
import AnsiColor.RED_BOLD_BRIGHT
import AnsiColor.RESET
import Day.Main
import kotlin.io.path.readLines

private val syntaxErrorScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

private val autoCompleteScore = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

fun checkSyntax(filename: String, verbose: Boolean): Int {
    val errors = filename.asPath()
        .readLines()
        .mapNotNull { line ->
            val result = line.parse()
            if (verbose) {
                println(result)
            }
            if (result is CorruptedResult) {
                result.errorChar
            } else {
                null
            }
        }

    return errors.sumOf { syntaxErrorScore[it]!! }
}

fun autoComplete(filename: String, verbose: Boolean): Long {
    val autoCompleteScores = filename.asPath()
        .readLines()
        .mapNotNull { line ->
            val result = line.parse()
            if (verbose) {
                println(result)
            }
            if (result is IncompleteResult) {
                result.autoComplete.fold(0L) { total, char -> (total * 5) + autoCompleteScore[char]!! }
            } else {
                null
            }
        }.sorted()
    return autoCompleteScores[autoCompleteScores.size / 2]
}

private fun String.parse() : ParseResult {
    val expected = mutableListOf<Char>()
    forEachIndexed { index, char ->
        when (char) {
            '(' -> expected += ')'
            '[' -> expected += ']'
            '{' -> expected += '}'
            '<' -> expected += '>'
            ')', ']', '}', '>' -> if (expected.removeLastOrNull() != char) {
                return CorruptedResult(this, index)
            }
        }
    }
    return if(expected.isEmpty()) {
        CompleteResult(this)
    } else {
        IncompleteResult(this, expected.reversed())
    }
}

private sealed interface ParseResult {
    override fun toString(): String
}

private data class CompleteResult(private val line: String) : ParseResult {
    override fun toString() = line
}

private data class CorruptedResult(private val line: String, private val errorIndex: Int) : ParseResult {
    override fun toString() =
        line.mapIndexed { i, c -> if (i == errorIndex) "$RED_BOLD_BRIGHT$c$RESET" else "$c" }.joinToString("")

    val errorChar =
        line[errorIndex]
}

private data class IncompleteResult(private val line: String, val autoComplete: List<Char>) : ParseResult {
    override fun toString() =
        line + autoComplete.joinToString("", GREEN_BOLD_BRIGHT, RESET)
}

class Day10 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        checkSyntax(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        autoComplete(filename, verbose)

    companion object : Main("Day10.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}