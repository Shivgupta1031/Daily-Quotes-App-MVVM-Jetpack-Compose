package com.devshiv.dailyquotes.model

data class QuotesModel(
    var status: String,
    var message: String = "",
    var data: List<Quotes>
) {
    data class Quotes(
        var id: Int = 0,
        var quote: String = "",
        var written_by: String = "",
        var image: String = "",
        var created: String = "",
    )
}