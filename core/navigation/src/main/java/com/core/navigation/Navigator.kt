package com.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Navigator(private val backStack: NavBackStack<NavKey>) {

    fun navigate(key: NavKey) {
        backStack.add(key)
    }

    fun onBackClick() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

}
