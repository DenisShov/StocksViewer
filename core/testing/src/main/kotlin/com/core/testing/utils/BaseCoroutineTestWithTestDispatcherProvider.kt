package com.core.testing.utils

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule

abstract class BaseCoroutineTestWithTestDispatcherProvider(
    dispatcher: TestDispatcher = StandardTestDispatcher(),
) {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher = dispatcher)

}
