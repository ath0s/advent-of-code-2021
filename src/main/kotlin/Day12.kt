import Day.Main
import Node.Companion.Node
import kotlin.io.path.readLines

fun findNumberOfPaths(filename: String, numberOfAllowedRevisitedSmallCaves: Int, verbose: Boolean): Int {
    val graph = filename.parseGraph()

    val paths = graph.findPaths(listOf(graph.start), numberOfAllowedRevisitedSmallCaves)

    if (verbose) {
        paths.forEach {
            println(it.joinToString(","))
        }
        println()
    }

    return paths.size
}

private fun String.parseGraph() =
    asPath()
        .readLines()
        .let { lines ->
            val edges = lines.map { line ->
                val (from, to) = line.split("-")
                Node(from) to Node(to)
            }
                .flatMap { (from, to) ->
                    when {
                        from == End || to == Start -> listOf(to to from)
                        from == Start || to == End -> listOf(from to to)
                        else -> listOf(from to to, to to from)
                    }
                }
                .groupBy({ (from, _) -> from }, { (_, to) -> to })
                .mapValues { (_, to) -> to.toSet() }
            Graph(edges)
        }

private sealed class Node(private val name: String) {
    final override fun toString() = name

    companion object {
        fun Node(name: String) =
            when {
                name == "start" -> Start
                name == "end" -> End
                name.all { it.isUpperCase() } -> BigCave(name)
                name.all { it.isLowerCase() } -> SmallCave(name)
                else -> throw IllegalArgumentException("Invalid name $name")
            }
    }

}

private object Start : Node("start")

private object End : Node("end")

private data class SmallCave(private val name: String) : Node(name)

private data class BigCave(private val name: String) : Node(name)

private class Graph(private val edges: Map<Node, Set<Node>>) {

    val start: Node get() = edges.keys.single { it == Start }

    fun findPaths(path: List<Node>, numberOfAllowedRevisitedSmallCaves: Int): List<List<Node>> {
        val previous = path.last()
        if (previous == End) {
            return listOf(path)
        }
        val existingDuplicateSmallCaves = path.filterIsInstance<SmallCave>()
            .groupBy { it }
            .mapValues { (_, visits) -> visits.size }
            .filterValues { it > 1 }
            .size

        val allowedNextStep: (Node) -> Boolean = if (existingDuplicateSmallCaves < numberOfAllowedRevisitedSmallCaves) {
            { true }
        } else {
            { !(it is SmallCave && it in path) }
        }
        val next = get(previous).filter(allowedNextStep)

        if (next.isEmpty()) {
            return emptyList()
        }

        return next.flatMap { findPaths(path + it, numberOfAllowedRevisitedSmallCaves) }
    }

    operator fun get(node: Node) =
        edges[node] ?: emptySet()

    override fun toString() =
        edges.map { (source, targets) -> "$source -> $targets" }.joinToString("\n")

}

class Day12 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        findNumberOfPaths(filename, 0, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        findNumberOfPaths(filename, 1, verbose)

    companion object : Main("Day12.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}