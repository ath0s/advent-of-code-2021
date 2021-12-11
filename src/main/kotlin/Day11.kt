import Day.Main

fun countOctopusFlashes(filename: String, iterations: Int, verbose: Boolean): Int {
    val matrix = filename.parseMatrix()
    var count = 0
    for (i in 1..iterations) {
        val flashes = matrix.step()
        if (verbose) {
            matrix.print { it in flashes }
            println()
        }
        count += flashes.size
    }
    return count
}

fun firstFullFlash(filename: String, verbose: Boolean): Int {
    val matrix = filename.parseMatrix()
    return generateSequence { matrix.step() }
        .onEach { flashes ->
            if (verbose) {
                matrix.print { it in flashes }
                println()
            }
        }
        .indexOfFirst {
            it.size == matrix.length
        } + 1
}

private fun Matrix.step(): Set<Coordinate> {
    updateEach { it + 1 }

    val flashed = mutableSetOf<Coordinate>()

    flash(flashed)

    flashed.forEach { set(it, 0) }

    return flashed
}

private tailrec fun Matrix.flash(flashed: MutableSet<Coordinate>) {
    val toFlash = filterIndexed { coordinate, value ->
        value > 9 && flashed.add(coordinate)
    }
    if (toFlash.isEmpty()) {
        return
    }
    toFlash.flatMap { getAllNeighbors(it) }
        .forEach { update(it) { value -> value + 1 } }

    flash(flashed)
}

class Day11 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        countOctopusFlashes(filename, 100, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        firstFullFlash(filename, verbose)

    companion object : Main("Day11.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}