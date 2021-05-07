package com.dut.cinemaapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.dut.cinemaapp.R
import com.dut.cinemaapp.domain.Place


class PlaceAdapter(
    private val context: Context,
    private val tickets: List<Place>,
    private val rows: Int,
    private val places: Int,
    private val chosenTickets: MutableList<Place>,
    private val onSelect: () -> Unit
) : BaseAdapter() {

    override fun getCount(): Int {
        return rows * places
    }

    override fun getItem(position: Int): Any {
        return getPlaceFromPosition(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val place: ImageView

        if (convertView == null) {
            place = ImageView(context)
            place.setImageResource(R.mipmap.ic_place_foreground)
        } else {
            place = convertView as ImageView
        }

        place.id = position

        place.layoutParams = LinearLayout.LayoutParams(80, 80)

        place.setPadding(1, 1, 1, 1)

        if (tickets.contains(getPlaceFromPosition(position))) {
            place.isEnabled = false
            place.setBackgroundColor(R.color.black)
        }
        else
            place.background = context.getDrawable(R.drawable.transparent_bg)

        place.setOnClickListener {
            var tick = getPlaceFromPosition(position)
            if (chosenTickets.contains(tick)) {
                chosenTickets.remove(tick)
                place.background = context.getDrawable(R.drawable.transparent_bg)
                onSelect.invoke()
            } else {
                if (chosenTickets.size < 10){
                    place.background = context.getDrawable(R.drawable.selected_bg)
                    chosenTickets.add(tick)
                    onSelect.invoke()
                }
                else
                    Toast.makeText(
                        context,
                        "Too many selected tickets",
                        Toast.LENGTH_SHORT
                    ).show()
            }

        }

        return place
    }

    private fun getPlaceFromPosition(position: Int): Place {
        return Place((position / places) + 1, (position % places) + 1)
    }
}