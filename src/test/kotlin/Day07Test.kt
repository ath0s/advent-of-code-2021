import org.junit.jupiter.api.Disabled
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {

    @Test
    fun `Part One`() {
        val result = crabs("Day07_test.txt")

        assertEquals(37, result)
    }

    @Test
    @Disabled
    fun `Part Two`() {
        val result = crabs("Day07_test.txt")

        assertEquals(0, result)
    }

}