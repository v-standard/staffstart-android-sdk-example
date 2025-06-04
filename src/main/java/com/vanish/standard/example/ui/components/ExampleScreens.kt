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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanish.standard.staffstart.app.view.presentation.component.blocks.StaffStartBaseProductSnapPlaysBlock

/*
以下、SDK利用者様側の画面実装
 */

@Composable
fun TopScreen(
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
fun ProductScreen(
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
