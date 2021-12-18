import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test : DayTest<Day18>("Day18_test.txt") {

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(
        delimiterString = " -> ",
        value = [
            "[[[[[9,8],1],2],3],4] -> [[[[0,9],2],3],4]",
            "[7,[6,[5,[4,[3,2]]]]] -> [7,[6,[5,[7,0]]]]",
            "[[6,[5,[4,[3,2]]]],1] -> [[6,[5,[7,0]]],3]",
            "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]] -> [[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]] -> [[3,[2,[8,0]]],[9,[5,[7,0]]]]"
        ]
    )
    fun `Should explode pair`(inputString: String, expectedString: String) {
        val reduced = inputString.toSnailfishPair().apply { tryExplode() }
        val expected = expectedString.toSnailfishPair()

        assertEquals(expected, reduced)
    }

    @Test
    fun `Test addition`() {
        val first = "[[[[4,3],4],4],[7,[[8,4],9]]]".toSnailfishPair()
        val second = "[1,1]".toSnailfishPair()

        val result = first + second

        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (4)`() {
        val input = """
               [1,1]
               [2,2]
               [3,3]
               [4,4]
           """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[1,1],[2,2]],[3,3]],[4,4]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (5)`() {
        val input = """
                   [1,1]
                   [2,2]
                   [3,3]
                   [4,4]
                   [5,5]
               """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[3,0],[5,3]],[4,4]],[5,5]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (6)`() {
        val input = """
                   [1,1]
                   [2,2]
                   [3,3]
                   [4,4]
                   [5,5]
                   [6,6]
               """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[5,0],[7,4]],[5,5]],[6,6]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 1)`() {
        val input = """
                [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
                [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
                """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 2)`() {
        val input = """
                   [[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]
                   [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 3)`() {
        val input = """
                   [[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]
                   [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 4)`() {
        val input = """
                  [[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]
                  [7,[5,[[3,8],[1,4]]]]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 5)`() {
        val input = """
                   [[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]
                   [[2,[2,2]],[8,[8,1]]]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 6)`() {
        val input = """
                    [[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]
                    [2,9]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 7)`() {
        val input = """
                   [[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]
                   [1,[[[9,3],9],[[9,0],[0,7]]]]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 8)`() {
        val input = """
                   [[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]
                   [[[5,[7,4]],7],1]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger, part 9)`() {
        val input = """
                   [[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]
                   [[[[4,2],2],6],[8,7]]
                   """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]".toSnailfishPair(), result)
    }

    @Test
    fun `Test sum (larger)`() {
        val input = """
            [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
            [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
            [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
            [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
            [7,[5,[[3,8],[1,4]]]]
            [[2,[2,2]],[8,[8,1]]]
            [2,9]
            [1,[[[9,3],9],[[9,0],[0,7]]]]
            [[[5,[7,4]],7],1]
            [[[[4,2],2],6],[8,7]]
        """.trim().lines().map { it.trim().toSnailfishPair() }

        val result = input.sum()

        assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]".toSnailfishPair(), result)
    }

    override val partOneExpected = 4140
    
}

