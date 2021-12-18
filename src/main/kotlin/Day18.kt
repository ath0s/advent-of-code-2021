import Day.Main
import kotlin.io.path.readLines

fun snailFishNumberMagnitude(filename: String, verbose: Boolean): Int {
    val pairs = filename.asPath().readLines().map { it.toSnailfishPair() }

    val result = pairs.sum()

    if(verbose) {
        println("result\t$result")
    }

    return result.magnitude
}

internal sealed interface SnailfishElement {
    val magnitude: Int
    fun addToLeft(value: RegularNumber)
    fun addToRight(value: RegularNumber)
}

internal data class SnailfishPair(
    var left: SnailfishElement,
    var right: SnailfishElement
) : SnailfishElement {

    override val magnitude
        get() = 3 * left.magnitude + 2 * right.magnitude

    private fun reduce() {
        do {
            val reduced = tryExplode() != null || trySplit()
            if (reduced) {
                println(this)
            }
        } while (reduced)
    }

    operator fun plus(other: SnailfishPair): SnailfishPair {
        val sum = SnailfishPair(this, other)
        println("after addition:\t$sum")
        sum.reduce()
        return sum
    }

    internal fun tryExplode(depth: Int = 0): ExplosionResult? {
        if (depth == 4) {
            print("after explode:\t")
            return Exploded(left as RegularNumber, right as RegularNumber)
        }
        (left as? SnailfishPair)?.tryExplode(depth + 1)?.let { result ->
            return when (result) {
                is Exploded -> {
                    left = RegularNumber(0)
                    right.addToLeft(result.addToRight)
                    result.addToLeft()
                }
                is AddToRight -> {
                    right.addToLeft(result.addToRight)
                    DidExplode
                }
                else -> result
            }
        }
        (right as? SnailfishPair)?.tryExplode(depth + 1)?.let { result ->
            return when (result) {
                is Exploded -> {
                    right = RegularNumber(0)
                    left.addToRight(result.addToLeft)
                    result.addToRight()
                }
                is AddToLeft -> {
                    left.addToRight(result.addToLeft)
                    DidExplode
                }
                else -> result
            }
        }
        return null
    }

    private fun trySplit(): Boolean {
        (left as? RegularNumber)?.let {
            val split = it.split()
            if (split != null) {
                left = split
                return true
            }
        }
        (left as? SnailfishPair)?.let {
            if (it.trySplit()) {
                return true
            }
        }
        (right as? RegularNumber)?.let {
            val split = it.split()
            if (split != null) {
                right = split
                return true
            }
        }
        (right as? SnailfishPair)?.let {
            if (it.trySplit()) {
                return true
            }
        }
        return false
    }

    override fun addToLeft(value: RegularNumber) =
        left.addToLeft(value)

    override fun addToRight(value: RegularNumber) =
        right.addToRight(value)

    override fun toString() =
        "[$left,$right]"

}

internal data class RegularNumber(
    var number: Int
) : SnailfishElement {

    override val magnitude get() = number

    fun split(): SnailfishPair? =
        if (number >= 10) {
            val left = number / 2
            val right = left + (number % 2)
            print("after split:\t")
            SnailfishPair(RegularNumber(left), RegularNumber(right))
        } else {
            null
        }

    override fun addToLeft(value: RegularNumber) = plusAssign(value)

    override fun addToRight(value: RegularNumber) = plusAssign(value)

    operator fun plusAssign(value: RegularNumber) {
        number += value.number
    }

    override fun toString() =
        number.toString()

}

internal sealed interface ExplosionResult

private data class Exploded(
    val addToLeft: RegularNumber,
    val addToRight: RegularNumber
) : ExplosionResult {

    fun addToLeft() =
        AddToLeft(addToLeft)

    fun addToRight() =
        AddToRight(addToRight)
}

@JvmInline
private value class AddToLeft(
    val addToLeft: RegularNumber
) : ExplosionResult

@JvmInline
private value class AddToRight(
    val addToRight: RegularNumber
) : ExplosionResult

private object DidExplode : ExplosionResult

internal fun String.toSnailfishPair() =
    toMutableList().toSnailfishPair()

private fun MutableList<Char>.toSnailfishPair(): SnailfishPair {
    assert(removeFirst() == '[')
    val left = if (first() == '[') toSnailfishPair() else toRegularNumber()
    assert(removeFirst() == ',')
    val right = if (first() == '[') toSnailfishPair() else toRegularNumber()
    assert(removeFirst() == ']')
    return SnailfishPair(left, right)
}

private fun MutableList<Char>.toRegularNumber() =
    let {
        buildString {
            while (it.first().isDigit()) {
                append(it.removeFirst())
            }
        }
    }.toInt().let { RegularNumber(it) }

internal fun Iterable<SnailfishPair>.sum() =
    reduce { first, second -> first + second }

class Day18 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        snailFishNumberMagnitude(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        snailFishNumberMagnitude(filename, verbose)

    companion object : Main("Day18.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()
    }

}