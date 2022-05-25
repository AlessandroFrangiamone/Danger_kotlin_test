package com.example.ircchatbot.model.bankaccount

import com.google.gson.annotations.SerializedName

data class GetBankAccountsResponse(
    @SerializedName("accounts")
    val accounts: List<BankAccount>?
)