package com.sachin.app.smsdetection

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.app.smsdetection.data.SMSLoader
import com.sachin.app.smsdetection.data.category.ExpenseCategory
import com.sachin.app.smsdetection.data.model.TransactionDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val smsLoader = SMSLoader(app)

    var smsList by mutableStateOf(emptyList<TransactionDetail>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedCategory: ExpenseCategory? by mutableStateOf(null)

    private var loadedList = emptyList<TransactionDetail>()

    init {
        snapshotFlow { selectedCategory }.onEach {
            if (loadedList.isNotEmpty()) {
                smsList = if (selectedCategory == null) {
                    loadedList
                } else {
                    loadedList.filter { it.category == selectedCategory }
                }
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    fun loadSMS() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            smsLoader.loadSMS()?.let {
                smsList = it
                loadedList = it
            }
            isLoading = false
        }
    }
}