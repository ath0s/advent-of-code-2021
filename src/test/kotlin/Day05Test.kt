import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Test {

    @Test
    fun `Part One`() {
        val result = hydroThermalVents("Day05_test.txt")

        assertEquals(5, result)
    }

    @Test
    @Disabled
    fun `Part Two`() {
        val result = hydroThermalVents("Day05_test.txt")

        assertEquals(0, result)
    }
    
}