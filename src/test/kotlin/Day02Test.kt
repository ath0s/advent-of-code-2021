import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun `Part One`() {
        val result = navigate("Day02_test.txt")

        assertEquals(150, result)
    }

    @Test
    fun `Part Two`() {
        val result = navigate("Day02_test.txt", true)

        assertEquals(900, result)
    }

}