package com.vanish.standard.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanish.standard.example.LocalExampleViewModel
import com.vanish.standard.example.ui.enum.FavoriteTab
import com.vanish.standard.staffstart.app.domain.model.SnapPlayFilterParams
import com.vanish.standard.staffstart.app.view.StaffStartFavoriteSnapPlayListScreen
import com.vanish.standard.staffstart.app.view.StaffStartFavoriteStaffListScreen
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayListScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffListScreen

@Composable
fun FavoriteView(
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(FavoriteTab.COORDINATE) }

    val navController = rememberNavController()

    val exampleViewModel = LocalExampleViewModel.current

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = PageType.Top.path,
    ) {
        composable(PageType.Top.path) {
            Column(modifier = Modifier.fillMaxSize()) {
                TabRow(selectedTabIndex = selectedTab.ordinal) {
                    FavoriteTab.entries.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab.ordinal == index,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title) },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    FavoriteTab.COORDINATE ->
                        CoordinateView(
                            onTapSnapPlay = { snapPlayId ->
                                navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                            },
                            onFavoriteAttemptWithoutLogin = {
                                // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                                exampleViewModel.showNeedLoginAlert()
                            },
                        )

                    FavoriteTab.STAFF ->
                        StaffView(
                            onTapStaff = { staffId ->
                                navController.navigate(PageType.SSStaffDetail.withArgs(staffId.toString()))
                            },
                            onFavoriteAttemptWithoutLogin = {
                                // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                                exampleViewModel.showNeedLoginAlert()
                            },
                        )
                }
            }
        }

        composable(PageType.ProductDetail.path) { backStackEntry ->
            val baseProductCode = backStackEntry.arguments?.getString("baseProductCode")?.takeUnless { it == NO_VALUE_PARAM }
            ProductScreen(
                baseProductCode,
                onTapSnapPlayDetail = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                },
                onTapReadMore = { baseProductCode ->
                    val path =
                        baseProductCode?.let {
                            PageType.SSSnapPlayList.withQueryArgs(mapOf("baseProductCode" to it))
                        } ?: PageType.SSSnapPlayList.path
                    navController.navigate(path)
                },
            )
        }

        composable(PageType.SSSnapPlayList.path) { backStackEntry ->
            val snapPlayFilterParams = SnapPlayFilterParams.from(backStackEntry)

            StaffStartSnapPlayListScreen(
                snapPlayFilterParams,
                onTapSnapPlay = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                },
                onFavoriteAttemptWithoutLogin = {
                    // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                    exampleViewModel.showNeedLoginAlert()
                },
            )
        }

        composable(PageType.SSStaffList.path) { backStackEntry ->
            StaffStartStaffListScreen(
                onTapStaff = { staffId ->
                    navController.navigate(PageType.SSStaffDetail.withArgs(staffId.toString()))
                },
                onFavoriteAttemptWithoutLogin = {
                    // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                    exampleViewModel.showNeedLoginAlert()
                },
            )
        }

        composable(PageType.SSStaffDetail.path) { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("id")
            if (staffId != null) {
                StaffStartStaffDetailScreen(
                    staffId,
                    onTapSnapPlay = { snapPlayId ->
                        navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                    },
                    onTapSnapPlayFilter = {
                        navController.navigate(PageType.SSSnapPlayList.withQueryArgs(it.toMap()))
                    },
                    onFavoriteAttemptWithoutLogin = {
                        // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
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
                        // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                        exampleViewModel.showNeedLoginAlert()
                    },
                )
            }
        }
    }
}

@Composable
fun CoordinateView(
    onTapSnapPlay: (Int) -> Unit,
    onFavoriteAttemptWithoutLogin: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // TODO I/O追加はhttps://v-standard.atlassian.net/browse/SS-13397でやる
        StaffStartFavoriteSnapPlayListScreen(
            onTapSnapPlay,
            onFavoriteAttemptWithoutLogin,
        )
    }
}

@Composable
fun StaffView(
    onTapStaff: (Int) -> Unit,
    onFavoriteAttemptWithoutLogin: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // TODO I/O追加はhttps://v-standard.atlassian.net/browse/SS-13397でやる
        StaffStartFavoriteStaffListScreen(
            onTapStaff,
            onFavoriteAttemptWithoutLogin,
        )
    }
}
