package com.vanish.standard.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.collections.associateWith
import kotlin.collections.flatMap
import kotlin.collections.mapValues
import kotlin.collections.removeLastOrNull

@Serializable
sealed interface ExampleRoute : NavKey {
    @Serializable
    data object Top : ExampleRoute

    @Serializable
    data class ProductDetail(
        val baseProductCode: String?,
        val uuid: String = UUID.randomUUID().toString()
    ) : ExampleRoute

    @Serializable
    data class BrandPage(
        val labelId: Int,
        val uuid: String = UUID.randomUUID().toString()
    ) : ExampleRoute

    @Serializable
    data class SSSnapPlayDetail(
        val id: String,
        val uuid: String = UUID.randomUUID().toString()
    ) : ExampleRoute

    @Serializable
    data class SSSnapPlayList(
        val queryString: String = "",
        val uuid: String = UUID.randomUUID().toString()
    ) : ExampleRoute

    @Serializable
    data class SSStaffDetail(
        val id: String,
        val uuid: String = UUID.randomUUID().toString()
    ) : ExampleRoute

    @Serializable
    data class SSStaffList(
        val queryString: String = "",
        val uuid: String = UUID.randomUUID().toString()
    ) : ExampleRoute
}

/**
 * Create a navigation state that persists config changes and process death.
 */
@Composable
fun <T : NavKey> rememberNavigationState(
    startRoute: T,
    topLevelRoutes: Set<T>
): NavigationState<T> {
    val topLevelRoute =
        rememberSerializable(
            startRoute,
            topLevelRoutes,
            serializer = MutableStateSerializer(NavKeySerializer<T>()),
        ) {
            mutableStateOf(startRoute)
        }

    @Suppress("UNCHECKED_CAST")
    val backStacks =
        topLevelRoutes.associateWith { key ->
            rememberNavBackStack(key) as NavBackStack<T>
        }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks,
        )
    }
}

/**
 * State holder for navigation state.
 */
class NavigationState<T : NavKey>(
    val startRoute: T,
    topLevelRoute: MutableState<T>,
    val backStacks: Map<T, NavBackStack<T>>
) {
    var topLevelRoute: T by topLevelRoute
    val stacksInUse: List<T>
        get() =
            if (topLevelRoute == startRoute) {
                listOf(startRoute)
            } else {
                listOf(startRoute, topLevelRoute)
            }
}

/**
 * Convert NavigationState into NavEntries.
 */
@Composable
fun <T : NavKey> NavigationState<T>.toEntries(
    entryProvider: (T) -> NavEntry<T>
): SnapshotStateList<NavEntry<T>> {
    val decoratedEntries =
        backStacks.mapValues { (_, stack) ->
            val decorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator<T>(),
                )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
class Navigator<T : NavKey>(
    val state: NavigationState<T>
) {
    fun navigate(route: T) {
        if (route in state.backStacks.keys) {
            // This is a top level route, just switch to it.
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack =
            state.backStacks[state.topLevelRoute]
                ?: error("Stack for ${state.topLevelRoute} not found")

        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else if (state.topLevelRoute != state.startRoute) {
            state.topLevelRoute = state.startRoute
        }
    }
}
