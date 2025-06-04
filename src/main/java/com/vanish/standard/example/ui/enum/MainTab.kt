package com.vanish.standard.example.ui.enum
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.Serializable

enum class MainTab(
    val label: String,
    val icon: ImageVector
) : Serializable {
    SNAP("コーデ", Icons.Default.Person),
    FAVORITE("お気に入り", Icons.Default.Favorite)
}
