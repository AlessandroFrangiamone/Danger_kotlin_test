package com.example.ircchatbot.model.transaction

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Transaction(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("accountId")
    val accountId: String? = null,
    @SerializedName("data")
    val data: String? = null,
    @SerializedName("destinatario")
    val description: String? = null,
    @SerializedName("tipologia")
    val type: String? = null,
    @SerializedName("categoria")
    val category: String? = null,
    @SerializedName("contabilizzata")
    val accounted: Boolean? = null,
    @SerializedName("importo")
    val amount: BigDecimal? = null,
    @SerializedName("segno")
    val symbol: String? = null
)