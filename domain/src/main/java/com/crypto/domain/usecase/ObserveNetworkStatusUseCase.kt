package com.crypto.domain.usecase

import com.crypto.domain.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow

class ObserveNetworkStatusUseCase(
    private val networkMonitor: NetworkMonitor
) {
    operator fun invoke(): Flow<Boolean> = networkMonitor.isOnline
}