import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test : DayTest<Day12>("Day12_test.txt") {

    @Test
    fun `Part One - Small`() {
        val result = target.partOne("Day12_test_small.txt", true)

        assertEquals(10, result)
    }


    @Test
    fun `Part One - Larger`() {
        val result = target.partOne("Day12_test_larger.txt", true)

        assertEquals(19, result)
    }

    override val partOneExpected = 226

}

