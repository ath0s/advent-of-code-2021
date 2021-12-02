import org.intellij.lang.annotations.Language
import kotlin.io.path.useLines

private val lineFormat = Regex("""(\S+)\s+(\d+)""")

fun navigate(@Language("file-reference") filename: String): Int =
    filename.asPath()
        .useLines { lines ->
            return lines.map { line ->
                val (direction: Direction, distance: Int) = lineFormat.find(line)!!
                direction to distance
            }.fold(Position()) { position, (direction, distance) -> direction(position, distance); position }
                .total
        }


class Position(var horisontal: Int = 0, var depth: Int = 0) {
    val total get() =
        horisontal * depth
}

enum class Direction(private val move: (Position, Int) -> Unit) : (Position, Int) -> Unit {
    forward({ position: Position, distance: Int ->
        position.horisontal += distance
    }),
    down({ position: Position, distance: Int ->
        position.depth += distance
    }),
    up({ position: Position, distance: Int ->
        position.depth -= distance
    });

    override fun invoke(position: Position, distance: Int) {
        move(position, distance)
    }
}

private inline operator fun <reified T : Enum<T>> MatchResult.component1(): T =
    enumValueOf(groupValues[1])

private operator fun MatchResult.component2(): Int =
    groupValues[2].toInt()

fun main() {
    val filename = "Day02.txt"

    fun partOne() =
        navigate(filename)

    fun partTwo() =
        navigate(filename)
                             
    println("Part One:\t${partOne()}")
    //println("Part Two:\t${partTwo()}")
}