package com.bibangamba.simplecalculatorawamo.data.repository

import com.bibangamba.simplecalculatorawamo.data.database.CalculationDao
import com.bibangamba.simplecalculatorawamo.data.service.MathService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito


class CalculationRepositoryTest {

    var mathService = Mockito.mock(MathService::class.java)
    var dao = Mockito.mock(CalculationDao::class.java)
    val repo = CalculationRepository(mathService, dao)


    @Test
    fun given2IntsToDivide_shouldReturnDouble() {
        val result = repo.divide(13, 878)
        assertEquals(result, 0.015, 0.0)

    }

    @Test
    fun givenDouble_shouldReturnDoubleTo3Decimals() {
        val result = repo.return3FDouble(13.87786)
        assertEquals(result, 13.878, 0.0)
    }

    @Test
    fun getNumbersAndExpectedResult_Test() {
        val (num1, num2, expected) = repo.getNumbersAndExpectedResult("5 + 10")

        assertEquals(num1, 5)
        assertEquals(num2, 10)
        assertEquals(expected, 15.0, 0.0)
    }
}