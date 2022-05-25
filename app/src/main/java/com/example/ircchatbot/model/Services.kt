package com.example.ircchatbot.model

import com.example.ircchatbot.model.bankaccount.GetBankAccountsResponse
import com.example.ircchatbot.model.transaction.GetTransactionsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("accounts")
    fun getBankAccounts(@Query( "customer") customer: String): Observable<GetBankAccountsResponse>

    @GET("transactions")
    fun getTransactions(@Query("customer") customer: String, @Query("page") page: Int): Observable<GetTransactionsResponse>

}