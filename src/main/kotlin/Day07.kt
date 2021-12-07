import Day.Main
import kotlin.io.path.readLines
import kotlin.math.abs

fun crabs(filename: String, verbose: Boolean, fuelPerStep: (Int) -> Int): Int =
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
            if(verbose) {
                println("Best position:\t$position, fuel: $fuel")
            }

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