import Day.Main
import LengthTypeId.numberOfSubPackets
import LengthTypeId.totalLengthInBits
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
    val binary = filename.asPath().readText().map { hexToBinary[it] }.joinToString("").mapTo(mutableListOf()) { it.toBit() }

    if (verbose) {
        println("original\t${binary.joinToString("")}")
    }

    val packet = binary.toPacket(verbose)
    if (verbose && binary.isNotEmpty()) {
        println("trailing\t${binary.toBinary()}")
    }

    return packet.sumVersion()
}

private fun MutableList<Bit>.toPacket(verbose: Boolean): Packet {
    val version = removeFirst(3).toInt()
    val typeId = removeFirst(3).toInt()
    val packet = if (typeId == 4) {
        toLiteralPacket(version, verbose)
    } else {
        toOperatorPacket(version, typeId, verbose)
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

private fun MutableList<Bit>.toOperatorPacket(version: Int, typeId: Int, verbose: Boolean): OperatorPacket {
    if (verbose) {
        print("operator packet (version $version) ")
    }
    val lengthTypeId = LengthTypeId.values()[removeFirst().toInt()]
    val subPackets = mutableListOf<Packet>()
    when (lengthTypeId) {
        totalLengthInBits -> {
            if (verbose) {
                print("(totalLengthInBits=")
            }
            val totalLengthInBits = removeFirst(lengthTypeId.numberOfBits).toInt()
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
            val numberOfSubPackets = removeFirst(lengthTypeId.numberOfBits).toInt()
            if (verbose) {
                println("$numberOfSubPackets)")
            }
            repeat(numberOfSubPackets) {
                subPackets += toPacket(verbose)
            }
        }
    }
    return OperatorPacket(version, typeId, lengthTypeId, subPackets)
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
    val typeId: Int

    fun sumVersion() = version
}

private data class LiteralPacket(
    override val version: Int,
    val value: Long
) : Packet {
    override val typeId = 4
}

private data class OperatorPacket(
    override val version: Int,
    override val typeId: Int,
    val lengthTypeId: LengthTypeId,
    val subPackets: List<Packet>
) : Packet {

    override fun sumVersion() =
        super.sumVersion() + subPackets.sumOf { it.sumVersion() }
}

private enum class LengthTypeId(val numberOfBits: Int) {
    totalLengthInBits(15),
    numberOfSubPackets(11)
}

class Day16 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        sumVersionNumbers(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        sumVersionNumbers(filename, verbose)

    companion object : Main("Day16.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}