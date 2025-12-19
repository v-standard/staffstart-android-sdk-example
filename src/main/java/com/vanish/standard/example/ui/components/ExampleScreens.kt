package com.vanish.standard.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanish.standard.example.LocalExampleViewModel
import com.vanish.standard.staffstart.app.domain.enum.order.Order
import com.vanish.standard.staffstart.app.view.presentation.component.blocks.StaffStartBaseProductSnapPlaysBlock
import com.vanish.standard.staffstart.app.view.presentation.component.blocks.StaffStartBrandSnapPlaysBlock
import com.vanish.standard.staffstart.app.view.presentation.component.blocks.StaffStartBrandStaffsBlock
import com.vanish.standard.staffstart.app.viewmodel.BrandSnapPlaysBlockCondition
import com.vanish.standard.staffstart.app.viewmodel.BrandStaffsBlockConditions
import com.vanish.standard.staffstart.core.domain.enum.CoordinateGenre
import com.vanish.standard.staffstart.core.domain.enum.Gender

/*
以下、SDK利用者様側の画面実装
 */

@Composable
fun TopScreen(
    onClickToDetailPage: () -> Unit,
    onClickToSnapPlayListPage: () -> Unit,
    onClickToStaffListPage: () -> Unit,
    onClickBrandPage: () -> Unit
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
            Button(onClickBrandPage) { Text("ブランド画面") }
            Button(onClickToSnapPlayListPage) { Text("SnapPlay一覧画面") }
            Button(onClickToStaffListPage) { Text("Staff一覧画面") }
        }
    }
}

@Composable
fun ProductScreen(
    useDarkTheme: Boolean,
    baseProductCode: String? = null,
    onTapSnapPlayDetail: (snapPlayId: Int) -> Unit,
    onTapReadMore: (baseProductCode: String?) -> Unit,
    onFavoriteAttemptWithoutLogin: () -> Unit = {}
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("ここに御社の商品情報が入ります")
        Text("白スウェット ヘビーウェイト")
        Text("とてもおしゃれなスウェットです。.....")
        Spacer(modifier = Modifier.height(40.dp))
        StaffStartBaseProductSnapPlaysBlock(
            useDarkTheme = useDarkTheme,
            baseProductCode = baseProductCode,
            onTapSnapPlayDetail = onTapSnapPlayDetail,
            onTapReadMore = onTapReadMore,
            onFavoriteAttemptWithoutLogin = onFavoriteAttemptWithoutLogin,
        )
    }
}

@Composable
fun BrandScreen(
    labelId: Int?,
    useDarkTheme: Boolean,
    onTapSnapPlay: (snapPlayId: Int) -> Unit,
    onTapReadMoreSnapPlay: (brandSnapPlaysBlockCondition: BrandSnapPlaysBlockCondition) -> Unit,
    onTapStaff: (userId: Int) -> Unit,
    onTapReadMoreStaff: (brandStaffsBlockConditions: BrandStaffsBlockConditions) -> Unit
) {
    val exampleViewModel = LocalExampleViewModel.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "VanishStandard",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
            )
            Text("今話題の原宿系ファッションブランド")
            Text("私たちは小売・サービス業の“人の価値”を再定義するSaaSプロダクト「STAFF START」を展開しています。店舗スタッフの接客力をオンラインでも発揮できる仕組みなど、業界構造や働き方に新しい常識を生み出し続けているスタートアップです。")
        }

        BrandSnapPlaysBlockSample(
            brandSnapPlaysBlockCondition =
                BrandSnapPlaysBlockCondition(
                    labelId = null,
                ),
            headerTitle = "全てのコーデ！",
            title = "たくさん表示されます",
            moreLabel = "もっとたくさん >",
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = onTapSnapPlay,
            onTapReadMoreSnapPlay = onTapReadMoreSnapPlay,
        )

        BrandSnapPlaysBlockSample(
            brandSnapPlaysBlockCondition =
                BrandSnapPlaysBlockCondition(
                    labelId = labelId,
                    coordinateGenre = CoordinateGenre.MALE,
                    tags = setOf("テストタグ2", "タグランキングテスト"),
                    order = Order.POPULARITY,
                ),
            headerTitle = "夏のメンズ人気コーデ！",
            title = "夏の爽やか",
            moreLabel = "もっと夏を >",
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = onTapSnapPlay,
            onTapReadMoreSnapPlay = onTapReadMoreSnapPlay,
        )

        BrandSnapPlaysBlockSample(
            brandSnapPlaysBlockCondition =
                BrandSnapPlaysBlockCondition(
                    labelId = labelId,
                    coordinateGenre = CoordinateGenre.FEMALE,
                    order = Order.NEW_ARRIVAL,
                ),
            headerTitle = "夏の女性コーデ",
            title = "最新Cute",
            moreLabel = "もっと新着をみる >",
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = onTapSnapPlay,
            onTapReadMoreSnapPlay = onTapReadMoreSnapPlay,
        )

        HorizontalDivider(modifier = Modifier.padding(top = 20.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
        ) {
            Text("ファッションのスペシャリスト")
            Text("スタッフへのご相談はお気軽に")
        }

        BrandStaffsBlockSample(
            brandStaffsBlockConditions =
                BrandStaffsBlockConditions(
                    labelId = null,
                ),
            headerTitle = "全員",
            title = "全てのスタッフ",
            moreLabel = "全員を見る",
            onTapStaff = onTapStaff,
            useDarkTheme = useDarkTheme,
            onTapReadMoreStaff = onTapReadMoreStaff,
            onFavoriteAttemptWithoutLogin = {
                // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                exampleViewModel.showNeedLoginAlert()
            },
        )

        BrandStaffsBlockSample(
            brandStaffsBlockConditions =
                BrandStaffsBlockConditions(
                    labelId = labelId,
                    gender = Gender.MALE,
                    order = Order.POPULARITY,
                ),
            headerTitle = "男性スタッフ",
            title = "人気の男性",
            moreLabel = "もっと見たい",
            useDarkTheme = useDarkTheme,
            onTapStaff = onTapStaff,
            onTapReadMoreStaff = onTapReadMoreStaff,
            onFavoriteAttemptWithoutLogin = {
                // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                exampleViewModel.showNeedLoginAlert()
            },
        )

        BrandStaffsBlockSample(
            brandStaffsBlockConditions =
                BrandStaffsBlockConditions(
                    labelId = labelId,
                    gender = Gender.FEMALE,
                    order = Order.NEW_ARRIVAL,
                ),
            headerTitle = "女性スタッフ",
            title = "ニューカマー",
            moreLabel = "もっと新人を見る",
            useDarkTheme = useDarkTheme,
            onTapStaff = onTapStaff,
            onTapReadMoreStaff = onTapReadMoreStaff,
            onFavoriteAttemptWithoutLogin = {
                // TODO ログインしていないのにお気に入りしようとした際のcallbackを実装してください
                exampleViewModel.showNeedLoginAlert()
            },
        )

        HorizontalDivider(modifier = Modifier.padding(top = 20.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Contact Us")
            Button({}) {
                Text("お問い合わせ")
            }
        }
    }
}

@Composable
private fun BrandSnapPlaysBlockSample(
    brandSnapPlaysBlockCondition: BrandSnapPlaysBlockCondition,
    headerTitle: String,
    title: String,
    moreLabel: String,
    useDarkTheme: Boolean,
    onTapSnapPlay: (snapPlayId: Int) -> Unit,
    onTapReadMoreSnapPlay: (brandSnapPlaysBlockCondition: BrandSnapPlaysBlockCondition) -> Unit,
    onFavoriteAttemptWithoutLogin: () -> Unit = {}
) {
    Column {
        Column {
            Text(
                headerTitle,
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .fillMaxWidth()
                        .padding(10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 10.dp),
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1.0f))
                // ご自由にどうぞ 3
                Text(
                    moreLabel,
                    color = MaterialTheme.colorScheme.outline,
                    modifier =
                        Modifier.clickable {
                            onTapReadMoreSnapPlay(brandSnapPlaysBlockCondition)
                        },
                )
            }
        }

        StaffStartBrandSnapPlaysBlock(
            conditions = brandSnapPlaysBlockCondition,
            useDarkTheme = useDarkTheme,
            onTapSnapPlay = onTapSnapPlay,
            onFavoriteAttemptWithoutLogin = onFavoriteAttemptWithoutLogin,
        )
    }
}

@Composable
private fun BrandStaffsBlockSample(
    brandStaffsBlockConditions: BrandStaffsBlockConditions,
    headerTitle: String,
    title: String,
    moreLabel: String,
    useDarkTheme: Boolean,
    onTapStaff: (userId: Int) -> Unit,
    onTapReadMoreStaff: (BrandStaffsBlockConditions) -> Unit,
    onFavoriteAttemptWithoutLogin: () -> Unit = {}
) {
    Column {
        Column {
            Text(
                headerTitle,
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .fillMaxWidth()
                        .padding(10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 10.dp),
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    moreLabel,
                    color = MaterialTheme.colorScheme.outline,
                    modifier =
                        Modifier.clickable {
                            onTapReadMoreStaff(brandStaffsBlockConditions)
                        },
                )
            }
        }

        StaffStartBrandStaffsBlock(
            conditions = brandStaffsBlockConditions,
            useDarkTheme = useDarkTheme,
            onTapStaff = onTapStaff,
            onFavoriteAttemptWithoutLogin = onFavoriteAttemptWithoutLogin,
        )
    }
}
