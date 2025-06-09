package com.example.inundationwarningapp

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.text.isBlank

// 定義狀態常量，增加可讀性
private const val UI_STATE_DEFAULT = 0
private const val UI_STATE_SET = 1

// Data class to hold the form data (optional but good practice)
data class FormData(
    val title: String = "",
    val address: String = ""
)

@Composable
fun AddressScreen() {
    // 使用 mutableIntStateOf 來儲存整數狀態，初始值為 UI_STATE_SOS_CONTENT (顯示 SOS 內容)
    var currentUiState by remember { mutableIntStateOf(UI_STATE_SET) }

    when (currentUiState) {
        UI_STATE_DEFAULT -> {
            DefaultPreview(
                onAddClick = {
                    currentUiState = UI_STATE_SET
                }
            )
        }
        UI_STATE_SET -> {
            SetAddress(
                onFinishCall = {
                    currentUiState = UI_STATE_DEFAULT
                },
                onSubmit = { formData ->
                    println("Form Submitted: Title = ${formData.title}, Address = ${formData.address}")
                }
            )
        }
    }
}

@Composable
fun DefaultPreview(onAddClick: () -> Unit){
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

            Text(
                text = stringResource(R.string.Click_the_button_in_the_lower_right_corner_to_set_the_address_and_location),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

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
                        onClick = {},
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
                    label = { Text("標題 (e.g., 車子, 住宅)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
//                    isError = isTitleError,
//                    supportingText = {
//                        if (isTitleError) {
//                            Text(
//                                text = "Title cannot be empty",
//                                color = MaterialTheme.colorScheme.error
//                            )
//                        }
//                    }
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
//                    onClick = {
//                        if (validateFields()) {
//                            onSubmit(FormData(title = title, address = address))
//                        }
                    onClick = onFinishCall,
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
    modifier: Modifier = Modifier
) {
    // SearchBar 通常需要一個 active 狀態來控制其展開/收縮的外觀和行為
    // 對於一個簡單的表單欄位，我們可能不希望它有複雜的 active 行為
    // 所以這裡的 active 狀態主要用於 SearchBar 的 API 要求
    var active by remember { mutableStateOf(false) }

    Box{ // 給 SearchBar 一些垂直邊距
        SearchBar(
            query = addressQuery,
            onQueryChange = onQueryChange,
            onSearch = {
                // 當使用者在鍵盤上按下搜尋按鈕時觸發
                // 你可以在這裡處理搜尋邏輯，或關閉 SearchBar 的 active 狀態
                active = false
                // onSubmit(FormData(title, addressQuery)) // 例如觸發表單提交
            },
            active = active, // 控制 SearchBar 是否處於活動/展開狀態
            onActiveChange = {
                // 當 active 狀態改變時觸發 (例如點擊 SearchBar)
                // 對於簡單的表單欄位，你可能想讓它一直 inactive，或者簡單地切換
                // active = it // 如果你希望它能展開，可以這樣寫
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            placeholder = { Text("搜尋地址...") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            },
            // trailingIcon = { ... } // 可以加入清除按鈕等
        ) {
            // 這個區塊是當 SearchBar 'active' 時顯示的內容 (例如搜尋建議列表)
            // 如果你只是把它當作一個輸入框，這個區塊可以為空，
            // 或者根據你的需求顯示一些建議 (這會增加複雜性)
            // For a simple text field, you might not need content here if 'active' is always false or managed simply.
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddressScreenPreview() {
    AddressScreen()

}