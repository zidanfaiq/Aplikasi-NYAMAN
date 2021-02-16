package com.zidanfaiq.percobaan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zidanfaiq.percobaan.ui.home.tablayout.MenuFragment
import com.zidanfaiq.percobaan.ui.home.tablayout.RestoFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                RestoFragment()
            }
            else -> {
                return MenuFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Rumah Makan"
            else -> {
                return "Menu Makan"
            }
        }
    }
}