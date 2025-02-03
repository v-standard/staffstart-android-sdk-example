package com.vanish.standard.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanish.standard.example.ui.enum.TabItem
import com.vanish.standard.staffstart.app.domain.model.SnapPlayFilterParams
import com.vanish.standard.staffstart.app.view.Scene
import com.vanish.standard.staffstart.app.view.StaffStartUI
import com.vanish.standard.staffstart.app.view.StaffStartUIConfiguration
import com.vanish.standard.staffstart.tracking.StaffStartTracking
import com.vanish.standard.staffstart.core.domain.enum.ContentType
import com.vanish.standard.staffstart.tracking.domain.model.PageViewParams
import kotlinx.coroutines.launch

@Composable
fun TabView(modifier: Modifier = Modifier) {
    val tabs = listOf(TabItem.PRODUCT, TabItem.SNAP_PLAY, TabItem.STAFF)
    var selectedTab by remember { mutableStateOf(TabItem.PRODUCT) }
    var baseProductCode by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    StaffStartUI.Configure(
        StaffStartUIConfiguration(
            onTapProductItem = { productCode ->
                baseProductCode = productCode
                selectedTab = TabItem.PRODUCT
            },
            onShowCoordinateDetail = { snapPlayId ->
                coroutineScope.launch {
                    StaffStartTracking.trackPageView(
                        PageViewParams(
                            contentId = snapPlayId,
                            userId = null,
                            contentType = ContentType.COORDINATE
                        )
                    )
                }
            }
        )
    )

    Box(
        modifier.fillMaxSize()
    ) {
        Column {
            TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.displayName) }
                    )
                }
            }

            when (selectedTab) {
                TabItem.PRODUCT -> {
                    val navController = StaffStartUI.getNavController(Scene.SNAP_PLAY)
                    ProductTab(
                        baseProductId = baseProductCode,
                        onNavigateToSnapPlayDetail = { snapPlayID ->
                            selectedTab = TabItem.SNAP_PLAY // タブを切り替え
                            StaffStartUI.navigateToSnapPlayDetail(navController, snapPlayID)
                        },
                        onNavigateToSnapPlayList = {
                            selectedTab = TabItem.SNAP_PLAY
                            StaffStartUI.navigateToSnapPlayList(
                                navController,
                                snapPlayFilterParams = SnapPlayFilterParams(baseProductCode = baseProductCode)
                            )
                        }
                    )
                }

                TabItem.SNAP_PLAY -> {
                    CoordinateTab().also {
                        // CV計測タグの例
//                        coroutineScope.launch(Dispatchers.IO) {
//                            StaffStartTracking.trackPurchase(
//                                PurchaseParams(
//                                    userId = "12345",
//                                    orderId = "54321",
//                                    productInfo = listOf(
//                                        ProductInfo(
//                                            sku = "12345",
//                                            price = 1000,
//                                            count = 1
//                                        )
//                                    )
//                                )
//                            )
//                        }

                        // カート計測タグの例
//                        coroutineScope.launch(Dispatchers.IO) {
//                            StaffStartTracking.trackAddToCart(
//                                AddToCartParams(
//                                    sku = "12345",
//                                    count = 1
//                                )
//                            )
//                        }
                    }
                }

                TabItem.STAFF -> {
                    StaffTab()
                }
            }
        }
    }
}

@Composable
fun ProductTab(
    baseProductId: String?,
    onNavigateToSnapPlayDetail: (String) -> Unit,
    onNavigateToSnapPlayList: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "商品情報を表示する画面です。",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            // SnapPlayBlockパーツを使って商品情報を表示
            StaffStartUI.SnapPlayBlock(
                baseProductCode = baseProductId,
                onTapSnapPlayDetail = { snapPlayID ->
                    onNavigateToSnapPlayDetail(snapPlayID.toString())
                },
                onTapReadMore = { productCode ->
                    // TODO: productCodeを使ってSNAP PLAY一覧に遷移すること
                    onNavigateToSnapPlayList()
                }
            )
        }
    }
}

@Composable
fun CoordinateTab() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StaffStartUI.SnapPlayNavigation()
    }
}

// スタッフタブのコンテンツ
@Composable
fun StaffTab() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StaffStartUI.StaffNavigation()
    }
}

// プレビュー
@Preview(showBackground = true)
@Composable
fun TabScreenPreview() {
    TabView()
}
