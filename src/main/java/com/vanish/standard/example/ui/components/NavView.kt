package com.vanish.standard.example.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanish.standard.example.LocalExampleViewModel
import com.vanish.standard.staffstart.app.domain.model.SnapPlayFilterParams
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayListScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffListScreen
import com.vanish.standard.staffstart.app.view.StaffStartUI
import com.vanish.standard.staffstart.app.view.StaffStartUIConfiguration
import com.vanish.standard.staffstart.core.domain.enum.ContentType
import com.vanish.standard.staffstart.core.framework.util.PlatformLogger
import com.vanish.standard.staffstart.tracking.StaffStartTracking
import com.vanish.standard.staffstart.tracking.domain.model.PageViewParams
import kotlinx.coroutines.launch

@Composable
fun NavView(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    val exampleViewModel = LocalExampleViewModel.current

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
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = PageType.Top.path,
    ) {
        composable(PageType.Top.path) {
            TopScreen(onClickToDetailPage = {
                val baseProductCode = "UC17S0051060200" // or null. nullであれば絞り込まない
                navController.navigate(PageType.ProductDetail.withArgs(baseProductCode))
            }, onClickToStaffListPage = {
                navController.navigate(PageType.SSStaffList.path)
            }, onClickToSnapPlayListPage = {
                navController.navigate(PageType.SSSnapPlayList.path)
            })
        }

        composable(PageType.ProductDetail.path) { backStackEntry ->
            val baseProductCode = backStackEntry.arguments?.getString("baseProductCode")?.takeUnless { it == NO_VALUE_PARAM }
            ProductScreen(baseProductCode, onTapSnapPlayDetail = { snapPlayId ->
                navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
            }, onTapReadMore = { baseProductCode ->
                val path =
                    baseProductCode?.let {
                        PageType.SSSnapPlayList.withQueryArgs(mapOf("baseProductCode" to it))
                    } ?: PageType.SSSnapPlayList.path
                navController.navigate(path)
            })
        }

        composable(PageType.SSSnapPlayList.path) { backStackEntry ->
            val snapPlayFilterParams = SnapPlayFilterParams.from(backStackEntry)

            StaffStartSnapPlayListScreen(snapPlayFilterParams, onTapSnapPlay = { snapPlayId ->
                navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
            }, onFavoriteAttemptWithoutLogin = {
                // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                exampleViewModel.showNeedLoginAlert()
            })
        }

        composable(PageType.SSStaffList.path) { backStackEntry ->
            StaffStartStaffListScreen(onTapStaff = { staffId ->
                navController.navigate(PageType.SSStaffDetail.withArgs(staffId.toString()))
            }, onFavoriteAttemptWithoutLogin = {
                // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                exampleViewModel.showNeedLoginAlert()
            })
        }

        composable(PageType.SSStaffDetail.path) { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("id")
            if (staffId != null) {
                StaffStartStaffDetailScreen(staffId, onTapSnapPlay = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                }, onTapSnapPlayFilter = {
                    navController.navigate(PageType.SSSnapPlayList.withQueryArgs(it.toMap()))
                }, onFavoriteAttemptWithoutLogin = {
                    // ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                    exampleViewModel.showNeedLoginAlert()
                })
            } else {
                Text("staffId not found")
            }
        }

        composable(PageType.SSSnapPlayDetail.path) { backStackEntry ->
            val snapPlayId = backStackEntry.arguments?.getString("id")
            snapPlayId?.let { snapPlayId ->
                StaffStartSnapPlayDetailScreen(
                    snapPlayId = snapPlayId,
                    onTapSnapPlay = {
                        navController.navigate(PageType.SSSnapPlayDetail.withArgs(it.toString()))
                    },
                    onTapSnapPlayFilter = {
                        navController.navigate(PageType.SSSnapPlayList.withQueryArgs(it.toMap()))
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
