package com.vanish.standard.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanish.standard.example.LocalExampleViewModel

@Composable
fun ExampleAppHeader() {
    Row(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .fillMaxWidth()
                .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("YourApp", color = MaterialTheme.colorScheme.background)
        LoginToggleButton()
    }
}

@Composable
private fun LoginToggleButton() {
    val exampleViewModel = LocalExampleViewModel.current
    val isLoggedIn = exampleViewModel.isLoggedIn.collectAsState().value

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isLoggedIn) {
            Text("ログイン中", color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(end = 10.dp))
        }
        Button(
            onClick = {
                if (isLoggedIn) {
                    exampleViewModel.logout()
                } else {
                    exampleViewModel.login()
                }
            },
        ) {
            Row {
                if (isLoggedIn) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "logout")
                    Text("ログアウト")
                } else {
                    Icon(Icons.Default.Person, "ログイン")
                    Text("ログイン")
                }
            }
        }
    }
}
