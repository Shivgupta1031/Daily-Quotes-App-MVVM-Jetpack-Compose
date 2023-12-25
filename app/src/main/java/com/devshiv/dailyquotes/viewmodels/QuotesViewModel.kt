package com.devshiv.dailyquotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.db.dao.UsersDao
import com.devshiv.dailyquotes.repository.DataRepository
import com.devshiv.dailyquotes.utils.ApiState
import com.devshiv.dailyquotes.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _quotesData = MutableStateFlow<ApiState>(ApiState.Empty)
    val quotesData: StateFlow<ApiState> = _quotesData.asStateFlow()

    init {
        viewModelScope.launch {
            _quotesData.value = ApiState.Loading
            repository.getQuotes()
                .catch { e ->
                    _quotesData.value = ApiState.Failure(e)
                }.collect {
                    _quotesData.value = ApiState.Success(it)
                }
        }
    }

    suspend fun addUserPoints(
        pointsToAdd: String,
        type: String,
        username: String = App.curUser
    ): Flow<ApiState> = flow {
        var data: ApiState = ApiState.Loading
        repository.addUserPoints(username, pointsToAdd, type)
            .catch { e ->
                data = ApiState.Failure(e)
                emit(data)
            }.collect {
                data = ApiState.Success(it)
                emit(data)
            }
    }

    suspend fun getTodayTask(): Int {
        val lastTask = usersDao.getAllData()[0].today_task
        val today = if (lastTask.isNotEmpty()) lastTask.split(",")[0] else ""
        return if (today != Utils.getTodayDate()) {
            0
        } else {
            lastTask.split(",")[1].toInt()
        }
    }

    suspend fun updateTodayTask() {
        val user = usersDao.getAllData()[0]
        user.today_task = Utils.getTodayDate() + "," + (getTodayTask() + 1).toString()
        usersDao.updateData(user)
    }

}