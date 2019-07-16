package com.example.justforvolley
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
//import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val adapter= PageFragmentAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter
        var tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.removeAllTabs()


        tabs.addTab(tabs.newTab().setText("FIXTURES"))
        tabs.addTab(tabs.newTab().setText("RESULTS"))

    }



}
