package com.example.ircchatbot.util

import com.example.ircchatbot.model.bankaccount.BankAccount

object BankUtils {
    fun getBankAccountCurrencySymbol(bankAccount: BankAccount): String {
        return when (bankAccount.currency) {
            "EUR" -> "€"
            "USD" -> "$"
            "GBP" -> "£"
            "JPY" -> "¥"
            else -> "€"
        }
    }
}