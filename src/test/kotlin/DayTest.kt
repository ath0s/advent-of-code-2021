import org.intellij.lang.annotations.Language
import java.lang.reflect.ParameterizedType
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class DayTest<D : Day>(
    @Language("file-reference") private val filename: String
) {

    abstract val partOneExpected: Number
    abstract val partTwoExpected: Number

    private val target: D = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>).kotlin.newInstance()

    @Test
    fun `Part One`() {
        val result = target.partOne(filename, true)

        assertEquals(partOneExpected, result)
    }

    @Test
    fun `Part Two`() {
        val result = target.partTwo(filename, true)

        assertEquals(partTwoExpected, result)
    }

}