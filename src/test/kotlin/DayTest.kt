import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assumptions.assumeFalse
import java.lang.reflect.ParameterizedType
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class DayTest<D : Day>(
    @Language("file-reference") private val filename: String
) {

    open val partOneExpected: Number = -1
    open val partTwoExpected: Number = -1

    protected val target: D = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>).kotlin.newInstance()

    @Test
    fun `Part One`() {
        assumeFalse(partOneExpected.toInt() < 0)

        val result = target.partOne(filename, true)

        assertEquals(partOneExpected, result)
    }

    @Test
    fun `Part Two`() {
        assumeFalse(partTwoExpected.toInt() < 0)

        val result = target.partTwo(filename, true)

        assertEquals(partTwoExpected, result)
    }

}