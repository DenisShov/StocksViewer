package com.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * Create a navigation state that persists across configuration changes and process death.
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
): NavigationState {
    val topLevelStack = rememberNavBackStack(startRoute)
    val backStacks = topLevelRoutes.associateWith { key -> rememberNavBackStack(key) }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelStack = topLevelStack,
            backStacks = backStacks,
        )
    }
}

/**
 * State holder for tab-aware navigation with independent back stacks per tab.
 *
 * @param startRoute the starting navigation key. The user will exit the app through this key.
 * @param topLevelStack the top level back stack holding only top-level keys.
 * @param backStacks a back stack per top-level route.
 */
class NavigationState(
    val startRoute: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    /** The currently active top-level route. */
    val topLevelRoute: NavKey by derivedStateOf { topLevelStack.last() }

    /** The current sub-stack for the active top-level route. */
    val currentSubStack: NavBackStack<NavKey>
        get() = backStacks[topLevelRoute]
            ?: error("Sub stack for $topLevelRoute does not exist")

    /** The key at the top of the current sub-stack. */
    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}

/**
 * Convert NavigationState into decorated NavEntries suitable for NavDisplay.
 */
@Composable
fun NavigationState.toDecoratedEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
