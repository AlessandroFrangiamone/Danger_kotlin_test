package com.example.ircchatbot.model.bankaccount

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BankAccount(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("nome")
    val name: String? = null,
    @SerializedName("numero")
    val number: String? = null,
    @SerializedName("nickname")
    val nickname: String? = null,
    @SerializedName("iban")
    val iban: String? = null,
    @SerializedName("circuito")
    val circuit: String? = null,
    @SerializedName("saldo")
    val balance: BigDecimal? = null,
    @SerializedName("valuta")
    val currency: String? = null
)