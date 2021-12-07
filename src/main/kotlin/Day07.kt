import org.intellij.lang.annotations.Language
import kotlin.io.path.readLines
import kotlin.math.abs

fun crabs(@Language("file-reference") filename: String, fuelPerStep: (Int) -> Int): Int =
    filename.asPath()
        .readLines()
        .let { lines ->
            val positions = lines[0].split(",").map { it.toInt() }
            val min = positions.minOrNull()!!
            val max = positions.maxOrNull()!!
            val cache = mutableMapOf<Int, Int>()
            val (position, fuel) = (min..max).fold(-1 to Integer.MAX_VALUE) { (bestPosition, bestFuel), currentPosition ->
                val currentFuel = calculateFuelIfLessThan(cache, positions, currentPosition, bestFuel, fuelPerStep)
                if (currentFuel != null) {
                    currentPosition to currentFuel
                } else {
                    bestPosition to bestFuel
                }
            }
            println("Best position:\t$position, fuel: $fuel")

            return fuel
        }

private fun calculateFuelIfLessThan(
    cache: MutableMap<Int, Int>,
    positions: Iterable<Int>,
    currentPosition: Int,
    bestFuel: Int,
    fuelPerStep: (Int) -> Int
): Int? {
    var sum = 0
    for (position in positions) {
        val moves = abs(currentPosition - position)
        val fuel = cache.computeIfAbsent(moves) { (1..it).map(fuelPerStep).sum() }
        sum += fuel
        if (sum >= bestFuel) {
            return null
        }
    }
    return sum
}

fun main() {
    val filename = "Day07.txt"

    fun partOne() =
        crabs(filename) { 1 }

    fun partTwo() =
        crabs(filename) { it }

    println("Part One:\t${partOne()}")
    println("Part Two:\t${partTwo()}")
}
