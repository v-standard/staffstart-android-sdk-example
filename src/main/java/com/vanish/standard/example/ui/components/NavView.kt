package com.vanish.standard.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vanish.standard.staffstart.app.domain.model.SnapPlayFilterParams
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartSnapPlayListScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffDetailScreen
import com.vanish.standard.staffstart.app.view.StaffStartStaffListScreen
import com.vanish.standard.staffstart.app.view.StaffStartUI
import com.vanish.standard.staffstart.app.view.StaffStartUIConfiguration
import com.vanish.standard.staffstart.app.view.presentation.component.blocks.StaffStartBaseProductSnapPlaysBlock
import com.vanish.standard.staffstart.core.domain.enum.ContentType
import com.vanish.standard.staffstart.core.framework.util.PlatformLogger
import com.vanish.standard.staffstart.tracking.StaffStartTracking
import com.vanish.standard.staffstart.tracking.domain.model.PageViewParams
import kotlinx.coroutines.launch

private const val NO_VALUE_PARAM = "NO_VALUE_PARAM"
private val PLACEHOLDER_REGEX = Regex("\\{[^}]+\\}")

enum class PageType(
    val path: String
) {
    Top("Top"),
    ProductDetail("ProductDetail/{baseProductCode}"),
    SSSnapPlayDetail("SSSnapPlayDetail/{id}"),
    SSSnapPlayList("SSSnapPlayList"),
    SSStaffDetail("SSStaffDetail/{id}"),
    SSStaffList("SSStaffList");

    fun withArgs(vararg args: String?): String {
        var result = path
        args.forEach { arg ->
            result = result.replaceFirst(PLACEHOLDER_REGEX, arg ?: NO_VALUE_PARAM)
        }
        return result
    }

    fun withQueryArgs(query: Map<String, Any>): String {
        // クエリパラメータが空の場合は元のURLを返す
        if (query.isEmpty()) return path

        // クエリパラメータを文字列に変換
        val queryString =
            query.entries.joinToString("&") { (key, value) ->
                "$key=$value"
            }

        // URLにクエリパラメータを追加
        return if (path.contains("?")) {
            "$path&$queryString"
        } else {
            "$path?$queryString"
        }
    }
}

@Composable
fun NavView(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

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
            })
        }

        composable(PageType.SSStaffList.path) { backStackEntry ->
            StaffStartStaffListScreen(onTapStaff = { staffId ->
                navController.navigate(PageType.SSStaffDetail.withArgs(staffId.toString()))
            })
        }

        composable(PageType.SSStaffDetail.path) { backStackEntry ->
            val staffId = backStackEntry.arguments?.getString("id")
            if (staffId != null) {
                StaffStartStaffDetailScreen(staffId, onTapSnapPlay = { snapPlayId ->
                    navController.navigate(PageType.SSSnapPlayDetail.withArgs(snapPlayId.toString()))
                }, onTapSnapPlayFilter = {
                    navController.navigate(PageType.SSSnapPlayList.withQueryArgs(it.toMap()))
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
                )
            }
        }
    }
}

/*
以下、SDK利用者様側の画面実装
 */

@Composable
private fun TopScreen(
    onClickToDetailPage: () -> Unit,
    onClickToSnapPlayListPage: () -> Unit,
    onClickToStaffListPage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            Text("トップページ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(onClickToDetailPage) { Text("商品詳細画面") }
            Button(onClickToSnapPlayListPage) { Text("SnapPlay一覧画面") }
            Button(onClickToStaffListPage) { Text("Staff一覧画面") }
        }
    }
}

@Composable
private fun ProductScreen(
    baseProductCode: String? = null,
    onTapSnapPlayDetail: (snapPlayId: Int) -> Unit,
    onTapReadMore: (baseProductId: String?) -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("ここに御社の商品情報が入ります")
        Text("白スウェット ヘビーウェイト")
        Text("とてもおしゃれなスウェットです。.....")
        Spacer(modifier = Modifier.height(40.dp))
        StaffStartBaseProductSnapPlaysBlock(
            baseProductCode = baseProductCode,
            onTapSnapPlayDetail = onTapSnapPlayDetail,
            onTapReadMore = onTapReadMore,
        )
    }
}
