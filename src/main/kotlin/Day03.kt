import Bit.*
import Day.Main

fun powerConsumption(filename: String, verbose: Boolean): Int {
    val lines = filename.asPath().readBinaries()
    val bitCounts = lines.toBitCounts()

    val gamma = bitCounts
        .mapNotNull { it.maxByOrNull { (_, count) -> count }?.key }
        .toBinary()

    val epsilon = !gamma

    if(verbose) {
        println("gamma\t$gamma")
        println("epsilon\t$epsilon")
    }

    return gamma.toInt() * epsilon.toInt()
}

fun lifeSupportRating(filename: String, verbose: Boolean): Int {
    val lines = filename.asPath().readBinaries()
    val oxygenGeneratorRating = lines.oxygenGeneratorRating(0, verbose)
    val co2ScrubberRating = lines.co2ScrubberRating(0, verbose)

    if(verbose) {
        println("oxygenGeneratorRating\t$oxygenGeneratorRating")
        println("co2ScrubberRating\t$co2ScrubberRating")
    }

    return oxygenGeneratorRating.toInt() * co2ScrubberRating.toInt()
}

private tailrec fun List<Binary>.oxygenGeneratorRating(index: Int, verbose: Boolean): Binary {
    val bitToKeep = toBitCounts()[index].maxOrDefault(`1`)
    val linesToKeep = filter { it[index] == bitToKeep }
    if(verbose) {
        println("oxygenGeneratorRating (index=$index,char=$bitToKeep)\t$linesToKeep")
    }
    return linesToKeep.singleOrNull() ?: linesToKeep.oxygenGeneratorRating(index + 1, verbose)
}

private tailrec fun List<Binary>.co2ScrubberRating(index: Int, verbose: Boolean): Binary {
    val bitToKeep = toBitCounts()[index].minOrDefault(`0`)
    val linesToKeep = filter { it[index] == bitToKeep }
    if(verbose) {
        println("co2ScrubberRating(index=$index,char=$bitToKeep)\t$linesToKeep")
    }
    return linesToKeep.singleOrNull() ?: linesToKeep.co2ScrubberRating(index + 1, verbose)
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

class Day03 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        powerConsumption(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        lifeSupportRating(filename, verbose)

    companion object : Main("Day03.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}