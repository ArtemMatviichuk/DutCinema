package com.dut.cinemaapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dut.cinemaapp.R
import com.dut.cinemaapp.activities.MovieActivity
import com.dut.cinemaapp.domain.Place
import com.dut.cinemaapp.domain.Ticket
import com.dut.cinemaapp.domain.TicketView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ticket_item.view.*
import java.text.SimpleDateFormat

class TicketsAdapter :
    RecyclerView.Adapter<TicketsAdapter.TicketViewHolder> {

    private var ticketsList: MutableList<Ticket>? = null
    private var groupedList: MutableList<TicketView>? = null
    private lateinit var activityContext: Context

    @RequiresApi(Build.VERSION_CODES.N)
    constructor(ticketsList: List<Ticket>) {
        this.ticketsList = ticketsList.toMutableList()
        groupedList =
            ticketsList.groupBy { it.sessionId }.map { it -> mapToView(it) }.toMutableList()
    }

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var poster = itemView.ticket_poster
        var title = itemView.ticket_title
        var date = itemView.ticket_date
        var hall = itemView.ticket_hall
        var amount = itemView.ticket_amount
        var canceletionAlert = itemView.ticket_canceled
        var tickets = itemView.ticket_exact_list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        activityContext = parent.context
        return TicketViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.ticket_item,
                    parent,
                    false
                )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        var currentItem = groupedList?.get(position)

        Picasso.get().load(currentItem?.posterPath).into(holder.poster)

        holder.poster.setOnClickListener {
            var intent = Intent(activityContext, MovieActivity::class.java)
            intent.putExtra("id", currentItem?.movieId)
            activityContext.startActivity(intent)
        }

        holder.title.text = currentItem?.movieTitle
        holder.date.text = SimpleDateFormat("HH:mm\nd MMM")
            .format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(currentItem?.date)
            )
        holder.hall.text = currentItem?.hallName
        holder.amount.text = "Amount: " + currentItem?.amount.toString()

        if (currentItem?.isCanceled!!)
            holder.canceletionAlert.visibility = View.VISIBLE

        holder.tickets.adapter =
            TicketManagementAdapter(currentItem.places, currentItem.sessionId) { id ->
                groupedList?.removeIf { return@removeIf it.sessionId == id }
                this.notifyDataSetChanged()
            }
        holder.tickets.layoutManager = LinearLayoutManager(activityContext)
    }

    override fun getItemCount(): Int {
        return groupedList?.size!!
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun mapToView(it: Map.Entry<Long, List<Ticket>>): TicketView {
        var places = mutableListOf<Pair<Long, Place>>()

        for (i in it.value.indices) {
            places.add(Pair(it.value[i].id, Place(it.value[i].row, it.value[i].place)))
        }

        return TicketView(
            it.key,
            it.value[0].movieId,
            it.value[0].movieTitle,
            it.value[0].date,
            it.value[0].hallName,
            it.value[0].posterPath,
            it.value.size,
            it.value[0].isCanceled,
            places
        )
    }
}