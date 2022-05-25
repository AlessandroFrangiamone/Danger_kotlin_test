package com.example.ircchatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ircchatbot.R
import com.example.ircchatbot.util.AdapterRowWrapper
import kotlinx.android.synthetic.main.item_operations_recycler_view.view.*
import kotlinx.android.synthetic.main.item_operations_shimmer_recycler_view.view.*

class OperationsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mShimmerMode = false

    private val mItemList = mutableListOf<AdapterRowWrapper<Any>>()


    /**
     * [RecyclerView.Adapter] functions
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            OPERATION_SHIMMER_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_operations_shimmer_recycler_view, parent, false)
                OperationShimmerViewHolder(view)
            }
            OPERATION_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_operations_recycler_view, parent, false)
                OperationViewHolder(view)
            }
            else -> throw IllegalStateException("ViewType ($viewType) does not exist")
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OperationShimmerViewHolder -> holder.bind()
            is OperationViewHolder -> holder.bind(mItemList[position].data as? OperationData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mItemList[position].rowType
    }


    /**
     * Public members
     * */
    fun setData(itemList: List<AdapterRowWrapper<Any>>) {
        mShimmerMode = false
        mItemList.clear()
        mItemList.addAll(itemList)
        notifyDataSetChanged()
    }

    fun startShimmer() {
        if (!mShimmerMode) {
            mShimmerMode = true
            mItemList.clear()
            mItemList.add(AdapterRowWrapper(rowType = OPERATION_SHIMMER_TYPE, data = null))
            mItemList.add(AdapterRowWrapper(rowType = OPERATION_SHIMMER_TYPE, data = null))
            mItemList.add(AdapterRowWrapper(rowType = OPERATION_SHIMMER_TYPE, data = null))
            notifyDataSetChanged()
        }
    }


    /**
     * ViewHolders
     * */
    private class OperationShimmerViewHolder(val view: View)  : RecyclerView.ViewHolder(view) {
        fun bind() {
            view.operationShimmerLayout.startShimmer()
        }
    }

    private class OperationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: OperationData?) {
            view.operationDescription.text = data?.operationDescription.orEmpty()
            view.operationAmount.text = data?.operationAmount.orEmpty()
        }
    }


    /**
     * DataClasses
     * */
    data class OperationData(
        val operationId: String?,
        val operationDescription: String?,
        val operationAmount: String?
    )


    companion object {
        const val OPERATION_SHIMMER_TYPE = 0
        const val OPERATION_TYPE = 10
    }
}