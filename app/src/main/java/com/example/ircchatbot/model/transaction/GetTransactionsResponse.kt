package com.example.ircchatbot.model.transaction

import com.google.gson.annotations.SerializedName

data class GetTransactionsResponse(
    @SerializedName("transactions")
    val transactions: List<Transaction>?
)