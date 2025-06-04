package com.vanish.standard.example.ui.enum

import java.io.Serializable

enum class FavoriteTab(
    val title: String
) : Serializable {
    COORDINATE("コーデ"),
    STAFF("スタッフ")
}
