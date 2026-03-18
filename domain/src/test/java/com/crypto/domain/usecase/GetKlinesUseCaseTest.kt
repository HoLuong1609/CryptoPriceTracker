package com.crypto.domain.usecase

import com.crypto.core.result.AppResult
import com.crypto.domain.model.Kline
import com.crypto.domain.repository.KlineRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetKlinesUseCaseTest {

    @Mock
    private lateinit var klineRepository: KlineRepository

    private lateinit var getKlinesUseCase: GetKlinesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getKlinesUseCase = GetKlinesUseCase(klineRepository)
    }

    @Test
    fun invoke_WithValidParams_ReturnsSuccessFlow() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L

        val mockKlines = listOf(
            Kline(
                symbol = symbol,
                interval = interval,
                openTime = startTime,
                openPrice = 43000.0,
                highPrice = 44000.0,
                lowPrice = 42500.0,
                closePrice = 43500.0,
                volume = 1000.0,
                closeTime = startTime + 3600000
            ),
            Kline(
                symbol = symbol,
                interval = interval,
                openTime = startTime + 3600000,
                openPrice = 43500.0,
                highPrice = 44500.0,
                lowPrice = 43000.0,
                closePrice = 44000.0,
                volume = 1200.0,
                closeTime = startTime + 7200000
            )
        )

        val successFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Success(mockKlines))
        }

        whenever(
            klineRepository.getKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(successFlow)

        // Act
        val result = getKlinesUseCase.invoke(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        assertEquals(2, result.size)
        assertEquals(AppResult.Loading, result[0])
        assert(result[1] is AppResult.Success)
        assertEquals(mockKlines, (result[1] as AppResult.Success).data)

        // Verify
        verify(klineRepository).getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        )
    }

    @Test
    fun invoke_WithRepositoryError_ReturnsErrorFlow() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L
        val errorMessage = "Network error"

        val errorFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Error(message = errorMessage))
        }

        whenever(
            klineRepository.getKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(errorFlow)

        // Act
        val result = getKlinesUseCase.invoke(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        assertEquals(2, result.size)
        assertEquals(AppResult.Loading, result[0])
        assert(result[1] is AppResult.Error)
        assertEquals(errorMessage, (result[1] as AppResult.Error).message)
    }

    @Test
    fun invoke_WithEmptyResult_ReturnsSuccessWithEmptyList() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L

        val emptyFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Success(emptyList<Kline>()))
        }

        whenever(
            klineRepository.getKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(emptyFlow)

        // Act
        val result = getKlinesUseCase.invoke(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        assertEquals(2, result.size)
        assert(result[1] is AppResult.Success)
        assertEquals(emptyList<Kline>(), (result[1] as AppResult.Success).data)
    }
}

