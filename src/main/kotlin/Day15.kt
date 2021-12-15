import Day.Main
import dijkstra.Node
import dijkstra.calculateShortestPathFromSource

fun shortestPath(filename: String, verbose: Boolean):Int {
    val matrix = filename.parseMatrix()
    val end = matrix.lastIndex()
    
    val nodes = matrix.mapIndexedNotNull{ coordinate, _ -> coordinate to Node(coordinate)}.toMap()
    nodes.forEach { (coordinate, node) ->
        matrix.getOrthogonalNeighbors(coordinate).forEach { neighbor ->
            val neighborNode = nodes[neighbor]!!
            val cost = matrix[neighbor]
            node.adjacentNodes[neighborNode] = cost
        }
    }
    val startNode = nodes[Coordinate(0,0)]!!
    calculateShortestPathFromSource(startNode)

    val endNode = nodes[end]!!

    if (verbose) {
        val shortestPath = endNode.shortestPath.map { it.value }.toSet() + end
        matrix.print { it in shortestPath }
        println()
    }

    return endNode.distance
}


class Day15 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        shortestPath(filename,  verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        shortestPath(filename, verbose)

    companion object : Main("Day15.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}