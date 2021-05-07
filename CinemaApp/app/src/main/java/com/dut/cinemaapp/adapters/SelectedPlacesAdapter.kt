package com.dut.cinemaapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dut.cinemaapp.R
import com.dut.cinemaapp.domain.Place
import kotlinx.android.synthetic.main.selected_ticket.view.*

class SelectedPlacesAdapter(private val selectedPlaces: MutableList<Place>) :
    RecyclerView.Adapter<SelectedPlacesAdapter.SelectedPlaceViewHolder>() {

    inner class SelectedPlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.selected_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPlaceViewHolder {
        return SelectedPlaceViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.selected_ticket,
                    parent,
                    false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SelectedPlaceViewHolder, position: Int) {
        val currentItem = selectedPlaces[position]

        holder.textView.text = "Row: " + currentItem.row + "\nPlace: " + currentItem.place
    }

    override fun getItemCount(): Int {
        return selectedPlaces.size
    }
}