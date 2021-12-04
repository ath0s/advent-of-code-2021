import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {

    @Test
    fun `Part One`() {
        val result = bingoScore("Day04_test.txt") { it.first() }

        assertEquals(4512, result)
    }

    @Test
    fun `Part Two`() {
        val result = bingoScore("Day04_test.txt") { it.last() }

        assertEquals(1924, result)
    }

}