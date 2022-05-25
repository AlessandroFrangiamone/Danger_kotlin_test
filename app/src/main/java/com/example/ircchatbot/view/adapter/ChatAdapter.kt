package com.example.ircchatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ircchatbot.R
import com.example.ircchatbot.util.AdapterRowWrapper
import kotlinx.android.synthetic.main.item_incoming_message_recycler_view.view.*
import kotlinx.android.synthetic.main.item_outgoing_message_recycler_view.view.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mItemList = mutableListOf<AdapterRowWrapper<Any>>()


    /**
     * [RecyclerView.Adapter] functions
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            INCOMING_MESSAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_incoming_message_recycler_view, parent, false)
                IncomingViewHolder(view)
            }
            OUTGOING_MESSAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_outgoing_message_recycler_view, parent, false)
                OutgoingViewHolder(view)
            }
            else -> throw IllegalStateException("ViewType ($viewType) does not exist")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IncomingViewHolder -> holder.bind(mItemList[position].data as? MessageData)
            is OutgoingViewHolder -> holder.bind(mItemList[position].data as? MessageData)
        }
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItemList[position].rowType
    }


    /**
     * Public members
     * */
    fun setData(itemList: List<AdapterRowWrapper<Any>>) {
        mItemList.clear()
        mItemList.addAll(itemList)
        notifyDataSetChanged()
    }

    fun addMessage(messageData: MessageData, type: Int) {
        if (mItemList.isNotEmpty() && mItemList.first().rowType == type) {
            (mItemList.first().data as? MessageData)?.isLastGroupMessage = false
            notifyItemChanged(0, Unit)
        }
        mItemList.add(0, AdapterRowWrapper(type, messageData))
        notifyItemInserted(0)
    }


    /**
     * ViewHolders
     * */
    private inner class IncomingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: MessageData?) {
            if (data?.messageText != null && data.messageText.isNotEmpty()) {
                view.incomingMessage.text = data.messageText
                if (data.isLastGroupMessage) {
                    view.incomingMessage.setBackgroundResource(R.drawable.ic_incoming_message_background)
                } else {
                    view.incomingMessage.setBackgroundResource(R.drawable.ic_incoming_message_no_triangle_background)
                }
            }
        }
    }

    private inner class OutgoingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: MessageData?) {
            if (data?.messageText != null && data.messageText.isNotEmpty()) {
                view.outgoingMessage.text = data.messageText
                if (data.isLastGroupMessage) {
                    view.outgoingMessage.setBackgroundResource(R.drawable.ic_outgoing_message_background)
                } else {
                    view.outgoingMessage.setBackgroundResource(R.drawable.ic_outgoing_message_no_triangle_background)
                }
            }
        }
    }


    /**
     * DataClasses
     * */
    data class MessageData(
        val messageText: String,
        var isLastGroupMessage: Boolean = true
    )


    companion object {
        const val INCOMING_MESSAGE = 10
        const val OUTGOING_MESSAGE = 20
    }
}