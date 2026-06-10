package com.crypto.domain.extension

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.timeout
import kotlin.time.Duration

/**
 * Add backpressure buffer to Flow
 *
 * @param bufferSize Maximum number of items to buffer
 * @return Flow with buffer applied
 */
fun <T> Flow<T>.withBackpressure(
    bufferSize: Int = 100
): Flow<T> = this.buffer(bufferSize)

/**
 * Add timeout to Flow
 * Throws TimeoutCancellationException if no item emitted within timeout
 *
 * @param duration Timeout duration
 * @return Flow with timeout applied
 */
@OptIn(FlowPreview::class)
fun <T> Flow<T>.withTimeout(
    duration: Duration
): Flow<T> = this.timeout(duration)

/**
 * Combine backpressure + timeout for resilient streams
 * Useful for production environments to prevent memory leaks and hanging
 *
 * @param bufferSize Maximum number of items to buffer
 * @param timeout Timeout duration
 * @return Flow with both buffer and timeout applied
 */
fun <T> Flow<T>.withResilience(
    bufferSize: Int = 100,
    timeout: Duration
): Flow<T> = this
    .withBackpressure(bufferSize)
    .withTimeout(timeout)

