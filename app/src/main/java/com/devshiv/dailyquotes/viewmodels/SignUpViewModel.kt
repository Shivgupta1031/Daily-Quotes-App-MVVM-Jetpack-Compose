package com.devshiv.dailyquotes.viewmodels

import android.util.Patterns
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
class SignUpViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _signUpResponse = MutableStateFlow<ApiState>(ApiState.Empty)
    val signUpResponse: StateFlow<ApiState> = _signUpResponse.asStateFlow()

    private val _usernameState = MutableStateFlow(false)
    val usernameState: StateFlow<Boolean> = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(false)
    val passwordState: StateFlow<Boolean> = _passwordState.asStateFlow()

    private val _numberState = MutableStateFlow(false)
    val numberState: StateFlow<Boolean> = _numberState.asStateFlow()

    private val _emailState = MutableStateFlow(false)
    val emailState: StateFlow<Boolean> = _emailState.asStateFlow()

    fun signUpUser(
        username: String,
        password: String,
        number: String,
        email: String,
    ) {
        viewModelScope.launch {
            _signUpResponse.value = ApiState.Loading
            repository.registerUser(username, password, email, number)
                .catch { e ->
                    _signUpResponse.value = ApiState.Failure(e)
                }.collect {
                    _signUpResponse.value = ApiState.Success(it)
                }
        }
    }

    fun validateFields(
        username: String,
        password: String,
        email: String,
        number: String
    ) {
        _usernameState.value = username.isBlank() || username.isEmpty()
        _emailState.value = email.isBlank() || email.isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        _numberState.value = number.isBlank() || number.isEmpty() || number.length < 10
        _passwordState.value = password.isBlank() || password.isEmpty()

        if (_usernameState.value || _emailState.value || _numberState.value || _passwordState.value) {
            return
        }

        signUpUser(username, password, email, number)
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