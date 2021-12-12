import Day.Main
import Node.Companion.Node
import kotlin.io.path.readLines

fun pathsWithSmallCavesAtMostOnce(filename: String, verbose: Boolean): Int {
    val graph = filename.parseGraph()

    if (verbose) {
        println(graph)
        println()
    }

    val paths = graph.findPaths(listOf(graph.start))

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

private sealed interface Node {
    override fun toString(): String

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

private object Start : Node {
    override fun toString() = "start"
}

private object End : Node {
    override fun toString() = "end"
}

private data class SmallCave(private val name: String) : Node {
    override fun toString() = name
}

private data class BigCave(private val name: String) : Node {
    override fun toString() = name
}

private class Graph(private val edges: Map<Node, Set<Node>>) {

    val start: Node get() = edges.keys.single { it == Start }


    fun findPaths(path: List<Node>): List<List<Node>> {
        val previous = path.last()
        if (previous == End) {
            return listOf(path)
        }
        val next = get(previous).filter { !(it is SmallCave && it in path) }
        if (next.isEmpty()) {
            return emptyList()
        }
        return next.flatMap { findPaths(path + it) }
    }

    operator fun get(node: Node) =
        edges[node] ?: emptySet()

    override fun toString() =
        edges.map { (source, targets) -> "$source -> $targets" }.joinToString("\n")

}

class Day12 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        pathsWithSmallCavesAtMostOnce(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        pathsWithSmallCavesAtMostOnce(filename, verbose)

    companion object : Main("Day12.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}