package com.example.inundationwarningapp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices
import kotlin.text.isBlank
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// 定義狀態常量，增加可讀性
private const val UI_STATE_DEFAULT = 0
private const val UI_STATE_SET = 1

// Data class to hold the form data (optional but good practice)
data class FormData(
    val title: String = "",
    val address: String = ""
)

// 存檔：將 List 轉為 JSON 字串存入 SharedPreferences
fun saveLocations(context: Context, locations: List<FormData>) {
    val sharedPreferences = context.getSharedPreferences("InundationPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val json = Gson().toJson(locations)
    editor.putString("saved_locations", json)
    editor.apply()
}

// 讀取：從 SharedPreferences 讀取 JSON 並轉回 List
fun loadLocations(context: Context): List<FormData> {
    val sharedPreferences = context.getSharedPreferences("InundationPrefs", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("saved_locations", null) ?: return emptyList()
    val type = object : TypeToken<List<FormData>>() {}.type
    return Gson().fromJson(json, type)
}

// 存檔：儲存最後一次獲取的定位字串
fun saveLastKnownLocation(context: Context, locationText: String) {
    val sharedPreferences = context.getSharedPreferences("InundationPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("last_known_location", locationText).apply()
}

// 讀取：獲取上次儲存的定位字串
fun loadLastKnownLocation(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("InundationPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("last_known_location", "正在獲取定位...") ?: "正在獲取定位..."
}

@Composable
fun AddressScreen() {
    val context = LocalContext.current
    var currentUiState by remember { mutableIntStateOf(UI_STATE_DEFAULT) }
    // --- 修改：初始值從檔案讀取 ---
    var customLocations by remember { mutableStateOf(loadLocations(context)) }

    // --- 新增：監控列表變化並存檔 ---
    // 每當 customLocations 內容改變時，這個區塊就會執行
    LaunchedEffect(customLocations) {
        saveLocations(context, customLocations)
    }

    // --- 修改：初始值改為從檔案讀取上次的紀錄 ---
    var currentLocationText by remember { mutableStateOf(loadLastKnownLocation(context)) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // --- 新增：定位權限 Launcher ---
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            // 權限成功，抓取位置
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val newLocation = "緯度：${String.format("%.2f", location.latitude)}，經度：${String.format("%.2f", location.longitude)}"

                        // 1. 更新目前的 UI 狀態
                        currentLocationText = newLocation

                        // 2. --- 新增：立即存檔到永久空間 ---
                        saveLastKnownLocation(context, newLocation)
                    } else {
                        "定位失敗，請確保 GPS 已開啟"
                    }
                }
            } catch (e: SecurityException) {
                currentLocationText = "權限不足"
            }
        }
    }

    when (currentUiState) {
        UI_STATE_DEFAULT -> {
            DefaultPreview(
                locationText = currentLocationText, // 傳入定位文字
                customLocations = customLocations, // 傳入列表
                onAddClick = { currentUiState = UI_STATE_SET },
                onLocationClick = {
                    // 點擊定位按鈕觸發
                    locationPermissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                // --- 新增：刪除邏輯 ---
                onDeleteLocation = { target ->
                    // 這裡刪除後，LaunchedEffect 會自動幫你存檔
                    customLocations = customLocations.filter { it != target }
                }
            )
        }
        UI_STATE_SET -> {
            SetAddress(
                onFinishCall = {currentUiState = UI_STATE_DEFAULT},
                onSubmit = { newData ->
                    // 這裡新增後，LaunchedEffect 會自動幫你存檔
                    customLocations = customLocations + newData
                    currentUiState = UI_STATE_DEFAULT
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DefaultPreview(
    locationText: String,
    customLocations: List<FormData>,
    onAddClick: () -> Unit,
    onLocationClick: () -> Unit,
    onDeleteLocation: (FormData) -> Unit // 新增 Callback
){
    // 用來控制對話框顯示的狀態
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<FormData?>(null) }

    // 刪除確認對話框
    if (showDeleteDialog && itemToDelete != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("刪除位置") },
            text = { Text("確定要刪除「${itemToDelete?.title}」嗎？") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        onDeleteLocation(itemToDelete!!)
                        showDeleteDialog = false
                    }
                ) { Text("確定", color = Color.Red) }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()), // 確保內容多時可捲動
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // header
            Text(
                text = stringResource(R.string.address_settings),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Text(
                text = stringResource(R.string.Click_the_button_in_the_lower_right_corner_to_set_the_address_and_location),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(10.dp))

            // 目前所在位置 Block
            LocationCard(title = "目前所在位置", content = locationText, color = MaterialTheme.colorScheme.surfaceVariant)

            Spacer(modifier = Modifier.padding(10.dp))

            //  動態新增的財產地址 Block
            customLocations.forEach { data ->
                Spacer(modifier = Modifier.height(10.dp))

                // 使用 Box 包裹以加入長按偵測
                Box(
                    modifier = Modifier.combinedClickable(
                        onClick = { /* 點擊可以做其他事，例如導航 */ },
                        onLongClick = {
                            itemToDelete = data
                            showDeleteDialog = true
                        }
                    )
                ) {
                    LocationCard(
                        title = data.title,
                        content = data.address,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
        // location button
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ){
            Column {
                androidx.compose.material3.Button(
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(width = 70.dp, height = 70.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.add),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Column {
                androidx.compose.material3.Button(
                    onClick = onLocationClick,
                    modifier = Modifier
                        .size(width = 70.dp, height = 70.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.place),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationCard(title: String, content: String, color: Color) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun SetAddress(onFinishCall: () -> Unit, onSubmit: (FormData) -> Unit) {
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
                text = stringResource(R.string.address_settings),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.padding(15.dp))

            // post
            var title by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }

            // Form validation state (optional, for more complex validation)
            var isTitleError by remember { mutableStateOf(false) }
            var isAddressError by remember { mutableStateOf(false) }

            fun validateFields(): Boolean {
                isTitleError = title.isBlank()
                isAddressError = address.isBlank()
                return !isTitleError && !isAddressError
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(335.dp)
                    .verticalScroll(rememberScrollState()), // Make the form scrollable if content overflows
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "輸入標題與地址",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(25.dp))

                // Title TextField
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (isTitleError) isTitleError = it.isBlank() // Clear error when user types
                    },
                    label = { Text("標題 (例：車子，住宅)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                // Address TextField
                AddressSearchBarField(
                    addressQuery = address,
                    onQueryChange = {
                        address = it
                        isAddressError = it.isBlank()
                    }
                )

                Spacer(modifier = Modifier.weight(1f)) // Push button to the bottom if content is short

                // Submit Button
                Button(
                    onClick = {
                        // 先檢查欄位是否為空
                        if (title.isNotBlank() && address.isNotBlank()) {
                            onSubmit(FormData(title = title, address = address))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("發送", fontSize = 26.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSearchBarField(
    addressQuery: String,
    onQueryChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val geocoder = remember { android.location.Geocoder(context) }
    var suggestions by remember { mutableStateOf(listOf<String>()) }

    Column {
        OutlinedTextField(
            value = addressQuery,
            onValueChange = { query ->
                onQueryChange(query)
                // 簡單的實作：當字數大於 3 時搜尋建議（非同步較佳，此處為示範）
                if (query.length > 3) {
                    try {
                        val addresses = geocoder.getFromLocationName(query, 3)
                        suggestions = addresses?.map { it.getAddressLine(0) } ?: emptyList()
                    } catch (e: Exception) {
                        suggestions = emptyList()
                    }
                }
            },
            label = { Text("搜尋地址 ...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        // 顯示搜尋建議清單
        suggestions.forEach { suggestion ->
            androidx.compose.material3.TextButton(
                onClick = {
                    onQueryChange(suggestion)
                    suggestions = emptyList()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(suggestion, textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddressScreenPreview() {
    AddressScreen()

}