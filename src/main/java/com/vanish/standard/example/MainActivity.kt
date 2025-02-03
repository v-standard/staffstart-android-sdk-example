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
import com.vanish.standard.example.ui.components.TabView
import com.vanish.standard.example.ui.theme.StaffstartandroidsdkTheme
import com.vanish.standard.staffstart.core.framework.config.StaffStart
import com.vanish.standard.staffstart.core.framework.config.StaffStartConfiguration
import com.vanish.standard.staffstart.tracking.framework.config.tracking
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeStaffStartSDK()
        enableEdgeToEdge()
        setContent {
            StaffstartandroidsdkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TabView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun initializeStaffStartSDK() {
        val staffStartConfiguration = StaffStartConfiguration(
            merchantId = "Y75tLWJ7X2kWeke4Tk9UwUEcdKhAnKZp",
            api = "https://test.staff-start.com",
            trackingApi = "https://test-analytics.staff-start.com"
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
