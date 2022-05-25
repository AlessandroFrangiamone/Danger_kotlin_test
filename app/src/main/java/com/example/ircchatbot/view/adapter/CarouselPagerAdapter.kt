package com.example.ircchatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.ircchatbot.R
import com.example.ircchatbot.model.bankaccount.BankAccount
import com.example.ircchatbot.util.BankUtils
import com.example.ircchatbot.view.custom.CarouselViewPager
import kotlinx.android.synthetic.main.item_bank_account_carousel.view.*
import kotlinx.android.synthetic.main.item_bank_account_carousel_shimmer.view.*
import timber.log.Timber

class CarouselPagerAdapter : PagerAdapter(), CarouselViewPager.CarouselAdapter {


    private var mShimmerMode = false

    private val mCardList: MutableList<BankAccount> = mutableListOf()
    private val mViewList: MutableList<View> = mutableListOf()

    /**
     * [PagerAdapter] functions
     * */
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return mCardList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (mShimmerMode) {
            val shimmerView = LayoutInflater.from(container.context)
                .inflate(R.layout.item_bank_account_carousel_shimmer, container, false)
            shimmerView.cardCarouselShimmer.startShimmer()
            shimmerView.tag = position
            mViewList.add(shimmerView)
            container.addView(shimmerView)

            return shimmerView
        } else {
            val view = LayoutInflater.from(container.context)
                .inflate(R.layout.item_bank_account_carousel, container, false)
            try {
                val currentAccount = mCardList[position]
                view.bankAccountName.text = currentAccount.nickname ?: currentAccount.name.orEmpty()
                view.bankAccountNumber.text = getBankAccountSafeNumber(currentAccount)
                view.bankAccountIban.text = currentAccount.iban.orEmpty()
                view.bankAccountBalance.text = getBankAccountBalance(currentAccount)
                val bankAccountLogo = getBankAccountLogoResource(currentAccount)
                if (bankAccountLogo != null) {
                    view.bankAccountCircuit.setBackgroundResource(bankAccountLogo)
                }
                view.tag = position
                mViewList.add(view)
                container.addView(view)
            } catch (e: Exception) {
                Timber.d("instantiateItem exception")
            }

            return view
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as? View)
    }

    /**
     * [CarouselViewPager.CarouselAdapter] functions
     * */
    override fun getRealCount(): Int {
        return mCardList.size
    }

    override fun getRealPosition(position: Int): Int {
        return position % mCardList.size
    }


    /**
     * Public members
     * */
    fun startCarouselShimmer() {
        mShimmerMode = true
        mCardList.add(BankAccount())
    }

    fun setData(cardList: List<BankAccount>) {
        mShimmerMode = false
        mCardList.addAll(cardList)
    }

    fun editAdjacentViewsVisibility(isVisible: Boolean, position: Int) {
        mViewList.find { it.tag == position - 1 }?.animate()
            ?.alpha(if (isVisible) 1f else 0f)?.duration = 200L
        mViewList.find { it.tag == position + 1 }?.animate()
            ?.alpha(if (isVisible) 1f else 0f)?.duration = 200L
    }


    /**
     * Private members
     * */
    private fun getBankAccountSafeNumber(card: BankAccount): String {
        val stringBuilder = StringBuilder()
        if (card.number != null && card.number.length == 16) {
            stringBuilder.append(card.number.substring(startIndex = 0, endIndex = 4))
            stringBuilder.append(" **** **** ")
            stringBuilder.append(card.number.substring(startIndex = 12))
        }
        return stringBuilder.toString()
    }

    private fun getBankAccountBalance(card: BankAccount): String {
        val currency = BankUtils.getBankAccountCurrencySymbol(card)

        return "${card.balance} $currency"
    }

    private fun getBankAccountLogoResource(card: BankAccount): Int? {
        return when (card.circuit) {
            "VISA" -> R.drawable.ic_visa_logo
            "MASTERCARD" -> R.drawable.ic_mastercard_logo
            "AMERICANEXPRESS" -> R.drawable.ic_amex_logo
            "DINERS" -> R.drawable.ic_diners_logo
            "BANCOMAT" -> R.drawable.ic_diners_logo
            else -> null
        }
    }

}