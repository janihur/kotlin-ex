import net.metsankulma.Sample
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test

class SampleTest {
    private val testSample: Sample = Sample()

    @Test
    fun testSum() {
        val expected = 42
        assertEquals(expected, testSample.sum(40, 2))
    }

    @Test
    fun testMul() {
        val expected = 42
        assertEquals(expected, testSample.mul(6, 7))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4, 5])
    fun testMulWithParameter(i: Int) {
        val expected = i * 7
        assertEquals(expected, testSample.mul(i, 7))
    }

    companion object {
        @JvmStatic
        fun mulTestData() = listOf(
            arrayOf(0, 7, 0),
            arrayOf(2, 7, 14),
            arrayOf(4, 7, 28),
            arrayOf(6, 7, 42)
        )
    }

    @ParameterizedTest
    @MethodSource("mulTestData")
    fun testMulWithMethodSource(a: Int, b: Int, expected: Int) {
        assertEquals(expected, testSample.mul(a, b))
    }
}