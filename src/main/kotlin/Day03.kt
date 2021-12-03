import Bit.*
import org.intellij.lang.annotations.Language

fun powerConsumption(@Language("file-reference") filename: String): Int {
    val lines = filename.asPath().readBinaries()
    val bitCounts = lines.toBitCounts()

    val gamma = bitCounts
        .mapNotNull { it.maxByOrNull { (_, count) -> count }?.key }
        .toBinary()

    val epsilon = !gamma

    println("gamma\t$gamma")
    println("epsilon\t$epsilon")

    return gamma.toInt() * epsilon.toInt()
}

fun lifeSupportRating(@Language("file-reference") filename: String): Int {
    val lines = filename.asPath().readBinaries()
    val oxygenGeneratorRating = oxygenGeneratorRating(lines, 0)
    val co2ScrubberRating = co2ScrubberRating(lines, 0)

    println("oxygenGeneratorRating\t$oxygenGeneratorRating")
    println("co2ScrubberRating\t$co2ScrubberRating")

    return oxygenGeneratorRating.toInt() * co2ScrubberRating.toInt()
}

private fun oxygenGeneratorRating(lines: List<Binary>, index: Int): Binary {
    val bitToKeep = lines.toBitCounts()[index].maxOrDefault(`1`)

    val linesToKeep = lines.filter { it[index] == bitToKeep }
    println("oxygenGeneratorRating (index=$index,char=$bitToKeep)\t$linesToKeep")
    return linesToKeep.singleOrNull() ?: oxygenGeneratorRating(linesToKeep, index + 1)
}

private fun co2ScrubberRating(lines: List<Binary>, index: Int): Binary {
    val bitToKeep = lines.toBitCounts()[index].minOrDefault(`0`)
    val linesToKeep = lines.filter { it[index] == bitToKeep }
    println("co2ScrubberRating(index=$index,char=$bitToKeep)\t$linesToKeep")
    return linesToKeep.singleOrNull() ?: co2ScrubberRating(linesToKeep, index + 1)
}

private fun Iterable<Binary>.toBitCounts(): List<Map<Bit, Int>> =
    flatMap { it.mapIndexed { index, c -> (index to c) } }
        .groupBy({ (index, _) -> index }, { (_, c) -> c })
        .mapValues { (_, list) -> list.groupBy { it }.mapValues { (_, bits) -> bits.size } }
        .let { it.toList().sortedBy { (index, _) -> index }.map { (_, bits) -> bits } }

private fun Map<Bit, Int>.maxOrDefault(defaultValue: Bit): Bit {
    val zeroCount = getOrDefault(`0`, 0)
    val oneCount = getOrDefault(`1`, 0)
    return when {
        zeroCount > oneCount -> `0`
        oneCount > zeroCount -> `1`
        else -> defaultValue
    }
}

private fun Map<Bit, Int>.minOrDefault(defaultValue: Bit): Bit {
    val zeroCount = getOrDefault(`0`, 0)
    val oneCount = getOrDefault(`1`, 0)
    return when {
        zeroCount < oneCount -> `0`
        oneCount < zeroCount -> `1`
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