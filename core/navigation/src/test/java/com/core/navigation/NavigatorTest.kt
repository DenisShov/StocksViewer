package com.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class NavigatorTest {

    private val backStack: NavBackStack<NavKey> = mockk(relaxed = true)
    private lateinit var navigator: Navigator

    @Before
    fun setUp() {
        navigator = Navigator(backStack)
    }

    @Test
    fun navigate_adds_key_to_backStack() {
        val key = mockk<NavKey>()

        navigator.navigate(key)

        verify { backStack.add(key) }
    }

    @Test
    fun onBackClick_removes_last_entry_when_backStack_has_more_than_one_item() {
        every { backStack.size } returns 3
        every { backStack.lastIndex } returns 2

        navigator.onBackClick()

        verify { backStack.removeAt(2) }
    }

    @Test
    fun onBackClick_does_nothing_when_backStack_has_exactly_one_item() {
        every { backStack.size } returns 1

        navigator.onBackClick()

        verify(exactly = 0) { backStack.removeAt(any()) }
    }

    @Test
    fun onBackClick_does_nothing_when_backStack_is_empty() {
        every { backStack.size } returns 0

        navigator.onBackClick()

        verify(exactly = 0) { backStack.removeAt(any()) }
    }

}
