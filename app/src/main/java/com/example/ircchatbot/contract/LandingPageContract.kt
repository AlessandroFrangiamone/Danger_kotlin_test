package com.example.ircchatbot.contract

import com.example.ircchatbot.model.bankaccount.BankAccount
import com.example.ircchatbot.model.transaction.Transaction

interface LandingPageContract {
    interface View {
        fun onServiceError()
        fun startCarouselShimmer()
        fun onBankAccountsAvailable(bankAccounts: List<BankAccount>)
        fun startListShimmer()
        fun onTransactionsAvailable(transactions: List<Transaction>)
    }

    interface Presenter {
        fun bindView(view: View)
        fun onDestroy()
        fun callGetBankAccountsService(customerId: String)
        fun callGetTransactionsService(customerId: String)
    }
}