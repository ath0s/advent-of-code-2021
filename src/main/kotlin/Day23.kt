import Day.Main
import java.util.*
import kotlin.io.path.readLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val roomPositions = intArrayOf(2, 4, 6, 8)

private const val hallwaySize = 11

fun moveAmphipods(filename: String, verbose: Boolean): Int {
    val input = filename.asPath().readLines()
    val state = input.parse()
    val goal = state.copy(rooms = state.rooms.flatten().sortedWith(nullsLast()).groupBy { it }.values.toList())
    return calculateMinimumEnergy(state, goal)
}

fun moveAmphipodsUnfolded(filename: String, verbose: Boolean): Int {
    val state = filename.asPath().readLines().toMutableList().apply {
        addAll(
            3,
            """|  #D#C#B#A#
               |  #D#B#A#C#""".trimMargin().lines()
        )
    }.parse()
    val goal = state.copy(rooms = state.rooms.flatten().sortedWith(nullsLast()).groupBy { it }.values.toList())
    return calculateMinimumEnergy(state, goal)
}

private fun List<String>.parse(): State {
    val hall = arrayOfNulls<Amphipod>(hallwaySize).toList()
    val rooms = arrayOf<MutableList<Amphipod>>(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
    forEach { line ->
        val amphipods = line.mapNotNull { it.toAmphipod() }
        if (amphipods.isNotEmpty()) {
            for (i in rooms.indices) {
                rooms[i] += amphipods[i]
            }
        }
    }

    val roomsValue = rooms.map { it.toList() }.toList()
    return State(hall, roomsValue)
}

private fun calculateMinimumEnergy(start: State, goal: State): Int {
    val dist = mutableMapOf(start to 0)
    val visited = mutableSetOf<State>()

    val queue = PriorityQueue<Pair<State, Int>>(compareBy { (_, priority) -> priority })

    queue += dist.toList()

    while (queue.isNotEmpty()) {
        val current = queue.remove().first
        if (current !in visited) {
            visited += current
            if (current == goal) {
                break
            }

            val currentCost = dist[current]!!

            for ((next, nextCost) in current.listMoves()) {
                val alt = currentCost + nextCost
                if (alt < (dist[next] ?: Int.MAX_VALUE)) {
                    dist[next] = alt
                    queue += next to alt
                }
            }
        }
    }

    return dist[goal]!!
}

private data class State(
    val hall: List<Amphipod?>,
    val rooms: List<List<Amphipod?>>
) {

    fun getRoom(index: Int) =
        rooms[index]

    fun listMoves(): List<Pair<State, Int>> {
        return buildList {
            (0..3).forEach { i ->
                for (pos in listOpenSpaces(i)) {
                    val moveResult = moveOut(i, pos)
                    if (moveResult is SuccessfulMove) {
                        val (updated, amphipod, steps) = moveResult
                        val cost = steps * amphipod.energyCost
                        add(updated to cost)
                    }
                }
            }

            for (i in hall.indices) {
                val moveResult = moveIn(i)
                if (moveResult is SuccessfulMove) {
                    val (updated, amphipod, steps) = moveResult
                    val cost = steps * amphipod.energyCost
                    add(updated to cost)
                }
            }
        }
    }

    private fun moveOut(roomIndex: Int, targetPosition: Int): MoveResult {
        val room = getRoom(roomIndex)
        val depth = room.indexOfFirst { it != null }
        if (depth < 0) {
            return FailedMove
        }

        val steps = abs(targetPosition - roomPositions[roomIndex]) + depth + 1
        val amphipod = room[depth]!!

        val updatedHall = hall.with(targetPosition, amphipod)
        val updatedRoom = room.with(depth, null)

        val updated = update(updatedHall, roomIndex, updatedRoom)
        return SuccessfulMove(updated, amphipod, steps)
    }

    private fun moveIn(hallwayPosition: Int): MoveResult {
        val amphipod = hall[hallwayPosition] ?: return FailedMove
        val goalRoomIndex = enumValues<Amphipod>().indexOf(amphipod)
        val goalPosition = roomPositions[goalRoomIndex]
        val start = if (goalPosition > hallwayPosition) hallwayPosition + 1 else hallwayPosition - 1
        val min = min(goalPosition, start)
        val max = max(goalPosition, start)
        if (hall.drop(min).take(max - min + 1).any { it != null })
            return FailedMove

        val room = getRoom(goalRoomIndex)
        if (room.any { it != null && it != amphipod })
            return FailedMove

        val depth = room.lastIndexOf(null)
        val steps = (max - min + 1) + depth + 1

        val updatedHall = hall.with(hallwayPosition, null)
        val updatedRoom = room.with(depth, amphipod)

        val updated = update(updatedHall, goalRoomIndex, updatedRoom)
        return SuccessfulMove(updated, amphipod, steps)
    }

    private fun update(updatedHall: List<Amphipod?>, roomIndex: Int, updatedRoom: List<Amphipod?>): State {
        val updatedRooms = rooms.with(roomIndex, updatedRoom)
        return copy(hall = updatedHall, rooms = updatedRooms)
    }

    private fun listOpenSpaces(roomIndex: Int): List<Int> {
        val position = roomPositions[roomIndex]

        return buildList {
            var i = position - 1
            while (i >= 0 && hall[i] == null) {
                if (i !in roomPositions) {
                    add(i)
                }
                i--
            }
            i = position + 1
            while (i < hall.size && hall[i] == null) {
                if (i !in roomPositions) {
                    add(i)
                }
                i++
            }
        }
    }

    private fun <T : Any?> List<T>.with(index: Int, value: T): List<T> =
        toMutableList().also { it[index] = value }

    private sealed interface MoveResult

    private object FailedMove : MoveResult

    private data class SuccessfulMove(
        val update: State,
        val amphipod: Amphipod,
        val steps: Int
    ) : MoveResult

}

private enum class Amphipod(val energyCost: Int) {
    A(1),
    B(10),
    C(100),
    D(1000)
}

private fun Char.toAmphipod() =
    try {
        enumValueOf<Amphipod>(toString())
    } catch (_: IllegalArgumentException) {
        null
    }


class Day23 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        moveAmphipods(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        moveAmphipodsUnfolded(filename, verbose)

    companion object : Main("Day23.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}