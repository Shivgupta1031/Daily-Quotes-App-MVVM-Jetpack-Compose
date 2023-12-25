package com.devshiv.dailyquotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.dailyquotes.repository.DataRepository
import com.devshiv.dailyquotes.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private var repository: DataRepository
) : ViewModel() {

    private val _quotesData = MutableStateFlow<ApiState>(ApiState.Empty)
    val quotesData: StateFlow<ApiState> = _quotesData.asStateFlow()

    init {
        viewModelScope.launch {
            _quotesData.value = ApiState.Loading
            repository.getTodayQuote()
                .catch { e ->
                    _quotesData.value = ApiState.Failure(e)
                }.collect {
                    _quotesData.value = ApiState.Success(it)
                }
        }
    }

}