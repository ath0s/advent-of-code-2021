import Day.Main
import kotlin.io.path.readLines

private val rulePattern = Regex("""(on|off) x=(.+)\.\.(.+),y=(.+)\.\.(.+),z=(.+)\.\.(.+)""")

fun countCubes(filename: String, verbose: Boolean): Int {
      val rules = filename.asPath().readLines().map { it.toCubeRule() }

    var onCount = 0

    (-50..50).forEach { x ->
        (-50..50).forEach { y ->
            (-50..50).forEach { z ->
                var on = false
                rules.forEach { rule ->
                    val ruleOn = rule(x,y,z)
                    if(ruleOn != null) {
                        on = ruleOn
                    }
                }
                if(on) {
                    onCount++
                }
            }
        }
    }
    

    return onCount
}

fun countAllCubes(filename: String, verbose: Boolean): Long {
      val rules = filename.asPath().readLines().map { it.toCubeRule() }

    var onCount = 0L

    val minX = rules.minOfOrNull { it.x.first }!!
    val maxX = rules.maxOfOrNull { it.x.last }!!
    val minY = rules.minOfOrNull { it.y.first }!!
    val maxY = rules.maxOfOrNull { it.y.last }!!
    val minZ = rules.minOfOrNull { it.z.first }!!
    val maxZ = rules.maxOfOrNull { it.z.last }!!

    (minX..maxX).forEach { x ->
        (minY..maxY).forEach { y ->
            (minZ..maxZ).forEach { z ->
                var on = false
                rules.forEach { rule ->
                    val ruleOn = rule(x,y,z)
                    if(ruleOn != null) {
                        on = ruleOn
                    }
                }
                if(on) {
                    onCount++
                }
            }
        }
    }


    return onCount
}

private data class CubeRule(
    private val on: Boolean,
    val x: IntRange,
    val y: IntRange,
    val z: IntRange
) {

    operator fun invoke(x: Int, y: Int, z: Int) =
        if (x in this.x && y in this.y && z in this.z) {
            on
        } else {
            null
        }

    override fun toString() =
        "${if(on)"on" else "off"} x=$x,y=$y,z=$z"
}

private fun String.toCubeRule() : CubeRule {
    val (on, xFrom, xTo, yFrom,yTo,zFrom,zTo ) = rulePattern.matchEntire(this)!!.destructured
    return CubeRule(on=="on", (xFrom.toInt()..xTo.toInt()), (yFrom.toInt()..yTo.toInt()), (zFrom.toInt()..zTo.toInt()))
}


class Day22 : Day {

    override fun partOne(filename: String, verbose: Boolean): Number =
        countCubes(filename, verbose)

    override fun partTwo(filename: String, verbose: Boolean): Number =
        countCubes(filename, verbose)
        //countAllCubes(filename, verbose)

    companion object : Main("Day22.txt") {

        @JvmStatic
        fun main(args: Array<String>) = main()

    }

}