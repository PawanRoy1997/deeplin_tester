package com.nextxform.deeplinktester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme

class GenerateQRActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GenerateQRScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GenerateQRScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Generated QR Activity",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GenerateQRScreenPreview() {
    DeeplinkTesterTheme {
        GenerateQRScreen()
    }
}