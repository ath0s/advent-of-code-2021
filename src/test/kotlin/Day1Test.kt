import org.junit.jupiter.api.Test

class Day1Test {

    @Test
    fun `Should calculate number of increases`() {
        val result = numberOfIncreases("Day01_test.txt")

        assert(result == 7)
    }

}