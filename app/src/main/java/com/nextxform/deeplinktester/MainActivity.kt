package com.nextxform.deeplinktester

import android.app.UiModeManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme
import com.nextxform.deeplinktester.utils.db.DeepLinkEntity
import com.nextxform.deeplinktester.viewModels.MainViewModel


class MainActivity : ComponentActivity() {

    private var isDarkMode by mutableStateOf(false)

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
                    val viewModel: MainViewModel by viewModels()

                    viewModel.getHistory()
                    MainScreen(
                        viewModel = viewModel,
                        shareLink = { link -> shareLink(link) },
                        copyLink = { link -> copyLink(link) },
                        modifier = Modifier
                            .padding(it)
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        clearHistory = {
                            viewModel.clearHistory()
                        },
                        isDarkMode = isDarkMode
                    )
                }
            }
        }

        checkDarkMode()
    }


    private fun checkDarkMode() {
        val uiModeManager = this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        isDarkMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
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
    viewModel: MainViewModel,
    shareLink: (String) -> Unit,
    copyLink: (String) -> Unit = {},
    modifier: Modifier,
    clearHistory: () -> Unit,
    isDarkMode: Boolean = false
) {

    val list = viewModel.deepLinkLiveData.value.map(DeepLinkEntity::url).toList()
    val context: Context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        var text by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        val deeplink = { url: String ->
            try {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(url)
                }
                context.startActivity(intent)
                viewModel.addNewEntry(url, System.currentTimeMillis())
            } catch (e: Exception) {
                error = "Something went wrong!\nPlease check your deep link again"
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.removeEntry(url)
            }
        }
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
            },
            supportingText = {
                Text(error)
            },
            isError = error.isNotEmpty(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (text.trim().isEmpty()) {
                    error = "Deep Link is Empty"
                    return@Button
                }
                error = ""
                deeplink.invoke(text)
            },
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

            Text(
                text = "Clear All",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Blue),
                modifier = Modifier.clickable {
                    clearHistory.invoke()
                }
            )
        }

        if (list.isEmpty()) {
            Text(
                text = "No History",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        val textColor = if(isDarkMode) Color.Gray else Color.DarkGray

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list.size) {
                Item(deeplink = list[it], shareLink = shareLink, execute = deeplink, textColor = textColor)
            }
        }
    }
}

@Composable
fun Item(deeplink: String, shareLink: (String) -> Unit, execute: (String) -> Unit, textColor: Color) {
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
            text = deeplink,
            style = MaterialTheme.typography.titleMedium.copy(color = textColor),
            modifier = Modifier.weight(weight = 1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "TEST",
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
                .clickable {
                    execute.invoke(deeplink)
                },
            tint = Color("#8BC34A".toColorInt()),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = "SHARE",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .clickable {
                    shareLink.invoke(deeplink)
                },
            tint = Color("#2196F3".toColorInt()),
        )
    }

}

@Preview
@Composable
fun PreviewItem() {
    DeeplinkTesterTheme {
        Item(deeplink = "Sample Deep Link", shareLink = {}, execute = {}, Color.DarkGray)
    }
}

@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewItemNight() {
    DeeplinkTesterTheme {
        Item(deeplink = "Sample Deep Link", shareLink = {}, execute = {}, Color.Gray)
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
            ) { padding ->
            MainScreen(
                viewModel = MainViewModel(),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                shareLink = {},
                clearHistory = {}
            )
        }
    }
}