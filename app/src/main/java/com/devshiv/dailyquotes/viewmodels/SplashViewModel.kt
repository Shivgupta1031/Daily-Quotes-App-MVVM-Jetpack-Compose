package com.devshiv.dailyquotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.db.dao.SettingsDao
import com.devshiv.dailyquotes.db.dao.UsersDao
import com.devshiv.dailyquotes.db.entity.SettingsEntity
import com.devshiv.dailyquotes.db.entity.UsersEntity
import com.devshiv.dailyquotes.repository.DataRepository
import com.devshiv.dailyquotes.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private var repository: DataRepository,
    private var settingsDao: SettingsDao,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _settings = MutableStateFlow<ApiState>(ApiState.Empty)
    val settings: StateFlow<ApiState> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            _settings.value = ApiState.Loading
            repository.getSettings()
                .catch { e ->
                    _settings.value = ApiState.Failure(e)
                }.collect {
                    _settings.value = ApiState.Success(it)
                }
        }
    }

    fun saveSettings(data: SettingsEntity) {
        App.settings = data

        viewModelScope.launch {
            settingsDao.addData(data)
        }
    }

    fun checkUserLoginStatus(): Flow<Boolean> = flow {
        val users: List<UsersEntity> = usersDao.getAllData()
        val value = users.isNotEmpty() && users[0].username.isNotBlank()
        if (value) App.curUser = users[0].username
        emit(value)
    }.flowOn(Dispatchers.IO)

}