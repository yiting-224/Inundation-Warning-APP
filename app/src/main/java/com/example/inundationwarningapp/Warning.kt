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



@Composable
fun WarningScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            // header
            Text(
                text = stringResource(R.string.flood_warning),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.padding(15.dp))
            MessageCard(Message(stringResource(R.string.flood_warning), "住宅區域 1 小時後可能出現淹水情形。\n請務必採取防災措施。"))
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Surface(shape = MaterialTheme.shapes.large, shadowElevation = 3.dp, color = MaterialTheme.colorScheme.error) {

            Column{
                Row(modifier = Modifier.padding(top = 10.dp, bottom = 0.dp, start = 10.dp, end = 0.dp)){
                    Image(
                        painter = painterResource(R.drawable.flood_unselected),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = msg.author,
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(top = 10.dp, bottom = 0.dp, start = 2.dp, end = 10.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(top = 0.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun WarningScreenPreview() {
    WarningScreen()
}