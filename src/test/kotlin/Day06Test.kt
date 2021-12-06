import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {

    @Test
    fun `Part One`() {
        val result = lanternfish("Day06_test.txt", 80, printState = true)

        assertEquals(5934, result)
    }

    @Test
    fun `Part Two`() {
        val result = lanternfish("Day06_test.txt", 256, printState = true)

        assertEquals(26984457539, result)
    }

}