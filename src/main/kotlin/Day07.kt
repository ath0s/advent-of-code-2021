import Day.Main
import kotlin.io.path.readLines
import kotlin.math.abs

fun crabs(filename: String, verbose: Boolean, fuelPerStep: (Int) -> Int): Int =
    filename.asPath()
        .readLines()
        .let { lines ->
            val positions = lines[0].split(",").map { it.toInt() }.toIntArray()
            val min = positions.minOrNull()!!
            val max = positions.maxOrNull()!!
            val cache = IntArray(max + 1) {
                (1..it).sumOf(fuelPerStep)
            }
            val (position, fuel) = (min..max).fold(-1 to Integer.MAX_VALUE) { (bestPosition, bestFuel), currentPosition ->
                val currentFuel = calculateFuelIfLessThan(cache, positions, currentPosition, bestFuel)
                if (currentFuel != null) {
                    currentPosition to currentFuel
                } else {
                    bestPosition to bestFuel
                }
            }
            if (verbose) {
                println("Best position:\t$position, fuel: $fuel")
            }

            return fuel
        }

private fun calculateFuelIfLessThan(
    cache: IntArray,
    positions: IntArray,
    currentPosition: Int,
    bestFuel: Int
): Int? {
    var sum = 0
    for (position in positions) {
        val moves = abs(currentPosition - position)
        val fuel = cache[moves]
        sum += fuel
        if (sum >= bestFuel) {
            return null
        }
    }
    return sum
}

class Day07 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        crabs(filename, verbose) { 1 }

    override fun partTwo(filename: String, verbose: Boolean): Number =
        crabs(filename, verbose) { it }

    companion object : Main("Day07.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}