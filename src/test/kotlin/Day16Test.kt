import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test : DayTest<Day16>() {

    @Test
    fun `operator packet (version 4) which contains an operator packet (version 1) which contains an operator packet (version 5) which contains a literal value (version 6)`() {
        val result = target.partOne("Day16_test_part_one_first.txt", true)

        assertEquals(16, result)
    }

    @Test
    fun `operator packet (version 3) which contains two sub-packets, each sub-packet is an operator packet that contains two literal values`() {
        val result = target.partOne("Day16_test_part_one_second.txt", true)

        assertEquals(12, result)
    }

    @Test
    fun `operator packet (version 3) which contains two sub-packets, each sub-packet is an operator packet that contains two literal values, differrent length type ID`() {
        val result = target.partOne("Day16_test_part_one_third.txt", true)

        assertEquals(23, result)
    }

    @Test
    fun `operator packet that contains an operator packet that contains an operator packet that contains five literal values`() {
        val result = target.partOne("Day16_test_part_one_fourth.txt", true)

        assertEquals(31, result)
    }

}

