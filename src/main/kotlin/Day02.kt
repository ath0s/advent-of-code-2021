import Day.Main
import Direction.*
import kotlin.io.path.useLines

private val lineFormat = Regex("""(\S+)\s+(\d+)""")

fun navigate(filename: String, useAim: Boolean = false): Int =
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


class Day02 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        navigate(filename)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        navigate(filename, true)

    companion object : Main("Day02.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }
    
}
