package com.example.ircchatbot

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ircchatbot.view.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class MainActivity : AppCompatActivity() {


    /**
     * [AppCompatActivity] functions
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTabs()
        KeyboardVisibilityEvent.setEventListener(
            activity = this,
            listener = object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    tabs.visibility = if (isOpen) View.GONE else View.VISIBLE
                }
            }
        )
    }

    /**
     * Private functions
     * */
    private fun setupTabs() {
        pager.adapter = PagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        tabs.setupWithViewPager(pager)
        tabs.getTabAt(0)?.setIcon(R.drawable.selector_home_tab)
        tabs.getTabAt(1)?.setIcon(R.drawable.selector_chat_tab)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                hideSoftKeyboard()
            }

            override fun onPageSelected(position: Int) {
                // no code needed here
            }

            override fun onPageScrollStateChanged(state: Int) {
                // no code needed here
            }
        })
    }

    private fun hideSoftKeyboard() {
        val focusedView = currentFocus
        if (focusedView != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

            inputMethodManager?.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

}
