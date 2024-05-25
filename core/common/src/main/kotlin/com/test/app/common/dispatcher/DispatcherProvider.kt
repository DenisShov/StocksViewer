package com.test.app.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}
