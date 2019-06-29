package com.larvaesoft.opengst.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.larvaesoft.opengst.ui.about.AboutFragment
import com.larvaesoft.opengst.ui.calculator.CalcFragment
import com.larvaesoft.opengst.ui.gstrates.GstRatesFragment
import com.larvaesoft.opengst.ui.portal.GstPortalFragment
import com.larvaesoft.opengst.ui.updates.GstUpdateFragment

class MyFragmentAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentCount: Int = 5

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> {
                CalcFragment()
            }
            1 -> {
                GstRatesFragment()
            }
            2 -> {
                GstUpdateFragment()
            }
            3 -> {
                GstPortalFragment()
            }
            4 -> {
                AboutFragment()
            }
            else -> {
                GstPortalFragment()
            }
        }
    }

    override fun getCount(): Int {
        return fragmentCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> {
                "GST Calculator"
            }
            1 -> {
                "GST Rates"
            }
            2 -> {
                "GST News"
            }
            3 -> {
                "GST Portal"
            }
            else -> {
                "Open GST"
            }
        }
    }
}