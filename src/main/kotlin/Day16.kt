import Day.Main
import LengthType.numberOfSubPackets
import LengthType.totalLengthInBits
import PacketType.*
import kotlin.io.path.readText

private val hexToBinary = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)

fun sumVersionNumbers(filename: String, verbose: Boolean): Int {
    val packet = parsePacket(filename, verbose)

    return packet.sumVersion()
}

fun calculatePacketValue(filename: String, verbose: Boolean): Long {
    val packet = parsePacket(filename, verbose)

    return packet.value
}

private fun parsePacket(filename: String, verbose: Boolean) : Packet {
    val binary = filename.asPath().readText().map { hexToBinary[it] }.joinToString("").mapTo(mutableListOf()) { it.toBit() }

       if (verbose) {
           println("original\t${binary.joinToString("")}")
       }

       val packet = binary.toPacket(verbose)
       if (verbose && binary.isNotEmpty()) {
           println("trailing\t${binary.toBinary()}")
       }

       return packet
}

private fun MutableList<Bit>.toPacket(verbose: Boolean): Packet {
    val version = removeFirst(3).toInt()
    val type = removeFirst(3).toInt().toPacketType()
    val packet = if (type == literal) {
        toLiteralPacket(version, verbose)
    } else {
        toOperatorPacket(version, type, verbose)
    }

    return packet
}

private fun MutableList<Bit>.toLiteralPacket(version: Int, verbose: Boolean): LiteralPacket {
    if (verbose) {
        print("literal packet (version $version), value=")
    }

    val valueBits = mutableListOf<Bit>()

    var moreGroups: Boolean
    do {
        val group = removeFirst(5)
        moreGroups = group[0].toInt() == 1
        valueBits += group.drop(1)
    } while (moreGroups)

    val value = valueBits.toLong()
    if (verbose) {
        println(value)
    }
    return LiteralPacket(version, valueBits.toLong())
}

private fun MutableList<Bit>.toOperatorPacket(version: Int, type: PacketType, verbose: Boolean): OperatorPacket {
    if (verbose) {
        print("operator packet (version $version) ")
    }
    val lengthType = removeFirst().toLengthType()
    val subPackets = mutableListOf<Packet>()
    when (lengthType) {
        totalLengthInBits -> {
            if (verbose) {
                print("(totalLengthInBits=")
            }
            val totalLengthInBits = removeFirst(lengthType.numberOfBits).toInt()
            if (verbose) {
                println("$totalLengthInBits)")
            }
            val content = removeFirst(totalLengthInBits)
            while (content.size > 6) {
                subPackets += content.toPacket(verbose)
            }

        }
        numberOfSubPackets -> {
            if (verbose) {
                print("(numberOfSubPackets=")
            }
            val numberOfSubPackets = removeFirst(lengthType.numberOfBits).toInt()
            if (verbose) {
                println("$numberOfSubPackets)")
            }
            repeat(numberOfSubPackets) {
                subPackets += toPacket(verbose)
            }
        }
    }
    return when (type) {
        sum -> SumPacket(version, subPackets)
        product -> ProductPacket(version, subPackets)
        minimum -> MinimumPacket(version, subPackets)
        maximum -> MaximumPacket(version, subPackets)
        greaterThan -> GreaterThanPacket(version, subPackets)
        lessThan -> LessThanPacket(version, subPackets)
        equalTo -> EqualToPacket(version, subPackets)
        else -> throw IllegalArgumentException("$type is not a supported operator type")
    }
}

private fun <T> MutableList<T>.removeFirst(n: Int) =
    mutableListOf<T>().also {
        repeat(n) { _ ->
            it.add(removeFirst())
        }
    }

private fun List<Bit>.toInt() =
    toBinary().toInt()

private fun List<Bit>.toLong() =
    toBinary().toLong()

private sealed interface Packet {
    val version: Int
    val value: Long
    fun sumVersion() = version
}

private data class LiteralPacket(
    override val version: Int,
    override val value: Long
) : Packet

private sealed class OperatorPacket(
    override val version: Int,
    val subPackets: List<Packet>
) : Packet {

    override fun sumVersion() =
        super.sumVersion() + subPackets.sumOf { it.sumVersion() }
}

private class SumPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        subPackets.sumOf { it.value }
}

private class ProductPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        subPackets.map { it.value }.reduce(Long::times)
}

private class MinimumPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        subPackets.minOf { it.value }
}

private class MaximumPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        subPackets.maxOf { it.value }
}

private class GreaterThanPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        if (subPackets[0].value > subPackets[1].value) 1L else 0L
}

private class LessThanPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        if (subPackets[0].value < subPackets[1].value) 1L else 0L
}

private class EqualToPacket(version: Int, subPackets: List<Packet>) : OperatorPacket(version, subPackets) {
    override val value =
        if (subPackets[0].value == subPackets[1].value) 1L else 0L
}

@Suppress("EnumEntryName")
private enum class PacketType {
    sum,
    product,
    minimum,
    maximum,
    literal,
    greaterThan,
    lessThan,
    equalTo
}

@Suppress("EnumEntryName")
private enum class LengthType(val numberOfBits: Int) {
    totalLengthInBits(15),
    numberOfSubPackets(11)
}

private fun Int.toPacketType() =
    PacketType.values()[toInt()]

private fun Bit.toLengthType() =
    LengthType.values()[toInt()]

class Day16 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        sumVersionNumbers(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        calculatePacketValue(filename, verbose)

    companion object : Main("Day16.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}