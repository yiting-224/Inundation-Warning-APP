package com.example.inundationwarningapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Surface


@Composable
fun SOSScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
//            modifier = Modifier
//                .fillMaxHeight()
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Column (
                    horizontalAlignment = Alignment.Start

                ) {
                    androidx.compose.material3.Button(
                        onClick = { println("Customized Button Clicked!") },
                        modifier = Modifier
                            .size(width = 180.dp, height = 180.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = CutCornerShape(10.dp),
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
                                fontSize = 26.sp
                            )
                        }
                    }
                }

                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    androidx.compose.material3.Button(
                        onClick = { println("Customized Button Clicked!") },
                        modifier = Modifier
                            .size(width = 180.dp, height = 180.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = CutCornerShape(10.dp),
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
                                fontSize = 22.sp
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
                    onClick = { println("Customized Button Clicked!") },
                    modifier = Modifier
                        .size(width = 375.dp, height = 200.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    shape = CutCornerShape(10.dp),
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    androidx.compose.material3.Button(
                        onClick = { println("Customized Button Clicked!") },
                        modifier = Modifier
                            .size(width = 180.dp, height = 180.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = CutCornerShape(10.dp),
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

                Column (
                    horizontalAlignment = Alignment.Start
                ) {
                    androidx.compose.material3.Button(
                        onClick = { println("Customized Button Clicked!") },
                        modifier = Modifier
                            .size(width = 180.dp, height = 180.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),
                        shape = CutCornerShape(10.dp),
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

@Preview(showBackground = true)
@Composable
fun SOSScreenPreview() {
    SOSScreen()
}