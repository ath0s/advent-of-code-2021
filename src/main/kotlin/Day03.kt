import org.intellij.lang.annotations.Language
import kotlin.io.path.readLines

fun powerConsumption(@Language("file-reference") filename: String): Int {
    val lines = filename.asPath().readLines()
    val bitCounts = lines.toBitCounts()

    val gamma = bitCounts
        .mapNotNull { it.maxByOrNull { (_, count) -> count }?.key }
        .asString()

    val epsilon = gamma
        .map { if (it == '0') '1' else '0' }
        .asString()

    println("gamma\t$gamma")
    println("epsilon\t$epsilon")

    return gamma.toInt(2) * epsilon.toInt(2)
}

fun lifeSupportRating(@Language("file-reference") filename: String): Int {
    val lines = filename.asPath().readLines()
    val oxygenGeneratorRating = oxygenGeneratorRating(lines, 0)
    val co2ScrubberRating = co2ScrubberRating(lines, 0)

    println("oxygenGeneratorRating\t$oxygenGeneratorRating")
    println("co2ScrubberRating\t$co2ScrubberRating")

    return oxygenGeneratorRating.toInt(2) * co2ScrubberRating.toInt(2)
}

private fun oxygenGeneratorRating(lines: List<String>, index: Int) : String {
    val bitToKeep = lines.toBitCounts()[index].maxOrDefault('1')

    val linesToKeep = lines.filter { it[index] == bitToKeep }
    println("oxygenGeneratorRating (index=$index,char=$bitToKeep)\t$linesToKeep")
    return linesToKeep.singleOrNull() ?: oxygenGeneratorRating(linesToKeep, index + 1)
}

private fun co2ScrubberRating(lines: List<String>, index: Int) :  String {
    val bitToKeep = lines.toBitCounts()[index].minOrDefault('0')
    val linesToKeep = lines.filter { it[index] == bitToKeep }
    println("co2ScrubberRating(index=$index,char=$bitToKeep)\t$linesToKeep")
    return linesToKeep.singleOrNull() ?: co2ScrubberRating(linesToKeep, index + 1)
}

private fun Iterable<String>.toBitCounts(): List<Map<Char, Int>> =
    flatMap { it.mapIndexed { index, c -> (index to c) }}
        .groupBy({ (index, _) -> index }, { (_, c) -> c })
        .mapValues { (_, list) -> list.groupBy { it }.mapValues { (_, chars) -> chars.size } }
        .let { it.toList().sortedBy { (index, _) -> index }.map { (_, chars) -> chars } }

private fun List<Char>.asString() =
    String(toCharArray())

private fun Map<Char, Int>.maxOrDefault(defaultValue: Char): Char {
    val zeroCount = getOrDefault('0', 0)
    val oneCount = getOrDefault('1', 0)
    return when {
        zeroCount > oneCount -> '0'
        oneCount > zeroCount -> '1'
        else -> defaultValue
    }
}

private fun Map<Char, Int>.minOrDefault(defaultValue: Char): Char {
    val zeroCount = getOrDefault('0', 0)
    val oneCount = getOrDefault('1', 0)
    return when {
        zeroCount < oneCount -> '0'
        oneCount < zeroCount -> '1'
        else -> defaultValue
    }
}

fun main() {
    val filename = "Day03.txt"

    fun partOne() =
        powerConsumption(filename)

    fun partTwo() =
        lifeSupportRating(filename)

    println("Part One:\t${partOne()}")
    println("Part Two:\t${partTwo()}")

}