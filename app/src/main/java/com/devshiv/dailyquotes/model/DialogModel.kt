package com.devshiv.dailyquotes.model

data class DialogModel(
    var showDialog: Boolean = false,
    var success: Boolean = false,
    var title: String = "",
    var description: String = ""
)