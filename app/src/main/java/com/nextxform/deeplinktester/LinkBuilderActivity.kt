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

class LinkBuilderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LinkBuilderScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LinkBuilderScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Link Builder Activity",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LinkBuilderScreenPreview() {
    DeeplinkTesterTheme {
        LinkBuilderScreen()
    }
}