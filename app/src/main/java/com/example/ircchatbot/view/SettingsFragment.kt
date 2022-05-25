package com.example.ircchatbot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ircchatbot.R

class SettingsFragment : Fragment() {

    companion object {
        const val TAG = "SettingsFragment"

        fun newInstance() = SettingsFragment()
    }


    /**
     * [Fragment] functions
     * */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}