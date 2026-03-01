package com.nextxform.deeplinktester

import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.nextxform.deeplinktester.ui.theme.DeeplinkTesterTheme
import com.nextxform.deeplinktester.utils.db.DeepLinkItem
import com.nextxform.deeplinktester.viewModels.FavouriteDeepLinksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteLinksActivity : ComponentActivity() {
    private val viewModel: FavouriteDeepLinksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeeplinkTesterTheme {
                val links = viewModel.deepLinkMutableState.collectAsState()
                FavouriteLinksScreen(
                    "Favourite Links",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteLinksScreen(
    title: String,
    onBackPress: () -> Unit,
    onSearch: (String) -> Unit,
    deepLinks: List<DeepLinkItem>,
    shareLink: (String) -> Unit,
    execute: (String, Boolean) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    title = {
                        Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPress) {
                            Icon(
                                painterResource(R.drawable.outline_arrow_back_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var expanded by rememberSaveable { mutableStateOf(false) }
            val textFieldState = rememberTextFieldState()
            val textColor = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray
            SearchBar(
                modifier = Modifier
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = textFieldState.text.toString(),
                        onQueryChange = {
                            textFieldState.edit { replace(0, length, it) }
                            onSearch(textFieldState.text.toString())
                        },
                        onSearch = {
                            onSearch(textFieldState.text.toString())
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Search") }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(deepLinks.size) {
                        Item(
                            deepLinks[it],
                            shareLink = shareLink,
                            execute = execute,
                            textColor = textColor,
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(deepLinks.size) {
                    Item(
                        deepLinks[it],
                        shareLink = shareLink,
                        execute = execute,
                        textColor = textColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavouriteLinksScreenPreview() {
    DeeplinkTesterTheme {
        FavouriteLinksScreen(
            "Favourite Links",
            onBackPress = {},
            onSearch = {},
            deepLinks = listOf(),
            shareLink = {},
            execute = { _, _ -> },
        )
    }
}