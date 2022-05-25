package com.example.ircchatbot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ircchatbot.R
import com.example.ircchatbot.view.adapter.ChatAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    companion object {
        fun newInstance() = ChatFragment()
    }


    private val mAdapter = ChatAdapter()


    /**
     * [Fragment] functions
     * */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRecyclerView.adapter = mAdapter
        chatSendButton.setOnClickListener {
            if (chatMessage.text.isNotEmpty()) {
                mAdapter.addMessage(messageData = ChatAdapter.MessageData(messageText = chatMessage.text.toString()), type = ChatAdapter.OUTGOING_MESSAGE)
                chatMessage.text.clear()
            } else {
                generateButtonChoices(listOf("ciao", "ciao2"))
            }
        }
    }


    /**
     * Private functions
     * */
    private fun generateButtonChoices(choiceList: List<String>) {
        buttonChoicesContainer.removeAllViews()
        choiceList.forEach { choice ->
            val button = Button(buttonChoicesContainer.context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(0, 10, 0, 10)
                }
                setBackgroundResource(R.drawable.ic_incoming_message_no_triangle_background)
                text = choice
                setTextColor(ContextCompat.getColor(buttonChoicesContainer.context, android.R.color.white))
                setOnClickListener {
                    mAdapter.addMessage(messageData = ChatAdapter.MessageData(messageText = choice), type = ChatAdapter.OUTGOING_MESSAGE)
                    buttonChoicesContainer.visibility = View.GONE
                    chatInputContainer.visibility = View.VISIBLE
                }
            }
            buttonChoicesContainer.addView(button)
        }
        buttonChoicesContainer.visibility = View.VISIBLE
        chatInputContainer.visibility = View.GONE
    }

}