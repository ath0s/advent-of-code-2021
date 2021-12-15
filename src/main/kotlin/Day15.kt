import Day.Main
import dijkstra.Node
import dijkstra.calculateShortestPathFromSource

fun shortestPath(filename: String, matrixMultiplier: Int, verbose: Boolean): Int {
    val originalMatrix = filename.parseMatrix()
    val matrix = originalMatrix * matrixMultiplier

    if (verbose && matrixMultiplier > 1) {
        matrix.print { it in originalMatrix }
        println()
    }

    val end = matrix.lastIndex()

    val nodes = matrix.mapIndexedNotNull { coordinate, _ -> coordinate to Node(coordinate) }.toMap()
    nodes.forEach { (coordinate, node) ->
        matrix.getOrthogonalNeighbors(coordinate).forEach { neighbor ->
            val neighborNode = nodes[neighbor]!!
            val cost = matrix[neighbor]
            node.adjacentNodes[neighborNode] = cost
        }
    }
    val startNode = nodes[Coordinate(0, 0)]!!
    calculateShortestPathFromSource(startNode)

    val endNode = nodes[end]!!

    if (verbose) {
        val shortestPath = endNode.shortestPath.map { it.value }.toSet() + end
        matrix.print { it in shortestPath }
        println()
    }

    return endNode.distance
}

private operator fun Matrix.times(multiplier: Int): Matrix {
    val originalWidth = lastIndex().x + 1
    val originalHeight = lastIndex().y + 1

    return Matrix(originalHeight * multiplier) { y ->
        Array(originalWidth * multiplier) { x ->
            val originalRisk = get(Coordinate(x % originalWidth, y % originalHeight))
            val distance = (y / originalHeight) + (x / originalWidth)
            (originalRisk + distance - 1) % 9 + 1
        }
    }
}

class Day15 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        shortestPath(filename, 1, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        shortestPath(filename, 5, verbose)

    companion object : Main("Day15.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}