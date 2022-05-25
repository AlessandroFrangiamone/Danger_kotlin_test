package com.example.ircchatbot.presenter

import android.os.Handler
import com.example.ircchatbot.contract.LandingPageContract
import com.example.ircchatbot.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LandingPagePresenter : LandingPageContract.Presenter {

    companion object {
        private const val LONG_DELAY = 1500L
        private const val SHORT_DELAY = 750L
    }

    private var mView: LandingPageContract.View? = null

    private val compositeDisposable = CompositeDisposable()

    override fun bindView(view: LandingPageContract.View) {
        mView = view
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    override fun callGetBankAccountsService(customerId: String) {
        mView?.startCarouselShimmer()
        mView?.startListShimmer()
        compositeDisposable.add(
            Repository.getBankAccounts(customerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response ->
                        Handler().postDelayed(
                            {
                                if (!response.accounts.isNullOrEmpty()) {
                                    mView?.onBankAccountsAvailable(bankAccounts = response.accounts.orEmpty())
                                } else {
                                    mView?.onServiceError()
                                }
                            },
                            LONG_DELAY
                        )
                    },
                    {
                        mView?.onServiceError()
                    }
                )
        )
    }

    override fun callGetTransactionsService(customerId: String) {
        mView?.startListShimmer()
        val nextPage = 1
        compositeDisposable.add(
            Repository.getTransactions(customerId, nextPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response ->
                        Handler().postDelayed(
                            {
                                if (!response.transactions.isNullOrEmpty()) {
                                    mView?.onTransactionsAvailable(response.transactions.orEmpty())
                                } else {
                                    mView?.onServiceError()
                                }
                            },
                            SHORT_DELAY
                        )
                    },
                    {
                        mView?.onServiceError()
                    }
                )
        )
    }
}