package com.devshiv.dailyquotes.api

import com.devshiv.dailyquotes.db.entity.SettingsEntity
import com.devshiv.dailyquotes.model.LoginResponse
import com.devshiv.dailyquotes.model.QuotesModel
import com.devshiv.dailyquotes.model.TodayModel
import com.devshiv.dailyquotes.utils.Constants
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("/admin/getSettings.php?api_key=${Constants.API_TOKEN}")
    suspend fun getSettings(): Response<SettingsEntity>

    @GET("/admin/loginUser.php?api_key=${Constants.API_TOKEN}")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("app_version") app_version: String,
    ): Response<LoginResponse>

    @GET("/admin/registerUser.php?api_key=${Constants.API_TOKEN}")
    suspend fun registerUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("email") email: String,
        @Query("phone_number") phone_number: String,
        @Query("app_version") app_version: String,
        @Query("device_id") device_id: String,
    ): Response<LoginResponse>

    @GET("/admin/getQuotes.php?api_key=${Constants.API_TOKEN}")
    suspend fun getQuotes(): Response<QuotesModel>

    @GET("/admin/getTodayQuote.php?api_key=${Constants.API_TOKEN}")
    suspend fun getTodayQuote(): Response<TodayModel>

    @GET("/admin/addUserPoints.php?api_key=${Constants.API_TOKEN}")
    suspend fun addUserPoints(
        @Query("username") username: String,
        @Query("points") password: String,
        @Query("type") email: String,
    ): Response<JsonObject>

    @GET("/admin/getUserPoints.php?api_key=${Constants.API_TOKEN}")
    suspend fun getUserPoints(
        @Query("username") username: String
    ): Response<JsonObject>

    @GET("/admin/makeWithdrawal.php?api_key=${Constants.API_TOKEN}")
    suspend fun makeWithdrawal(
        @Query("username") username: String,
        @Query("pointsToWithdraw") pointsToWithdraw: String,
        @Query("amount") amount: String,
        @Query("method") method: String,
        @Query("details") details: String,
        @Query("email") email: String
    ): Response<JsonObject>

}