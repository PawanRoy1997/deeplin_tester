package com.nextxform.deeplinktester.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextxform.deeplinktester.utils.db.DeepLinkDao
import com.nextxform.deeplinktester.utils.db.DeepLinkDatabase
import com.nextxform.deeplinktester.utils.db.DeepLinkEntity
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var deepLinkDao: DeepLinkDao = DeepLinkDatabase.getDatabase().getDeepLinkDao()
    private val _deepLinkLiveData = mutableStateOf(listOf<DeepLinkEntity>())
    val deepLinkLiveData: State<List<DeepLinkEntity>> = _deepLinkLiveData

    fun getHistory() {
        viewModelScope.launch {
            _deepLinkLiveData.value = deepLinkDao.getAllDeepLinks()
        }
    }

    fun addNewEntry(deepLink: String, time: Long) {
        viewModelScope.launch {
            if(deepLinkLiveData.value.size == 100)
                deepLinkDao.deleteEntry(deepLinkLiveData.value.last().lastUsed)
            deepLinkDao.insertDeepLink(DeepLinkEntity(0, deepLink, time))
            _deepLinkLiveData.value = deepLinkDao.getAllDeepLinks()
        }
    }

    fun removeEntry(url: String) {
        viewModelScope.launch {
            deepLinkDao.deleteLastItem(url)
            _deepLinkLiveData.value = deepLinkDao.getAllDeepLinks()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            deepLinkDao.deleteAllLinks()
            _deepLinkLiveData.value = deepLinkDao.getAllDeepLinks()
        }
    }
}