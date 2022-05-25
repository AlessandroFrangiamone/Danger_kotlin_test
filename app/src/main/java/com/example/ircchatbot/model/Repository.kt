package com.example.ircchatbot.model

object Repository {

    fun getBankAccounts(customerId: String) = Api.retrofitClient.create(Services::class.java).getBankAccounts(customerId)
    fun getTransactions(customerId: String, page: Int) = Api.retrofitClient.create(Services::class.java).getTransactions(customerId, page)

}