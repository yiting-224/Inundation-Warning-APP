package com.example.inundationwarningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inundationwarningapp.ui.theme.InundationWarningAPPTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            InundationWarningAPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(selectedTab: Int = 1) {
//    val navController = rememberNavController()

    val navItems = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.address_settings),
            selectedIcon = painterResource(id = R.drawable.location),
            unselectedIcon = painterResource(id = R.drawable.location_unselected),
            hasNews = false
        ),
        BottomNavigationItem(
            title = stringResource(R.string.flood_warning),
            selectedIcon = painterResource(id = R.drawable.flood),
            unselectedIcon = painterResource(id = R.drawable.flood_unselected),
            hasNews = false
        ),
        BottomNavigationItem(
            title = stringResource(R.string.sos),
            selectedIcon = painterResource(id = R.drawable.emergency_home),
            unselectedIcon = painterResource(id = R.drawable.emergency_home_unselected),
            hasNews = false
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(selectedTab)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navController.navigate(item.title)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    painter = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        })
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (selectedItemIndex) {
                0 -> AddressScreen()
                1 -> WarningScreen()
                2 -> SOSScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InundationWarningAPPTheme {
        MainScreen(1)
    }
}

@Preview(showBackground = true)
@Composable
fun RecordPreview() {
    InundationWarningAPPTheme {
        MainScreen(0)
    }
}

@Preview(showBackground = true)
@Composable
fun TodoPreview() {
    InundationWarningAPPTheme {
        MainScreen(2)
    }
}
