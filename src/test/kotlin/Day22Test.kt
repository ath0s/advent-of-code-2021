import org.junit.jupiter.api.Nested

class Day22Test {

    @Nested
    inner class PartOne : DayTest<Day22>("Day22_test_part_one.txt") {
        override val partOneExpected = 590784L
    }

    @Nested
    inner class PartTwo : DayTest<Day22>("Day22_test_part_two.txt") {
        override val partTwoExpected = 2758514936282235L
    }

}
