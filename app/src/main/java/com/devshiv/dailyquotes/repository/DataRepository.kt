package com.devshiv.dailyquotes.repository

import com.devshiv.dailyquotes.App
import com.devshiv.dailyquotes.api.RetrofitApi
import com.devshiv.dailyquotes.db.entity.SettingsEntity
import com.devshiv.dailyquotes.model.LoginResponse
import com.devshiv.dailyquotes.model.QuotesModel
import com.devshiv.dailyquotes.model.TodayModel
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DataRepository @Inject constructor(private val api: RetrofitApi) {

    suspend fun getSettings(): Flow<SettingsEntity> = flow {
        val response = api.getSettings()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun loginUser(
        username: String,
        password: String,
    ): Flow<LoginResponse> = flow {
        val response = api.loginUser(
            username, password, App.app_version
        )
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun registerUser(
        username: String,
        password: String,
        email: String,
        number: String,
    ): Flow<LoginResponse> = flow {
        val response = api.registerUser(
            username, password, email, number, App.app_version, App.device_id
        )
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun getQuotes(): Flow<QuotesModel> = flow {
        val response = api.getQuotes()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun getTodayQuote(): Flow<TodayModel> = flow {
        val response = api.getTodayQuote()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun addUserPoints(
        username: String,
        pointsToAdd: String,
        type: String
    ): Flow<JsonObject> = flow {
        val response = api.addUserPoints(username, pointsToAdd, type)
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun getUserPoints(
        username: String,
    ): Flow<JsonObject> = flow {
        val response = api.getUserPoints(username)
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    suspend fun makeWithdrawal(
        username: String,
        pointsToWithdraw: String,
        amount: String,
        method: String,
        details: String,
        email: String,
    ): Flow<JsonObject> = flow {
        val response = api.makeWithdrawal(username, pointsToWithdraw, amount, method, details, email)
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

}