import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {

    @Test
    fun `Part One`() {
        val result = crabs("Day07_test.txt") { 1 }

        assertEquals(37, result)
    }

    @Test
    fun `Part Two`() {
        val result = crabs("Day07_test.txt") { it }

        assertEquals(168, result)
    }

}