import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16Test : DayTest<Day16>() {

    @Nested
    inner class PartOne {

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
        fun `operator packet (version 3) which contains two sub-packets, each sub-packet is an operator packet that contains two literal values, different length type ID`() {
            val result = target.partOne("Day16_test_part_one_third.txt", true)

            assertEquals(23, result)
        }

        @Test
        fun `operator packet that contains an operator packet that contains an operator packet that contains five literal values`() {
            val result = target.partOne("Day16_test_part_one_fourth.txt", true)

            assertEquals(31, result)
        }

    }

    @Nested
    inner class PartTwo {

        @Test
        fun `finds the sum of 1 and 2`() {
            val result = target.partTwo("Day16_test_part_two_first.txt", true)

            assertEquals(3L, result)
        }

        @Test
        fun `finds the product of 6 and 9`() {
            val result = target.partTwo("Day16_test_part_two_second.txt", true)

            assertEquals(54L, result)
        }

        @Test
        fun `finds the minimum of 7, 8, and 9`() {
            val result = target.partTwo("Day16_test_part_two_third.txt", true)

            assertEquals(7L, result)
        }

        @Test
        fun `finds the maximum of 7, 8, and 9`() {
            val result = target.partTwo("Day16_test_part_two_fourth.txt", true)

            assertEquals(9L, result)
        }

        @Test
        fun `5 is less than 15`() {
            val result = target.partTwo("Day16_test_part_two_fifth.txt", true)

            assertEquals(1L, result)
        }

        @Test
        fun `5 is not greater than 15`() {
            val result = target.partTwo("Day16_test_part_two_sixth.txt", true)

            assertEquals(0L, result)
        }

        @Test
        fun `5 is not equal to 15`() {
            val result = target.partTwo("Day16_test_part_two_seventh.txt", true)

            assertEquals(0L, result)
        }

        @Test
        fun `1 plus 3 equals 2 times 2`() {
            val result = target.partTwo("Day16_test_part_two_eighth.txt", true)

            assertEquals(1L, result)
        }

    }

}

