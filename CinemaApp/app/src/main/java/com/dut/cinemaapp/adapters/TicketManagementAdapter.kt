package com.dut.cinemaapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dut.cinemaapp.R
import com.dut.cinemaapp.domain.Place
import com.dut.cinemaapp.services.TicketsService
import kotlinx.android.synthetic.main.ticket_exact.view.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TicketManagementAdapter(
    private val places: MutableList<Pair<Long, Place>>,
    private val sessionId: Long,
    private val onZeroTickets: (id: Long) -> Unit
) :
    RecyclerView.Adapter<TicketManagementAdapter.TicketManagementViewHolder>() {

    private lateinit var activityContext: Context

    inner class TicketManagementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowTv = itemView.ticket_exact_row
        val placeTv = itemView.ticket_exact_place
        val deleteBtn = itemView.ticket_exact_btn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketManagementViewHolder {
        activityContext = parent.context
        return TicketManagementViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.ticket_exact,
                    parent,
                    false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TicketManagementViewHolder, position: Int) {
        val currentItem = places[position]

        holder.rowTv.text = "Row: " + currentItem.second.row.toString()
        holder.placeTv.text = "Place: " + currentItem.second.place.toString()

        holder.deleteBtn.setOnClickListener {
            TicketsService().removeBooking(currentItem.first).enqueue(object : Callback,
                retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        places.remove(currentItem)
                        this@TicketManagementAdapter.notifyDataSetChanged()
                        if (places.size == 0) {
                            onZeroTickets.invoke(sessionId)
                        }
                    } else
                        Toast.makeText(
                            activityContext,
                            "Error: " + response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        activityContext,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }
}