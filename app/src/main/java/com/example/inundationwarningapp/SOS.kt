package com.example.inundationwarningapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.material.shape.TriangleEdgeTreatment
import android.Manifest

// 定義狀態常量，增加可讀性
private const val UI_STATE_SOS_CONTENT = 0
private const val UI_STATE_REP_BY_CALL = 1
private const val UI_STATE_REP_BY_TEXT = 2

@Composable
fun SOSScreen() {
//    val context = LocalContext.current  // --- 1. 宣告區 使用
//    var currentUiState by remember { mutableIntStateOf(UI_STATE_SOS_CONTENT) } // --- 2. 邏輯區 使用
//
//    // --- 1. 宣告區：必須放在 Composable 的頂層 ---
//    val locationPermissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
//        val coarseLocationGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
//
//        if (fineLocationGranted || coarseLocationGranted) {
//            // 權限成功！這裡可以執行獲取位置的邏輯
//            // 提示：實務上這裡可以呼叫一個獲取經緯度並更新 UI 的 function
//        } else {
//            android.widget.Toast.makeText(context, "需定位權限以提供救援位置", android.widget.Toast.LENGTH_SHORT).show()
//        }
//    }

    val context = LocalContext.current
    var currentUiState by remember { mutableIntStateOf(UI_STATE_SOS_CONTENT) }

    // --- 新增：用來儲存待傳送的簡訊文字 ---
    var smsText by remember { mutableStateOf("您好，我是一名聽障人士，\n目前所在位置淹水，需救援。\n請儘速派人協助。\n\n謝謝！") }

    // --- 新增：定位工具 ---
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // 宣告權限 Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            // 權限成功：抓取位置後發送
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    val finalMessage = if (location != null) {
                        "$smsText\n\n我的精確位置：\nhttps://www.google.com/maps?q=${location.latitude},${location.longitude}"
                    } else {
                        "$smsText\n\n(暫時無法取得座標)"
                    }

                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:119")
                        putExtra("sms_body", finalMessage)
                    }
                    context.startActivity(intent)
                }
            } catch (e: SecurityException) {
                Toast.makeText(context, "定位失敗", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 權限被拒絕：依然發送簡訊，只是沒有座標
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:119")
                putExtra("sms_body", smsText + "\n\n(使用者未授權定位)")
            }
            context.startActivity(intent)
        }
    }
    // --- 2. 邏輯區：根據狀態顯示頁面 ---
    // 使用 mutableIntStateOf 來儲存整數狀態，初始值為 UI_STATE_SOS_CONTENT (顯示 SOS 內容)

    when (currentUiState) {
        UI_STATE_SOS_CONTENT -> {
            DefaultPreview(
                onEmergencyCallClick = {
                    currentUiState = UI_STATE_REP_BY_CALL
                },
                onEmergencyTextClick = {
                    currentUiState = UI_STATE_REP_BY_TEXT
                }
            )
        }
        UI_STATE_REP_BY_CALL -> {
            ReportByCall(
                onEmergencyCall = {
                    currentUiState = UI_STATE_REP_BY_TEXT
                }
            )
        }
//        UI_STATE_REP_BY_TEXT -> {
//            ReportByText(
//                onEmergencyTextClick = {
//                    currentUiState = UI_STATE_SOS_CONTENT
//                }
//            )
//        }
        UI_STATE_REP_BY_TEXT -> {
            ReportByText(
                currentText = smsText,
                onTextChange = { smsText = it },
                onSendClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun DefaultPreview(onEmergencyCallClick: () -> Unit, onEmergencyTextClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // header
            Text(
                text = stringResource(R.string.sos),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Text(
                text = stringResource(R.string.I_am_a___),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Row (
                modifier = Modifier
                    .size(width = 400.dp, height = 160.dp),
                horizontalArrangement = Arrangement.Center

            ) {
                Column (
                    horizontalAlignment = Alignment.Start

                ) {
                    androidx.compose.material3.Button(
                        onClick = onEmergencyCallClick,
                        modifier = Modifier
                            .size(width = 160.dp, height = 160.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Column {
                            Image(
                                painter = painterResource(R.drawable.person),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Text(
                                stringResource(R.string.regular_user),
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    androidx.compose.material3.Button(
                        onClick = onEmergencyCallClick,
                        modifier = Modifier
                            .size(width = 160.dp, height = 160.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Column {
                            Image(
                                painter = painterResource(R.drawable.sos),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Text(
                                stringResource(R.string.non_native_speaker),
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Button(
                    onClick = onEmergencyCallClick,
                    modifier = Modifier
                        .size(width = 335.dp, height = 200.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.blind),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        Text(
                            stringResource(R.string.visually_impaired_individual),
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier
                    .size(width = 400.dp, height = 160.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    androidx.compose.material3.Button(
                        onClick = onEmergencyTextClick,
                        modifier = Modifier
                            .size(width = 160.dp, height = 160.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Column {
                            Image(
                                painter = painterResource(R.drawable.hearing_disabled),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.width(27.dp))
                            Text(
                                stringResource(R.string.hearing_impaired_individual),
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                Column {
                    androidx.compose.material3.Button(
                        onClick = onEmergencyCallClick,
                        modifier = Modifier
                            .size(width = 160.dp, height = 160.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Column {
                            Image(
                                painter = painterResource(R.drawable.accessible),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.width(27.dp))
                            Text(
                                stringResource(R.string.person_with_disabilities),
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ReportByCall(onEmergencyCall: () -> Unit) {
    val context = LocalContext.current // 獲取 Context
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // header
            Text(
                text = stringResource(R.string.sos),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Text(
                text = stringResource(R.string.click_the_button_to_call_the_police_hotline),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Column {    // 開啟撥號頁面，填入號碼 119，但不自動撥出
                androidx.compose.material3.Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:119")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .size(width = 335.dp, height = 400.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.call911),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        Text(
                            stringResource(R.string._119),
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 50.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportByText(
    currentText: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // header
            Text(
                text = stringResource(R.string.sos),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.padding(15.dp))

            var text by remember { mutableStateOf("您好，我是一名聽障人士，\n" +
                    "目前所在位置淹水，需救援。\n" +
                    "請儘速派人協助。\n\n" +
                    "謝謝！") } // 狀態來保存使用者輸入的文字

            OutlinedTextField(
                value = currentText,
                onValueChange = onTextChange,
                label = { Text("Enter your message") }, // 提示標籤
                modifier = Modifier
                    .fillMaxWidth() // 讓文字框填滿可用寬度
                    .padding(16.dp),
                singleLine = false, // 允許多行輸入
                minLines = 5 // 最小行數，可以讓文字框看起來像一個訊息輸入區域
            )

            // Submit Button
            Column (
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ){
                androidx.compose.material3.Button(
                    onClick = onSendClick,
                    modifier = Modifier
                        .size(width = 335.dp, height = 65.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Row {
                        Image(
                            painter = painterResource(R.drawable.send),
                            contentDescription = null,
                            modifier = Modifier
                                .size(45.dp)
                        )

                        Spacer(modifier = Modifier.padding(15.dp))

                        Text(
                            stringResource(R.string.submit),
                            color = MaterialTheme.colorScheme.background,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SOSScreenPreview() {
    SOSScreen()
}