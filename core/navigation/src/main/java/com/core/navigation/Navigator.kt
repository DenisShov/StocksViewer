package com.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events by updating the [NavigationState].
 * Supports tab-aware navigation with independent back stacks per top-level route.
 */
class Navigator(val state: NavigationState) {

    /**
     * Navigate to a navigation key.
     * If the key is a top-level route, switches to that tab.
     * If the key is the current top-level route, clears its sub-stack.
     * Otherwise, adds the key to the current tab's back stack.
     */
    fun navigate(key: NavKey) {
        when (key) {
            state.topLevelRoute -> clearSubStack()
            in state.backStacks.keys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    /**
     * Go back to the previous navigation key.
     */
    fun onBackClick() {
        when (state.currentKey) {
            state.startRoute -> error("Cannot go back from the start route")
            state.topLevelRoute -> {
                // At the root of the current sub-stack, go back to the previous top-level stack.
                state.topLevelStack.removeLastOrNull()
            }
            else -> state.currentSubStack.removeLastOrNull()
        }
    }

    /**
     * Whether the system back press should be handled by the navigator.
     * Returns true if we're not at the root of the start tab.
     */
    fun canGoBack(): Boolean {
        return state.currentKey != state.startRoute
    }

    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startRoute) {
                clear()
            } else {
                remove(key)
            }
            add(key)
        }
    }

    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
