package com.vanish.standard.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.vanish.standard.example.ui.components.NavView
import com.vanish.standard.example.ui.components.TabView
import com.vanish.standard.example.ui.theme.StaffstartandroidsdkTheme
import com.vanish.standard.staffstart.core.framework.config.StaffStart
import com.vanish.standard.staffstart.core.framework.config.StaffStartConfiguration
import com.vanish.standard.staffstart.tracking.framework.config.tracking
import kotlinx.coroutines.launch

private const val UNIT_SCREEN_FLAG = true

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeStaffStartSDK()
        enableEdgeToEdge()
        setContent {
            StaffstartandroidsdkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (UNIT_SCREEN_FLAG) {
                        NavView(modifier = Modifier.padding(innerPadding))
                    } else {
                        TabView(
                            modifier = Modifier.padding(innerPadding),
                        )
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
        // TODO 利用者に提供するclearのIFの整理
        // 何単位で提供するか
        StaffStart.Core.close()
    }
}
