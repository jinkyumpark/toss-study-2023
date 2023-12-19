package com.jinkyumpark.calculator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import kotlin.test.assertEquals

class StringCalculatorTest {

    private val calculator = StringCalculator()

    @Test
    fun `콜론으로 정상적으로 분리되야 한다`() {
        assertEquals(calculator.add("1:2"), 3)
        assertEquals(calculator.add("2:3"), 5)
        assertEquals(calculator.add("10:2"), 12)
        assertEquals(calculator.add("100:0"), 100)
    }

    @Test
    fun `쉼표로 정상적으로 분리되야 한다`() {
        assertEquals(calculator.add("1,2"), 3)
        assertEquals(calculator.add("2,3"), 5)
        assertEquals(calculator.add("10,2"), 12)
        assertEquals(calculator.add("100,0"), 100)
    }

    // //와 \n 사이에 구분자 지정 가능
    @Test
    fun `원하는 구분자를 지정할 수 있어야 한다`() {
        assertEquals(calculator.add("1//%\n2"), 3)
        assertEquals(calculator.add("2//*\n3"), 5)
        assertEquals(calculator.add("10//(\n2"), 12)
        assertEquals(calculator.add("100//@\n0"), 100)
    }

    @Test
    fun `어느 한 문자열이 음수면 RuntimeException을 던져야 한다`() {
        assertThrows<RuntimeException> { calculator.add("-1:0") }
        assertThrows<RuntimeException> { calculator.add("0:-1") }
        assertThrows<RuntimeException> { calculator.add("-1:-1") }
        assertThrows<RuntimeException> { calculator.add("-100:0") }
        assertThrows<RuntimeException> { calculator.add("0:-100") }
    }

    @Test
    fun `text가 null이나 빈칸이면 0이 와야 한다`() {
        assertEquals(calculator.add(""), 0)
        assertEquals(calculator.add(null), 0)
    }

    @Test
    fun `숫자 1개를 문자열로 입력하면 해당 숫자를 반환해야 한다`() {
        assertEquals(calculator.add("1"), 1)
        assertEquals(calculator.add("10"), 10)
        assertEquals(calculator.add("100"), 100)
        assertEquals(calculator.add("123"), 123)
        assertEquals(calculator.add("0"), 0)
    }
}
