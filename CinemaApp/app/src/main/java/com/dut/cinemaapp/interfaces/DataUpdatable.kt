package com.dut.cinemaapp.interfaces

import com.dut.cinemaapp.adapters.ViewPagerAdapter

interface DataUpdatable {
    fun updateData(holder: ViewPagerAdapter.Pager2ViewHolder)
}