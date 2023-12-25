package com.devshiv.dailyquotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.dailyquotes.db.dao.UsersDao
import com.devshiv.dailyquotes.db.entity.UsersEntity
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
class LoginViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _loginResponse = MutableStateFlow<ApiState>(ApiState.Empty)
    val loginResponse: StateFlow<ApiState> = _loginResponse.asStateFlow()

    private val _usernameState = MutableStateFlow(false)
    val usernameState: StateFlow<Boolean> = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(false)
    val passwordState: StateFlow<Boolean> = _passwordState.asStateFlow()

    fun loginUser(
        username: String,
        password: String,
    ) {
        viewModelScope.launch {
            _loginResponse.value = ApiState.Loading
            repository.loginUser(username, password)
                .catch { e ->
                    _loginResponse.value = ApiState.Failure(e)
                }.collect {
                    _loginResponse.value = ApiState.Success(it)
                }
        }
    }

    fun validateFields(username: String, password: String) {
        _usernameState.value = username.isBlank() || username.isEmpty()
        _passwordState.value = password.isBlank() || password.isEmpty()

        if (_usernameState.value || _passwordState.value) {
            return
        }

        loginUser(username, password)
    }

    fun saveData(data: UsersEntity){
        viewModelScope.launch {
            usersDao.addData(data)
        }
    }

    fun deleteAllData(){
        viewModelScope.launch {
            usersDao.deleteAllData()
        }
    }
}