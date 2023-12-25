package com.devshiv.dailyquotes.model

data class TodayModel(
    var status: String = "",
    var message: String = "",
    var data: QuotesModel.Quotes = QuotesModel.Quotes()
)