import Direction.*
import org.intellij.lang.annotations.Language
import kotlin.io.path.useLines

private val lineFormat = Regex("""(\S+)\s+(\d+)""")

fun navigate(@Language("file-reference") filename: String, useAim: Boolean = false): Int =
    filename.asPath()
        .useLines { lines ->
            return lines.map { line ->
                val (direction: Direction, distance: Int) = lineFormat.find(line)!!
                movement(direction, distance, useAim)
            }.fold(Position()) { position, movement -> movement(position) }
                .total
        }

private data class Position(val horizontal: Int = 0, val depth: Int = 0, val aim : Int = 0) {
    val total get() =
        horizontal * depth
}

@Suppress("EnumEntryName")
private enum class Direction {
    forward,
    down,
    up
}

private fun movement(direction: Direction, distance: Int, useAim: Boolean = false): (Position) -> Position =
    when (direction) {
        forward -> { position ->
            position.copy(
                horizontal = position.horizontal + distance,
                depth = position.depth + (position.aim * distance)
            )
        }
        down -> { position ->
            if (useAim) {
                position.copy(aim = position.aim + distance)
            } else {
                position.copy(depth = position.depth + distance)
            }
        }
        up -> { position ->
            if (useAim) {
                position.copy(aim = position.aim - distance)
            } else {
                position.copy(depth = position.depth - distance)
            }
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
        navigate(filename, true)
                             
    println("Part One:\t${partOne()}")
    println("Part Two:\t${partTwo()}")
}