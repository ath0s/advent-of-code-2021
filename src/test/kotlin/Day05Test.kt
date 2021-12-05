import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {

    @Test
    fun `Part One`() {
        val result = hydroThermalVents("Day05_test.txt", includeDiagonal = false, printBoard = true)

        assertEquals(5, result)
    }

    @Test
    fun `Part Two`() {
        val result = hydroThermalVents("Day05_test.txt", includeDiagonal = true, printBoard = true)

        assertEquals(12, result)
    }
    
}