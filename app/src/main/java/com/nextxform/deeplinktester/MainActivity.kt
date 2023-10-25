package com.nextxform.deeplinktester

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources.Theme
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeeplinkTesterTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Deep Link Tester",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),

                    ) {
                    MainScreen(
                        deeplink = { link -> executeDeepLink(link) },
                        shareLink = { link -> shareLink(link) },
                        copyLink = { link -> copyLink(link) },
                        modifier = Modifier
                            .padding(it)
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        list = listOf(
                            "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester1",
                            "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester2",
                            "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester3",
                            "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester4"
                        )
                    )
                }
            }
        }
    }

    private fun executeDeepLink(link: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

    private fun shareLink(link: String) {
        val intent = Intent().apply {
            this.type = "text/plain"
            this.putExtra(Intent.EXTRA_TEXT, link)
        }
        val chooser = Intent.createChooser(intent, "Share link via")
        startActivity(chooser)
    }

    private fun copyLink(link: String) {
        val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("deeplink", link)
        clipboardManager.setPrimaryClip(clipData)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    deeplink: (String) -> Unit,
    shareLink: (String) -> Unit,
    copyLink: (String) -> Unit = {},
    modifier: Modifier,
    list: List<String> = ArrayList()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Deep Link") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.copy),
                    contentDescription = "Copy",
                    modifier = Modifier.clickable {
                        copyLink.invoke(text)
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { deeplink.invoke(text) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),

            ) {
            Text(
                text = "TEST",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 20.sp,
                textDecoration = TextDecoration.None,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "History",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(text = "Clear All", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Blue),)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list.size) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        .defaultMinSize(minHeight = 54.dp)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                        modifier = Modifier.weight(weight = 1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = "TEST",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clickable {
                                deeplink.invoke(list[it])
                            },
                        tint = Color.White,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "SHARE",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clickable {
                                shareLink.invoke(list[it])
                            },
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_UNDEFINED,
    showBackground = true
)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    DeeplinkTesterTheme {

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Deep Link Tester", style = MaterialTheme.typography.headlineMedium)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),

            ) {
            MainScreen(
                deeplink = {}, modifier = Modifier
                    .padding(it)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                shareLink = {},
                list = listOf(
                    "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester1",
                    "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester2",
                    "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester3",
                    "https://play.google.com/store/apps/details?id=com.penguenlabs.deeplinktester4"
                )
            )
        }
    }
}