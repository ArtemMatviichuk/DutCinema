package com.dut.cinemaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dut.cinemaapp.R
import com.dut.cinemaapp.activities.BookingActivity
import com.dut.cinemaapp.domain.SessionShort
import kotlinx.android.synthetic.main.session_short_item.view.*

class SessionShortAdapter(private val sessionsList: List<SessionShort>, private val activityContext: Context) :
    RecyclerView.Adapter<SessionShortAdapter.SessionShortViewHolder>() {

    inner class SessionShortViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hallName: TextView = itemView.session_short_hall
        val date: TextView = itemView.session_short_date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionShortViewHolder {
        return SessionShortViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.session_short_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: SessionShortViewHolder, position: Int) {
        val currentItem = sessionsList[position]

        holder.hallName.text = currentItem.hallName

        holder.date.text = currentItem.date

        holder.itemView.setOnClickListener {
            onClickListener(currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return sessionsList.size
    }

    private fun onClickListener(id: Long) {
        var intent = Intent(activityContext, BookingActivity::class.java)
        intent.putExtra("sessionId", id)
        activityContext.startActivity(intent)
    }
}