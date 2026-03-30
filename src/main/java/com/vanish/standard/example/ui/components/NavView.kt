package com.vanish.standard.example.ui.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.vanish.standard.example.ExampleViewModel
import com.vanish.standard.example.LocalExampleViewModel
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayListScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffListScreen
import com.vanish.standard.staffstart.app.view.StaffStartUI
import com.vanish.standard.staffstart.app.view.StaffStartUIConfiguration
import com.vanish.standard.staffstart.app.viewmodel.SnapPlaySearchConditionRouteParams
import com.vanish.standard.staffstart.app.viewmodel.StaffSearchConditionRouteParams
import com.vanish.standard.staffstart.core.domain.enum.ContentType
import com.vanish.standard.staffstart.core.framework.util.PlatformLogger
import com.vanish.standard.staffstart.tracking.StaffStartTracking
import com.vanish.standard.staffstart.tracking.domain.model.PageViewParams
import kotlinx.coroutines.launch

@Composable
fun NavView(
    modifier: Modifier = Modifier,
    useDarkTheme: Boolean = false,
    useNavigation3: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    StaffStartUI.Configure(
        StaffStartUIConfiguration(
            onTapProductItem = { productCode ->
                PlatformLogger.d("NavView.onTapProductItem", productCode)
            },
            onShowCoordinateDetail = { snapPlayId ->
                PlatformLogger.d("NavView.onShowCoordinateDetail", snapPlayId.toString())

                coroutineScope.launch {
                    StaffStartTracking.trackPageView(
                        PageViewParams(
                            contentId = snapPlayId,
                            userId = null,
                            contentType = ContentType.COORDINATE,
                        ),
                    )
                }
            },
        ),
    )

    if (useNavigation3) {
        Navigation3View(
            modifier = modifier,
            useDarkTheme = useDarkTheme,
        )
    } else {
        NavHostView(
            modifier = modifier,
            useDarkTheme = useDarkTheme,
        )
    }
}

@Composable
private fun Navigation3View(
    modifier: Modifier = Modifier,
    useDarkTheme: Boolean = false
) {
    val exampleViewModel = LocalExampleViewModel.current
    val navigationState =
        rememberNavigationState<ExampleRoute>(
            startRoute = ExampleRoute.Top,
            topLevelRoutes = setOf(ExampleRoute.Top),
        )
    val navigator = remember { Navigator(navigationState) }

    val entryProvider =
        entryProvider<ExampleRoute> {
            exampleGraph(navigator, useDarkTheme, exampleViewModel)
        }

    NavDisplay(
        modifier = modifier.fillMaxSize(),
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        transitionSpec = {
            (slideInHorizontally { it } + fadeIn()) togetherWith
                    (slideOutHorizontally { -it / 4 } + fadeOut())
        },
        popTransitionSpec = {
            (slideInHorizontally { -it / 4 } + fadeIn()) togetherWith
                    (slideOutHorizontally { it } + fadeOut())
        },
        predictivePopTransitionSpec = { _ ->
            (slideInHorizontally { -it / 4 } + fadeIn()) togetherWith
                    (slideOutHorizontally { it } + fadeOut())
        },
    )
}

private fun EntryProviderScope<ExampleRoute>.exampleGraph(
    navigator: Navigator<ExampleRoute>,
    useDarkTheme: Boolean,
    exampleViewModel: ExampleViewModel
) {
    entry<ExampleRoute.Top> {
        TopScreen(
            onClickToDetailPage = {
                val baseProductCode = "1612804606-0000"
                navigator.navigate(ExampleRoute.ProductDetail(baseProductCode))
            },
            onClickToStaffListPage = {
                navigator.navigate(ExampleRoute.SSStaffList())
            },
            onClickToSnapPlayListPage = {
                navigator.navigate(ExampleRoute.SSSnapPlayList())
            },
            onClickBrandPage = {
                val labelId = 152 // 3Coins
                navigator.navigate(ExampleRoute.BrandPage(labelId))
            },
        )
    }

    entry<ExampleRoute.ProductDetail> { key ->
        ProductScreen(
            useDarkTheme = useDarkTheme,
            baseProductCode = key.baseProductCode,
            onTapSnapPlayDetail = { snapPlayId ->
                navigator.navigate(ExampleRoute.SSSnapPlayDetail(snapPlayId.toString()))
            },
            onTapReadMore = { baseProductCode ->
                val params = SnapPlaySearchConditionRouteParams(baseProductCode = baseProductCode)
                navigator.navigate(ExampleRoute.SSSnapPlayList(params.toQueryString()))
            },
            onFavoriteAttemptWithoutLogin = {
                exampleViewModel.showNeedLoginAlert()
            },
        )
    }

    entry<ExampleRoute.BrandPage> { key ->
        BrandScreen(
            key.labelId,
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = { snapPlayId ->
                navigator.navigate(ExampleRoute.SSSnapPlayDetail(snapPlayId.toString()))
            },
            onTapReadMoreSnapPlay = { brandSnapPlaysBlockCondition ->
                val params = brandSnapPlaysBlockCondition.toSnapPlaySearchConditionRouteParams()
                navigator.navigate(ExampleRoute.SSSnapPlayList(params.toQueryString()))
            },
            onTapStaff = { userId ->
                navigator.navigate(ExampleRoute.SSStaffDetail(userId.toString()))
            },
            onTapReadMoreStaff = { brandStaffsBlockCondition ->
                val params = brandStaffsBlockCondition.toStaffSearchConditionRouteParams()
                navigator.navigate(ExampleRoute.SSStaffList(params.toQueryString()))
            },
        )
    }

    entry<ExampleRoute.SSSnapPlayList> { key ->
        val params = SnapPlaySearchConditionRouteParams.fromQueryString(key.queryString)
        StaffStartSnapPlayListScreen(
            params,
            useDarkTheme,
            onTapSnapPlay = { snapPlayId ->
                navigator.navigate(ExampleRoute.SSSnapPlayDetail(snapPlayId.toString()))
            },
            onFavoriteAttemptWithoutLogin = {
                exampleViewModel.showNeedLoginAlert()
            },
        )
    }

    entry<ExampleRoute.SSStaffList> { key ->
        val params = StaffSearchConditionRouteParams.fromQueryString(key.queryString)
        StaffStartStaffListScreen(
            params,
            useDarkTheme,
            onTapStaff = { staffId ->
                navigator.navigate(ExampleRoute.SSStaffDetail(staffId.toString()))
            },
            onFavoriteAttemptWithoutLogin = {
                exampleViewModel.showNeedLoginAlert()
            },
        )
    }

    entry<ExampleRoute.SSStaffDetail> { key ->
        StaffStartStaffDetailScreen(
            staffId = key.id,
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = { snapPlayId ->
                navigator.navigate(ExampleRoute.SSSnapPlayDetail(snapPlayId.toString()))
            },
            onTapSnapPlayFilter = {
                val params = it.toSnapPlaySearchConditionRouteParams()
                navigator.navigate(ExampleRoute.SSSnapPlayList(params.toQueryString()))
            },
            onFavoriteAttemptWithoutLogin = {
                exampleViewModel.showNeedLoginAlert()
            },
        )
    }

    entry<ExampleRoute.SSSnapPlayDetail> { key ->
        StaffStartSnapPlayDetailScreen(
            snapPlayId = key.id,
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = {
                navigator.navigate(ExampleRoute.SSSnapPlayDetail(it.toString()))
            },
            onTapSnapPlayFilter = {
                val params = it.toSnapPlaySearchConditionRouteParams()
                navigator.navigate(ExampleRoute.SSSnapPlayList(params.toQueryString()))
            },
            onTapProductItem = {
                navigator.navigate(ExampleRoute.ProductDetail(it))
            },
            onTapStaff = {
                navigator.navigate(ExampleRoute.SSStaffDetail(it.toString()))
            },
            onTapSnapPlayNotFoundBack = {
                navigator.goBack()
            },
            onFavoriteAttemptWithoutLogin = {
                exampleViewModel.showNeedLoginAlert()
            },
        )
    }
}

@Composable
private fun NavHostView(
    modifier: Modifier = Modifier,
    useDarkTheme: Boolean = false
) {
    val navController = rememberNavController()
    val exampleViewModel = LocalExampleViewModel.current

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = PageType.Top.path,
    ) {
        composable(PageType.Top.path) {
            TopScreen(onClickToDetailPage = {
                val baseProductCode = "1612804606-0000" // or null. nullであれば絞り込まない
                navController.navigate(PageType.ProductDetail.withArgs(baseProductCode))
            }, onClickToStaffListPage = {
                navController.navigate(PageType.SSStaffList.path)
            }, onClickToSnapPlayListPage = {
                navController.navigate(PageType.SSSnapPlayList.path)
            }, onClickBrandPage = {
                val labelId = "152" // 3Coins
                navController.navigate(PageType.BrandPage.withArgs(labelId))
            })
        }

        composable(PageType.ProductDetail.path) { backStackEntry ->
            val baseProductCode = backStackEntry.arguments?.getString("baseProductCode")?.takeUnless { it == NO_VALUE_PARAM }
            ProductScreen(
                useDarkTheme = useDarkTheme,
                baseProductCode = baseProductCode,
                onTapSnapPlayDetail = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                },
                onTapReadMore = { baseProductCode ->
                    val snapPlaySearchConditionRouteParams = SnapPlaySearchConditionRouteParams(baseProductCode = baseProductCode)
                    val path = PageType.SSSnapPlayList.withQueryString(snapPlaySearchConditionRouteParams.toQueryString())
                    navController.navigate(path)
                },
                onFavoriteAttemptWithoutLogin = {
                    // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                    exampleViewModel.showNeedLoginAlert()
                },
            )
        }

        composable(PageType.BrandPage.path) { backStackEntry ->
            val labelId = backStackEntry.arguments?.getString("labelId")?.toIntOrNull() ?: return@composable Text("Not Found labelId")

            BrandScreen(
                labelId,
                useDarkTheme = useDarkTheme,
                onTapSnapPlay = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                },
                onTapReadMoreSnapPlay = { brandSnapPlaysBlockCondition ->
                    val snapPlaySearchConditionRouteParams = brandSnapPlaysBlockCondition.toSnapPlaySearchConditionRouteParams()
                    navController.navigate(PageType.SSSnapPlayList.withQueryString(snapPlaySearchConditionRouteParams.toQueryString()))
                },
                onTapStaff = { userId ->
                    navController.navigate(PageType.SSStaffDetail.withArgs(userId.toString()))
                },
                onTapReadMoreStaff = { brandStaffsBlockCondition ->
                    val staffSearchConditionRouteParams = brandStaffsBlockCondition.toStaffSearchConditionRouteParams()
                    navController.navigate(PageType.SSStaffList.withQueryString(staffSearchConditionRouteParams.toQueryString()))
                },
            )
        }

        composable(PageType.SSSnapPlayList.path) { backStackEntry ->
            val snapPlaySearchConditionRouteParams = SnapPlaySearchConditionRouteParams.fromBackStackEntry(backStackEntry)

            StaffStartSnapPlayListScreen(
                snapPlaySearchConditionRouteParams,
                useDarkTheme,
                onTapSnapPlay = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                },
                onFavoriteAttemptWithoutLogin = {
                    // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                    exampleViewModel.showNeedLoginAlert()
                },
            )
        }

        composable(PageType.SSStaffList.path) { backStackEntry ->
            val staffSearchConditionRouteParams = StaffSearchConditionRouteParams.fromBackStackEntry(backStackEntry)
            StaffStartStaffListScreen(
                staffSearchConditionRouteParams,
                useDarkTheme,
                onTapStaff = { staffId ->
                    navController.navigate(PageType.SSStaffDetail.withArgs(staffId.toString()))
                },
                onFavoriteAttemptWithoutLogin = {
                    // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                    exampleViewModel.showNeedLoginAlert()
                },
            )
        }

        composable(PageType.SSStaffDetail.path) { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("id")
            if (staffId != null) {
                StaffStartStaffDetailScreen(
                    staffId = staffId,
                    useDarkTheme = useDarkTheme,
                    onTapSnapPlay = { snapPlayId ->
                        navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                    },
                    onTapSnapPlayFilter = {
                        val snapPlaySearchConditionRouteParams = it.toSnapPlaySearchConditionRouteParams()
                        navController.navigate(PageType.SSSnapPlayList.withQueryString(snapPlaySearchConditionRouteParams.toQueryString()))
                    },
                    onFavoriteAttemptWithoutLogin = {
                        // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                        exampleViewModel.showNeedLoginAlert()
                    },
                )
            } else {
                Text("staffId not found")
            }
        }

        composable(PageType.SSSnapPlayDetail.path) { backStackEntry ->
            val snapPlayId = backStackEntry.arguments?.getString("id")
            snapPlayId?.let { snapPlayId ->
                StaffStartSnapPlayDetailScreen(
                    snapPlayId = snapPlayId,
                    useDarkTheme = useDarkTheme,
                    onTapSnapPlay = {
                        navController.navigate(PageType.SSSnapPlayDetail.withArgs(it.toString()))
                    },
                    onTapSnapPlayFilter = {
                        val snapPlaySearchConditionRouteParams = it.toSnapPlaySearchConditionRouteParams()
                        navController.navigate(PageType.SSSnapPlayList.withQueryString(snapPlaySearchConditionRouteParams.toQueryString()))
                    },
                    onTapProductItem = {
                        navController.navigate(PageType.ProductDetail.withArgs(it))
                    },
                    onTapStaff = {
                        navController.navigate(PageType.SSStaffDetail.withArgs(it.toString()))
                    },
                    onTapSnapPlayNotFoundBack = {
                        navController.popBackStack()
                    },
                    onFavoriteAttemptWithoutLogin = {
                        // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                        exampleViewModel.showNeedLoginAlert()
                    },
                )
            }
        }
    }
}
