package com.example.ircchatbot.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.ircchatbot.R
import com.example.ircchatbot.contract.LandingPageContract
import com.example.ircchatbot.model.bankaccount.BankAccount
import com.example.ircchatbot.model.transaction.Transaction
import com.example.ircchatbot.presenter.LandingPagePresenter
import com.example.ircchatbot.util.AdapterRowWrapper
import com.example.ircchatbot.util.BankUtils
import com.example.ircchatbot.util.CarouselPageTransformer
import com.example.ircchatbot.view.adapter.CarouselPagerAdapter
import com.example.ircchatbot.view.adapter.OperationsAdapter
import kotlinx.android.synthetic.main.fragment_landing_page.*

class LandingPageFragment : Fragment(), LandingPageContract.View {

    companion object {
        fun newInstance() = LandingPageFragment()
    }


    private val mPresenter = LandingPagePresenter()

    private val mAdapter = OperationsAdapter()

    private val mCustomerId = "32323232"
    private var mSelectedAccount = BankAccount()


    /**
     * [Fragment] functions
     * */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.bindView(view = this)
        setupMenuListener()
        operationsRecyclerView.adapter = mAdapter

        mPresenter.callGetBankAccountsService(customerId = mCustomerId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    /**
     * [LandingPageContract.View] functions
     * */
    override fun onServiceError() {
        Toast
            .makeText(context, "Request ended with an error", Toast.LENGTH_LONG)
            .show()
    }

    override fun startCarouselShimmer() {
        val shimmerAdapter = CarouselPagerAdapter()
        shimmerAdapter.startCarouselShimmer()
        carouselView.setCarouselAdapter(shimmerAdapter)
    }

    override fun onBankAccountsAvailable(bankAccounts: List<BankAccount>) {
        setupCarousel(bankAccounts)
    }

    override fun startListShimmer() {
        mAdapter.startShimmer()
    }

    override fun onTransactionsAvailable(transactions: List<Transaction>) {
        setupTransactions(transactions)
    }

    /**
     * Private functions
     * */
    private fun setupCarousel(bankAccounts: List<BankAccount>) {
        mSelectedAccount = bankAccounts[0]
        collapsedBankAccountName.text = mSelectedAccount.name

        val carouselAdapter = CarouselPagerAdapter()
        carouselAdapter.setData(bankAccounts)
        mPresenter.callGetTransactionsService(customerId = mCustomerId)

        carouselView.setPageTransformer(false, CarouselPageTransformer())
        carouselView.setCarouselAdapter(carouselAdapter)
        carouselView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // no need to implement code here
            }

            override fun onPageSelected(position: Int) {
                mSelectedAccount = bankAccounts[position]
                collapsedBankAccountName.text = mSelectedAccount.name
                mPresenter.callGetTransactionsService(customerId = mCustomerId)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // no need to implement code here
            }
        })
        carouselView.offscreenPageLimit = 3
        carouselView.clipToPadding = false

        landingPageMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                // no need to implement code here
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                carouselAdapter.editAdjacentViewsVisibility(
                    isVisible = false,
                    position = carouselView.currentItem
                )
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                if (p3 == 0f) {
                    carouselAdapter.editAdjacentViewsVisibility(
                        isVisible = true,
                        position = carouselView.currentItem
                    )
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                // no need to implement code here
            }
        })
    }

    private fun setupTransactions(transactions: List<Transaction>) {
        val adapterList = mutableListOf<AdapterRowWrapper<Any>>()
        transactions.forEach { transaction ->
            adapterList.add(
                AdapterRowWrapper(
                    rowType = OperationsAdapter.OPERATION_TYPE,
                    data = OperationsAdapter.OperationData(
                        operationId = transaction.id,
                        operationDescription = transaction.description,
                        operationAmount = "${transaction.symbol}${transaction.amount} ${BankUtils.getBankAccountCurrencySymbol(mSelectedAccount)}"
                    )
                )
            )
        }

        mAdapter.setData(adapterList)
    }

    private fun setupMenuListener() {
        settingsView.setOnClickListener {
            context?.let { ctx ->
                val choices = arrayOf(
                    getString(R.string.fragment_landing_page_settings)
                )
                val onSelectionListener = DialogInterface.OnClickListener { _, which ->
                    when (choices[which]) {
                        getString(R.string.fragment_landing_page_settings) -> {
                            val settingsIntent = Intent(ctx, SettingsActivity::class.java)
                            startActivity(settingsIntent)
                        }
                    }
                }

                val dialog = AlertDialog.Builder(ctx)
                    .setItems(choices, onSelectionListener)
                    .create()
                dialog.window?.attributes?.apply {
                    gravity = Gravity.TOP
                    x = settingsView.x.toInt()
                    y = settingsView.y.toInt()
                }

                dialog.show()
            }
        }
    }

}