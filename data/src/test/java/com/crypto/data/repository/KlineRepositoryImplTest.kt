package com.crypto.data.repository

import com.crypto.core.result.AppResult
import com.crypto.data.remote.datasource.KlineRemoteDataSource
import com.crypto.data.remote.dto.KlineResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class KlineRepositoryImplTest {

    @Mock
    private lateinit var klineRemoteDataSource: KlineRemoteDataSource

    private lateinit var klineRepository: KlineRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        klineRepository = KlineRepositoryImpl(klineRemoteDataSource)
    }

    @Test
    fun getKlines_WithSuccessResult_MapsAndReturnsFlow() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L

        val mockKlineResponses = listOf(
            KlineResponse(
                symbol = symbol,
                interval = interval,
                openingTime = startTime,
                openingPrice = "43000.0",
                highestPrice = "44000.0",
                lowestPrice = "42500.0",
                closingPrice = "43500.0",
                volume = "1000.0",
                closingTime = startTime + 3600000
            ),
            KlineResponse(
                symbol = symbol,
                interval = interval,
                openingTime = startTime + 3600000,
                openingPrice = "43500.0",
                highestPrice = "44500.0",
                lowestPrice = "43000.0",
                closingPrice = "44000.0",
                volume = "1200.0",
                closingTime = startTime + 7200000
            )
        )

        val remoteFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Success(mockKlineResponses))
        }

        whenever(
            klineRemoteDataSource.fetchKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(remoteFlow)

        // Act
        val result = klineRepository.getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        assertEquals(2, result.size)
        assertEquals(AppResult.Loading, result[0])
        assert(result[1] is AppResult.Success)

        val successResult = result[1] as AppResult.Success
        assertEquals(2, successResult.data.size)
        assertEquals(symbol, successResult.data[0].symbol)
        assertEquals(43500.0, successResult.data[0].closePrice)

        // Verify
        verify(klineRemoteDataSource).fetchKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        )
    }

    @Test
    fun getKlines_WithErrorResult_ReturnsErrorFlow() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L
        val errorMessage = "Network error"

        val remoteFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Error(message = errorMessage))
        }

        whenever(
            klineRemoteDataSource.fetchKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(remoteFlow)

        // Act
        val result = klineRepository.getKlines(
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

        // Verify
        verify(klineRemoteDataSource).fetchKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        )
    }

    @Test
    fun getKlines_WithEmptyResult_ReturnsSuccessWithEmptyList() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L

        val remoteFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Success(emptyList<KlineResponse>()))
        }

        whenever(
            klineRemoteDataSource.fetchKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(remoteFlow)

        // Act
        val result = klineRepository.getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        assertEquals(2, result.size)
        assert(result[1] is AppResult.Success)
        assertEquals(0, (result[1] as AppResult.Success).data.size)

        // Verify
        verify(klineRemoteDataSource).fetchKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        )
    }

    @Test
    fun getKlines_MapsKlineResponseToDomainModel_Correctly() = runTest {
        // Arrange
        val symbol = "ETHUSDT"
        val interval = "4h"
        val startTime = 1700000000L
        val endTime = 1700100000L

        val mockKlineResponse = KlineResponse(
            symbol = symbol,
            interval = interval,
            openingTime = startTime,
            openingPrice = "2500.5",
            highestPrice = "2600.0",
            lowestPrice = "2450.0",
            closingPrice = "2550.5",
            volume = "5000.0",
            closingTime = startTime + 14400000
        )

        val remoteFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Success(listOf(mockKlineResponse)))
        }

        whenever(
            klineRemoteDataSource.fetchKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(remoteFlow)

        // Act
        val result = klineRepository.getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        val successResult = result[1] as AppResult.Success
        val kline = successResult.data[0]

        assertEquals(symbol, kline.symbol)
        assertEquals(interval, kline.interval)
        assertEquals(startTime, kline.openTime)
        assertEquals(2500.5, kline.openPrice)
        assertEquals(2600.0, kline.highPrice)
        assertEquals(2450.0, kline.lowPrice)
        assertEquals(2550.5, kline.closePrice)
        assertEquals(5000.0, kline.volume)
        assertEquals(startTime + 14400000, kline.closeTime)
    }

    @Test
    fun getKlines_MultipleLoadingStates_AllEmitted() = runTest {
        // Arrange
        val symbol = "BTCUSDT"
        val interval = "1h"
        val startTime = 1700000000L
        val endTime = 1700100000L

        val remoteFlow = flow {
            emit(AppResult.Loading)
            emit(AppResult.Loading)
            emit(AppResult.Success(emptyList<KlineResponse>()))
        }

        whenever(
            klineRemoteDataSource.fetchKlines(
                symbol = symbol,
                interval = interval,
                startTime = startTime,
                endTime = endTime
            )
        ).thenReturn(remoteFlow)

        // Act
        val result = klineRepository.getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        ).toList()

        // Assert
        assertEquals(3, result.size)
        assertEquals(AppResult.Loading, result[0])
        assertEquals(AppResult.Loading, result[1])
        assert(result[2] is AppResult.Success)
    }
}

