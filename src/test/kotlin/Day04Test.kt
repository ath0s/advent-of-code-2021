import org.junit.jupiter.api.Disabled
import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {

    @Test
    fun `Part One`() {
        val result = bingoScore("Day04_test.txt")

        assertEquals(4512, result)
    }

    @Test
    @Disabled
    fun `Part Two`() {
        val result = bingoScore("Day04_test.txt")

        assertEquals(0, result)
    }

}