package com.devshiv.dailyquotes.model

import com.devshiv.dailyquotes.db.entity.UsersEntity

data class LoginResponse(
    var status: String = "",
    var message: String = "",
    var user_details: UsersEntity?
)