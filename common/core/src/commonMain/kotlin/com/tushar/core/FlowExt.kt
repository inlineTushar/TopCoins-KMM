package com.tushar.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T, R> Flow<T>.zipWithNext(
    transform: suspend (previous: T?, current: T) -> R
): Flow<R> = flow {
    var previous: T? = null
    collect { current ->
        emit(transform(previous, current))
        previous = current
    }
}
