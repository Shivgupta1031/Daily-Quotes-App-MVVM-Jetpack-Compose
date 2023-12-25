package com.devshiv.dailyquotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.db.dao.UsersDao
import com.devshiv.dailyquotes.db.entity.UsersEntity
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
class ProfileViewModel @Inject constructor(
    private var repository: DataRepository,
    private var usersDao: UsersDao
) : ViewModel() {

    private val _profileData = MutableStateFlow<UsersEntity>(UsersEntity())
    val profileData: StateFlow<UsersEntity> = _profileData.asStateFlow()

    init {
        getProfileData()
    }

    private fun getProfileData() {
        viewModelScope.launch {
            _profileData.value = usersDao.getAllData()[0]
        }
    }

    suspend fun checkDailyBonus(): Boolean {
        val lastBonus = usersDao.getAllData()[0].last_bonus
        return Utils.isDateGreaterThanToday(lastBonus)
    }

    suspend fun updateDailyBonusStatus(date: String) {
        val user = usersDao.getAllData()[0]
        user.last_bonus = date
        usersDao.updateData(user)
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

}