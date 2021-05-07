package com.dut.cinemaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dut.cinemaapp.R
import com.dut.cinemaapp.interfaces.DataUpdatable
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter(private var amount: Int) :
    RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    private lateinit var activityContext: Context

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recycler: RecyclerView = itemView.recycler
        val swipe: SwipeRefreshLayout = itemView.swipeRefresh
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        val holder = Pager2ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_page,
                    parent,
                    false
                )
        )

        activityContext = parent.context
        holder.recycler.layoutManager = LinearLayoutManager(parent.context)
        holder.recycler.setHasFixedSize(true)

        return holder
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        if (position == 0){
            holder.recycler.adapter = ActualSessionsAdapter(mutableListOf(), activityContext)
        }
        else if (position == 1){
            holder.recycler.adapter = AllMoviesAdapter(mutableListOf(), activityContext)
        }


        holder.swipe.setOnRefreshListener {
            (holder.recycler.adapter as DataUpdatable).updateData(holder)
        }

        (holder.recycler.adapter as DataUpdatable).updateData(holder)
    }

    override fun getItemCount(): Int {
        return amount
    }
}