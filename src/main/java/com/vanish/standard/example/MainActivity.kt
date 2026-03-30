package com.vanish.standard.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanish.standard.example.ui.components.ExampleAppHeader
import com.vanish.standard.example.ui.components.FavoriteView
import com.vanish.standard.example.ui.components.NavView
import com.vanish.standard.example.ui.components.TabView
import com.vanish.standard.example.ui.enum.MainTab
import com.vanish.standard.example.ui.theme.ExampleAppTheme
import com.vanish.standard.staffstart.core.framework.config.StaffStart
import com.vanish.standard.staffstart.core.framework.config.StaffStartConfiguration
import com.vanish.standard.staffstart.tracking.framework.config.tracking
import kotlinx.coroutines.launch

private const val UNIT_SCREEN_FLAG = true
private const val USE_NAVIGATION_3_FLAG = true

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeStaffStartSDK()
        enableEdgeToEdge()
        setContent {
//            val useDarkTheme = isSystemInDarkTheme() // デフォルト: 端末のダークモード設定に合わせる
//            val useDarkTheme = true // ダークモード固定
            val useDarkTheme = false // ライトモード 固定 (お使いのアプリがダークモードに対応していない時はこちらを設定してください

            val exampleViewModel: ExampleViewModel = viewModel()
            CompositionLocalProvider(LocalExampleViewModel provides exampleViewModel) {
                ExampleAppTheme(useDarkTheme = useDarkTheme) {
                    // ← 利用者様Appでお使いのテーマがある場合
                    var selectedTab by rememberSaveable { mutableStateOf(MainTab.SNAP) }

                    val isShowAlertLogin = exampleViewModel.isShowLoginAlert.collectAsState().value
                    if (isShowAlertLogin) {
                        AlertDialog(
                            onDismissRequest = { exampleViewModel.hideNeedLoginAlert() },
                            title = { Text("ログインしてください") },
                            text = { Text("お気に入り登録にはログインが必要です") },
                            confirmButton = {
                                TextButton(onClick = {
                                    // このAlertのまま使う場合は、ここにLoginへの導線を置いてください

                                    exampleViewModel.hideNeedLoginAlert()
                                }) {
                                    Text("OK")
                                }
                            },
                        )
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavigationBar {
                                MainTab.entries.forEach { tab ->
                                    NavigationBarItem(
                                        selected = tab == selectedTab,
                                        onClick = { selectedTab = tab },
                                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                                        label = { Text(tab.label) },
                                        alwaysShowLabel = true,
                                    )
                                }
                            }
                        },
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            ExampleAppHeader()
                            Box(modifier = Modifier.weight(1.0f)) {
                                when (selectedTab) {
                                    MainTab.SNAP ->
                                        when (UNIT_SCREEN_FLAG) {
                                            true -> NavView(useDarkTheme = useDarkTheme, useNavigation3 = USE_NAVIGATION_3_FLAG)
                                            false -> TabView(useDarkTheme = useDarkTheme)
                                        }

                                    MainTab.FAVORITE -> FavoriteView(useDarkTheme = useDarkTheme)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initializeStaffStartSDK() {
        val staffStartConfiguration =
            StaffStartConfiguration(
                merchantId = "YOUR_MERCHANT_ID",
                api = "ENV_API_URL",
                trackingApi = "ENV_TRACKING_API_URL",
            )
        lifecycleScope.launch {
            StaffStart.Core.initialize(staffStartConfiguration)
            StaffStart.tracking.initialize(applicationContext)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StaffStart.Core.close()
    }
}
