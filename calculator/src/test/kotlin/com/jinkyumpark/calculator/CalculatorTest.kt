package com.jinkyumpark.calculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CalculatorTest {

    private var cal = Calculator()

    @BeforeEach
    fun setup() {
        cal = Calculator()
    }

    @Test
    fun add() {
        assertEquals(9, cal.add(6, 3))
    }

    @Test
    fun subtract() {
        assertEquals(3, cal.subtract(6, 3))
    }
}
