import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {

    @Test
    fun `Part One`() {
        val result = powerConsumption("Day03_test.txt")

        assertEquals(198, result)
    }

    @Test
    fun `Part Two`() {
        val result = lifeSupportRating("Day03_test.txt")

        assertEquals(230, result)
    }

}