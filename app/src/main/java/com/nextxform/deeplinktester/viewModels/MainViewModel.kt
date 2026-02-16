package com.nextxform.deeplinktester.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextxform.deeplinktester.utils.db.DeepLinkDao
import com.nextxform.deeplinktester.utils.db.DeepLinkEntity
import com.nextxform.deeplinktester.utils.db.DeepLinkItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@HiltViewModel
class MainViewModel @Inject constructor(private val deepLinkDao: DeepLinkDao) : ViewModel() {
    private val _deepLinkMutableStateFlow = MutableStateFlow(listOf<DeepLinkItem>())
    val deepLinkStateFlow: StateFlow<List<DeepLinkItem>> = _deepLinkMutableStateFlow

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm aa")

    fun getHistory() {
        viewModelScope.launch {
            _deepLinkMutableStateFlow.value = deepLinkDao.getAllDeepLinks().map { item ->
                var formattedTime = "NA"
                runCatching { simpleDateFormat.format(item.lastUsed) }.onFailure { formattedTime = "NA" }.onSuccess { formattedTime = it }
                DeepLinkItem(
                    item.id, item.url, formattedTime, item.isFavourite
                )
            }
        }
    }

    fun addNewEntry(deepLink: String, time: Long, favourite: Boolean = false) {
        viewModelScope.launch {
            deepLinkDao.insertDeepLink(DeepLinkEntity(0, deepLink, time, favourite))
            val formattedDeeplink = deepLinkDao.getAllDeepLinks().map { item ->
                var formattedTime = "NA"
                runCatching { simpleDateFormat.format(item.lastUsed) }.onFailure { formattedTime = "NA" }.onSuccess { formattedTime = it }
                DeepLinkItem(
                    item.id, item.url, formattedTime, favourite
                )
            }
            _deepLinkMutableStateFlow.emit(formattedDeeplink)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            deepLinkDao.deleteAllLinks()
            _deepLinkMutableStateFlow.emit(listOf())
        }
    }
}
