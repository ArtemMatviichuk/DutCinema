package com.dut.cinemaapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.dut.cinemaapp.R
import com.dut.cinemaapp.adapters.ViewPagerAdapter
import com.dut.cinemaapp.services.AccountService
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*


class MainActivity : AppCompatActivity() {

    private var titles = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tool_bar.title = ""
        tool_bar_title.text = "DutCinema"
        setActionBar(tool_bar)

        tool_bar_btn.text = "Logout"
        tool_bar_btn.setOnClickListener {
            AccountService.Singleton.getInstance()?.logout {
                this.startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        toolbar_acc_btn.setOnClickListener {
            this.startActivity(Intent(this, AccountActivity::class.java))
        }

        postToList()

        view_pager2.adapter = ViewPagerAdapter(titles.size)
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(tabLayout, view_pager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun postToList() {
        titles.add("Actual sessions")
        titles.add("All movies")
    }
}