package com.nextxform.deeplinktester

import android.Manifest.permission.CAMERA
import android.app.UiModeManager
import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme
import com.nextxform.deeplinktester.utils.db.DeepLinkEntity
import com.nextxform.deeplinktester.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
    private var isDarkMode by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                val list = viewModel.deepLinkLiveData.value.map(DeepLinkEntity::url).toList()
                Scaffold(
                    contentWindowInsets = WindowInsets.safeContent,
                    topBar = {
                        Surface(shadowElevation = 4.dp) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Deep Link Tester",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                            )
                        }
                    },
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(onClick = {
                                    val intent = Intent(this@MainActivity, FavouriteLinksActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.favourite),
                                        contentDescription = "Localized description",
                                        Modifier.size(24.dp)
                                    )
                                }
                                IconButton(onClick = {
                                    val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Icon(
                                        painterResource(R.drawable.outline_avg_time_24),
                                        contentDescription = "Localized description",
                                        Modifier.size(24.dp)
                                    )
                                }
                            },
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    val intent = Intent(this@MainActivity, LinkBuilderActivity::class.java)
                                    startActivity(intent)
                                }, containerColor = MaterialTheme.colorScheme.primary) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_add_2_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        list = list,
                        shareLink = { link -> shareLink(link) },
                        clearHistory = { viewModel.clearHistory() },
                        log = { url, time -> viewModel.addNewEntry(url, time) },
                        isDarkMode = isDarkMode
                    )
                }
            }
        }
        checkDarkMode()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getHistory()
    }

    private fun checkDarkMode() {
        val uiModeManager = this.getSystemService(UI_MODE_SERVICE) as UiModeManager
        isDarkMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    }

    private fun shareLink(link: String) {
        val intent = Intent().apply {
            this.type = "text/plain"
            this.putExtra(EXTRA_TEXT, link)
        }
        val chooser = Intent.createChooser(intent, "Share link via")
        startActivity(chooser)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    list: List<String>,
    shareLink: (String) -> Unit,
    modifier: Modifier,
    clearHistory: () -> Unit,
    log: (String, Long) -> Unit,
    isDarkMode: Boolean = false
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val context = LocalContext.current
        var text by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        var hasCameraPermission by remember { mutableStateOf(false) }
        val deeplink = { url: String ->
            try {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = url.toUri()
                }
                context.startActivity(intent)
                log(url, System.currentTimeMillis())
            } catch (_: Exception) {
                error = "Something went wrong!\nPlease check your deep link again"
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }

        val qrScanner = rememberLauncherForActivityResult(ScanContract()) { result: ScanIntentResult? ->
            if (result == null || result.originalIntent == null) {
                Toast.makeText(context, "Scan Cancelled", Toast.LENGTH_SHORT).show()
                return@rememberLauncherForActivityResult
            }

            if (result.originalIntent.getBooleanExtra("CANCELLED", false)) {
                Toast.makeText(context, "Scan Cancelled", Toast.LENGTH_SHORT).show()
                return@rememberLauncherForActivityResult
            }

            text = result.contents
        }

        val cameraPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { result: Boolean? ->
            hasCameraPermission = (result == true)
        }

        fun scanQRCode() {
            if (!hasCameraPermission) {
                cameraPermissionLauncher.launch(CAMERA); return
            }
            val scanOption = ScanOptions().apply {
                setCaptureActivity(CaptureActivity::class.java)
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setPrompt("Scan QR Code")
                setBeepEnabled(true)
                setOrientationLocked(true)
                setTorchEnabled(true)
                setCameraId(0)
            }
            qrScanner.launch(scanOption)
        }

        Spacer(Modifier.height(10.dp))

        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp, focusedElevation = 6.dp)
        ) {
            Column(Modifier.padding(10.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter Deep Link") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.qr_code),
                            contentDescription = "Copy",
                            tint = Color(0xFF673AB7),
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { scanQRCode() }
                        )
                    },
                    supportingText = {
                        Text(error)
                    },
                    isError = error.isNotEmpty(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (text.trim().isEmpty()) {
                                error = "Deep Link is Empty"
                                return@Button
                            }
                            error = ""
                            deeplink.invoke(text)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Fire Deep Link",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    OutlinedButton(
                        onClick = {
                            if (text.trim().isEmpty()) {
                                error = "Deep Link is Empty"
                                return@OutlinedButton
                            }
                            error = ""
                            deeplink.invoke(text)
                        },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    ) {
                        Text(
                            text = "Add to Favourite",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            }
        }

        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "History",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text("Clear All", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
        }

        if (list.isEmpty()) {
            Text(
                text = "No History",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { clearHistory.invoke() }
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        val textColor = if (isDarkMode) Color.Gray else Color.DarkGray

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list.size) {
                Item(
                    deeplink = list[it],
                    shareLink = shareLink,
                    execute = deeplink,
                    textColor = textColor
                )
            }
        }
    }
}

@Composable
fun Item(
    deeplink: String,
    shareLink: (String) -> Unit,
    execute: (String) -> Unit,
    textColor: Color
) {
    val context = LocalContext.current
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
            painter = painterResource(R.drawable.qr_code),
            contentDescription = "TEST",
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
                .clickable {
                    val intent = Intent(context, GenerateQRActivity::class.java)
                    intent.putExtra("url", deeplink)
                    context.startActivity(intent)
                },
            tint = Color(0xFF673AB7),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(R.drawable.play),
            contentDescription = "TEST",
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
                .clickable {
                    execute.invoke(deeplink)
                },
            tint = Color(0xFF8BC34A),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(R.drawable.share),
            contentDescription = "SHARE",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .clickable {
                    shareLink.invoke(deeplink)
                },
            tint = Color(0xFF2196F3),
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
            Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeContent,
            topBar = {
                Surface(shadowElevation = 4.dp) {
                    TopAppBar(
                        title = {
                            Text(text = "Deep Link Tester", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        },
                    )
                }
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(painter = painterResource(R.drawable.favourite), contentDescription = "Localized description", Modifier.size(20.dp))
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(painterResource(R.drawable.outline_avg_time_24), contentDescription = "Localized description", Modifier.size(20.dp))
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /* do something */ }, containerColor = MaterialTheme.colorScheme.primary) {
                            Icon(
                                painter = painterResource(R.drawable.outline_add_2_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                )
            }
        ) { padding ->
            MainScreen(
                listOf(),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth(),
                shareLink = {},
                clearHistory = {},
                log = { _, _ -> },
                isDarkMode = false
            )
        }
    }
}
