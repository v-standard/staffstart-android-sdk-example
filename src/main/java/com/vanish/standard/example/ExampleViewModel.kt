package com.vanish.standard.example

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanish.standard.staffstart.app.view.StaffStartUI
import com.vanish.standard.staffstart.core.framework.config.StaffStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExampleViewModel : ViewModel() {
    private val _isShowLoginAlert = MutableStateFlow(false)
    val isShowLoginAlert: StateFlow<Boolean> get() = _isShowLoginAlert.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn.asStateFlow()

    fun showNeedLoginAlert() {
        _isShowLoginAlert.value = true
    }

    fun hideNeedLoginAlert() {
        _isShowLoginAlert.value = false
    }

    fun login() {
        viewModelScope.launch {
            _isLoggedIn.value = true
            StaffStart.Core.setCustomerUserCode("YOUR_SERVICE_USER_CUSTOMER_CODE")
            StaffStartUI.refresh()
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoggedIn.value = false
            StaffStart.Core.setCustomerUserCode(null)
            StaffStartUI.refresh()
        }
    }
}

val LocalExampleViewModel =
    staticCompositionLocalOf<ExampleViewModel> {
        error("No ExampleViewModel provided")
    }
