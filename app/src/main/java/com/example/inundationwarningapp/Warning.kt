package com.example.inundationwarningapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun WarningScreen() {
    val context = LocalContext.current
    // 讀取你在 Address.kt 儲存的財產清單
    val myLocations = remember { loadLocations(context) }

    // 狀態管理
    var warningMessages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 進入頁面時抓取 API
    LaunchedEffect(Unit) {
        try {
            // 授權碼
            val response = NetworkClient.service.getRainfall("CWA-EADE5605-BCFC-43D2-80B7-05105357DECC")

            val newWarnings = mutableListOf<Message>()

            // 3. 邏輯比對：遍歷所有雨量站
            response.records.Station.forEach { station ->
                val rainValue = station.RainfallElement.Now.Precipitation.toDoubleOrNull() ?: 0.0

                // 門檻：時雨量 > 40.0 (大雨等級)
                if (rainValue > 40.0) {
                    val county = station.GeoInfo.CountyName
                    val town = station.GeoInfo.TownName

                    // 檢查我的財產清單中，是否有地址包含這個縣市或行政區
                    myLocations.forEach { myLoc ->
                        if (myLoc.address.contains(county) && myLoc.address.contains(town)) {
                            newWarnings.add(
                                Message(
                                    author = "淹水警戒：${myLoc.title}",
                                    body = "偵測到 ${county}${town} 目前時雨量為 ${rainValue} mm，預計 1 小時後可能出現淹水情形。請務必採取防災措施。"
                                )
                            )
                        }
                    }
                }
            }
            warningMessages = newWarnings
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
            errorMessage = "網路連線異常，請檢查網路設定。"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        // UI 部分：將 savedLocations 與 alertDistricts 進行比對
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(
                text = stringResource(R.string.flood_warning),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(15.dp))

            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                errorMessage != null -> Text(errorMessage!!, color = Color.Red)
                warningMessages.isEmpty() -> {
                    if (myLocations.isEmpty()) {
                        Text("請先至設定頁面新增財產地址。", color = Color.Gray)
                    } else {
                        Text("目前所有監控區域降雨正常，無淹水警戒。", color = Color.Gray)
                    }
                }
                else -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        warningMessages.forEach { msg ->
                            MessageCard(msg)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    // 移除 fillMaxSize()，改用 fillMaxWidth() 讓卡片可以一個接一個排隊
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.error
    ) {
        Column {
            Row(modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
                Image(
                    painter = painterResource(R.drawable.flood_unselected),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.onSecondary, // 建議用對比色
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Text(
                text = msg.body,
                modifier = Modifier.padding(15.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WarningScreenPreview() {
    WarningScreen()
}