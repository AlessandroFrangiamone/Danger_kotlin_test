package com.example.ircchatbot.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.ircchatbot.view.ChatFragment
import com.example.ircchatbot.view.LandingPageFragment

class PagerAdapter(fragmentManager: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fragmentManager, behavior) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LandingPageFragment.newInstance()
            1 -> ChatFragment.newInstance()
            else -> throw Exception("Add fragment for tab position $position")
        }
    }

    override fun getCount(): Int {
        return 2
    }
}