package com.example.inundationwarningapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
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

@Composable
fun AddressScreen() {
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
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = stringResource(R.string.tap_the_tag_twice_to_edit_information),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = { println("Customized Button Clicked!") },
                modifier = Modifier.width(300.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ),
                shape = CutCornerShape(10.dp),
            ) {
                Row(modifier = Modifier.padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)){
                    Image(
                        painter = painterResource(R.drawable.home),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(27.dp))
                    Text(
                        stringResource(R.string.home),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(top = 3.dp, bottom = 0.dp, start = 3.dp, end = 28.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = { println("Customized Button Clicked!") },
                modifier = Modifier.width(300.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White      // 按鈕內容 (文字) 顏色
                ),
                shape = CutCornerShape(10.dp),
            ) {
                Row(modifier = Modifier.padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)){
                    Image(
                        painter = painterResource(R.drawable.automobile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        stringResource(R.string.automobile),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(top = 3.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp
                    )
                }
            }
        }
    }
}

@Composable
fun Button(msg: Message) {
    Button(
        onClick = { println("Customized Button Clicked!") },
        modifier = Modifier.padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Magenta, // 按鈕背景顏色
            contentColor = Color.White      // 按鈕內容 (文字) 顏色
        ),
        shape = CutCornerShape(12.dp) // 設定按鈕的形狀 (例如：切角)
    ) {
        Text(
            text = stringResource(R.string.home),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddressScreenPreview() {
    AddressScreen()
}