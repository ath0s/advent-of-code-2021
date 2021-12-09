import Day.Main
import kotlin.io.path.readLines

fun lavaTubes(filename: String, verbose: Boolean) : Int {
            return filename.asPath()
                .readLines().let { lines ->
                          val heightmap  = lines.map {line -> line.map { char ->char.digitToInt() }.toIntArray()}.toTypedArray()

                    val lowpoints = mutableListOf<Int>()

                    heightmap.forEachIndexed { x, row ->
                        row.forEachIndexed { y, height ->
                            val neighbors = heightmap.getNeighbors(x, y)
                            if(neighbors.all { it > height }) {
                                lowpoints += (height + 1)
                            }

                        }
                    }
                    lowpoints.sum()
                }

}

private fun Array<IntArray>.getNeighbors(x: Int, y: Int): List<Int> {
    val neighbors = mutableListOf<Int>()
    (x-1..x+1).forEach { nx ->
        val row = getOrNull(nx)
        if(row != null) {
            (y - 1..y + 1).forEach { ny ->
                if(!(nx == x && ny == y)) {
                    row.getOrNull(ny)?.let { neighbor ->
                        neighbors += neighbor
                    }
                }
            }
        }
    }
    return neighbors
}

class Day09 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        lavaTubes(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        lavaTubes(filename, verbose)

    companion object : Main("Day09.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}