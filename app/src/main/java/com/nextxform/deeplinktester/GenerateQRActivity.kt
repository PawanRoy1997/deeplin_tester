package com.nextxform.deeplinktester

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme
import com.nextxform.deeplinktester.viewModels.GenerateQRViewModel
import com.nextxform.deeplinktester.viewModels.PreviewGenerateQRViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenerateQRActivity : ComponentActivity() {
    private val viewModel: GenerateQRViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                GenerateQRScreen(viewModel) {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val link = intent.getStringExtra("url").orEmpty()
        viewModel.generateQRof(
            link = link,
            background = getDrawable(R.drawable.ic_app_launcher_foreground),
            onFailure = { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateQRScreen(viewModel: GenerateQRViewModel, backPress: () -> Unit) {
    val isGenerating by viewModel.isGenerating.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    title = { Text("QR Code", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary) },
                    navigationIcon = {
                        Icon(
                            painterResource(R.drawable.outline_arrow_back_24),
                            contentDescription = null,
                            modifier = Modifier.clickable { backPress.invoke() },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            if (isGenerating) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Spacer(Modifier.height(100.dp))
                Text("Generating...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            } else {
                Spacer(Modifier.padding(20.dp))
                Image(bitmap = viewModel.qrCode.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxWidth(0.9f).aspectRatio(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenerateQRScreenPreview(@PreviewParameter(PreviewGenerateQRViewModel::class) viewModel: GenerateQRViewModel) {
    DeeplinkTesterTheme {
        GenerateQRScreen(viewModel) {}
    }
}