import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {

    @Test
    fun `Part One`() {
        val result = numberOfIncreases("Day01_test.txt")

        assertEquals(7, result)
    }

    @Test
    fun `Part Two`() {
        val result = numberOfIncreases("Day01_test.txt", 3)

        assertEquals(5, result)
    }

}