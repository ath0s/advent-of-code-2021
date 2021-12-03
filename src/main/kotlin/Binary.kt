import java.nio.file.Path
import kotlin.io.path.readLines

internal class Binary(private val bits: Array<Bit>) {

    override fun toString(): String {
        return bits.joinToString("") { it.toString() }
    }

    fun toInt() =
        toString().toInt(2)

    operator fun get(index: Int) =
        bits[index]

    operator fun not() =
        Binary(bits.map { !it }.toTypedArray())

    fun <R> mapIndexed(transform: (index: Int, Bit) -> R) =
        bits.mapIndexed(transform)

}

internal fun Collection<Bit>.toBinary() =
    Binary(toTypedArray())

internal fun String.toBinary() =
    Binary(map { it.toBit() }.toTypedArray())

internal fun Path.readBinaries() =
    readLines().map { it.toBinary() }