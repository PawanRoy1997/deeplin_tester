package com.nextxform.deeplinktester.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextxform.deeplinktester.utils.db.DeepLinkDao
import com.nextxform.deeplinktester.utils.db.DeepLinkItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@HiltViewModel
class HistoryViewModel  @Inject constructor(private val deepLinkDao: DeepLinkDao) : ViewModel() {

    private val _deepLinkMutableStateFlow = MutableStateFlow(listOf<DeepLinkItem>())
    val deepLinkMutableState: StateFlow<List<DeepLinkItem>> = _deepLinkMutableStateFlow

    private var allFavouriteDeepLinks: List<DeepLinkItem> = listOf()

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm aa")

    init {
        viewModelScope.launch {
            allFavouriteDeepLinks = deepLinkDao.getAllDeepLinks().filter { it.isFavourite }.map { item ->
                var formattedTime = "NA"
                runCatching { simpleDateFormat.format(item.lastUsed) }.onFailure { formattedTime = "NA" }.onSuccess { formattedTime = it }
                DeepLinkItem(
                    item.id, item.url, formattedTime, item.isFavourite
                )
            }
            _deepLinkMutableStateFlow.emit(allFavouriteDeepLinks)
        }
    }

    fun onSearch(searchQuery: String) {
        _deepLinkMutableStateFlow.value = allFavouriteDeepLinks.filter { it.url.contains(searchQuery.trim()) }
    }
}