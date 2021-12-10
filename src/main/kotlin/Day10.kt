import AnsiColor.RED_BOLD_BRIGHT
import AnsiColor.RESET
import Day.Main
import kotlin.io.path.readLines

private val score = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

fun balancedParenthesis(filename: String, verbose: Boolean): Int {
val errors = mutableListOf<Char>()
    filename.asPath()
        .readLines()
        .let { lines ->
            lines.forEach forEachLine@{ line ->
                val expected = mutableListOf<Char>()
                line.forEachIndexed { index, char ->
                    when (char) {
                        '(' -> expected += ')'
                        '[' -> expected += ']'
                        '{' -> expected += '}'
                        '<' -> expected += '>'
                        ')', ']', '}', '>' -> if (expected.removeLastOrNull() != char) {
                            errors += char
                            if (verbose) {
                                line.forEachIndexed { i, c ->
                                    print(if (i == index) "$RED_BOLD_BRIGHT$c$RESET" else c)
                                }
                                println()
                            }
                            return@forEachLine
                        }
                    }
                }
                if (verbose) {
                    println(line)
                }
            }
        }

    return errors.sumOf { score[it]!! }
}

class Day10 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        balancedParenthesis(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        balancedParenthesis(filename, verbose)

    companion object : Main("Day10.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}