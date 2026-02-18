package com.nextxform.deeplinktester

import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.net.toUri
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme
import com.nextxform.deeplinktester.viewModels.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                val links = viewModel.deepLinkMutableState.collectAsState()
                FavouriteLinksScreen(
                    "History",
                    onBackPress = { onBackPressedDispatcher.onBackPressed() },
                    onSearch = { searchQuery -> viewModel.onSearch(searchQuery) },
                    deepLinks = links.value,
                    shareLink = { shareLink(it) },
                    execute = { link, _ -> execute(link) }
                )
            }
        }
    }

    private fun shareLink(link: String) {
        val intent = Intent().apply {
            this.type = "text/plain"
            this.putExtra(EXTRA_TEXT, link)
        }
        val chooser = Intent.createChooser(intent, "Share link via")
        startActivity(chooser)
    }

    private fun execute(link: String) {
        try {
            val intent = Intent().apply {
                this.action = Intent.ACTION_VIEW
                this.data = link.toUri()
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, e.message.orEmpty(), Toast.LENGTH_SHORT).show()
        }
    }
}