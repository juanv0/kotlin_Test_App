package com.example.justforvolley
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PageFragmentAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment? = when (position){
        0 -> FixtureFragment.newInstance()
        1 -> ResultFragment.newInstance()
        else -> null

    }
    override fun getPageTitle(position: Int): CharSequence? = when (position){
        0 -> "FIXTURES"
        1 -> "RESULTS"
        else -> ""
    }
    override fun getCount(): Int = 2
}