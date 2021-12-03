import org.intellij.lang.annotations.Language
import kotlin.io.path.useLines

fun powerConsumption(@Language("file-reference") filename: String): Int =
    filename.asPath()
        .useLines { lines ->
            lines.flatMap { line -> line.mapIndexed { index, c -> (index to c) } }
                .groupBy({ (index, _) -> index }, { (_, c) -> c })
                .mapValues { (_, list) -> list.groupBy { it }.mapValues { (_, chars) -> chars.size } }
                .let { charsByIndex ->
                    charsByIndex.toList().sortedBy { (index, _) -> index }.map { (_, chars) -> chars }
                }
                .let { charCounts ->
                    val gammaBinary =
                        charCounts.mapNotNull { charCount -> charCount.maxByOrNull { (_, count) -> count }?.key }
                            .toCharArray()
                            .let { String(it) }

                    val epsilonBinary = gammaBinary.map { if (it == '1') '0' else '1' }
                        .toCharArray()
                        .let { String(it) }

                    val gamma = gammaBinary.toInt(2)
                    val epsilon = epsilonBinary.toInt(2)

                    return gamma * epsilon
                }
        }

fun main(args: Array<String>) {
    val filename = "Day03.txt"

    fun partOne() =
        powerConsumption(filename)

    fun partTwo() =
        powerConsumption(filename)

    println("Part One:\t${partOne()}")
    //println("Part Two:\t${partTwo()}")

}