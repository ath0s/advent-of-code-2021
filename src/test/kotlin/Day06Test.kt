import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day06Test {

    @Test
    fun `Part One`() {
        val result = lanternfish("Day06_test.txt", printState = true)

        assertEquals(5934, result)
    }

    @Test
    @Disabled
    fun `Part Two`() {
        val result = lanternfish("Day06_test.txt", printState = true)

        assertEquals(0, result)
    }

}