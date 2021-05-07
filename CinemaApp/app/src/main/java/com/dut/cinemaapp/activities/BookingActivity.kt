package com.dut.cinemaapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dut.cinemaapp.R
import com.dut.cinemaapp.adapters.SelectedPlacesAdapter
import com.dut.cinemaapp.domain.*
import com.dut.cinemaapp.repsenters.BookingRepresenter
import com.dut.cinemaapp.services.TicketsService
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.place.*
import kotlinx.android.synthetic.main.tool_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingActivity : AppCompatActivity() {
    var representer: BookingRepresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        tool_bar_title.text = ""
        setActionBar(tool_bar)

        tool_bar_btn.setOnClickListener { onBackPressed() }

        toolbar_acc_btn.setOnClickListener {
            this.startActivity(Intent(this, AccountActivity::class.java))
        }

        representer = BookingRepresenter(intent.getLongExtra("sessionId", -1L), this)
        representer!!.loadData()

        booking_chosen_places.adapter = SelectedPlacesAdapter(representer!!.chosenTickets)
        booking_chosen_places.layoutManager = LinearLayoutManager(this)

        setOnBookListener()
    }

    private fun setOnBookListener()
    {
        booking_btn.setOnClickListener {
            TicketsService().bookTickets(representer?.session?.id!!, representer!!.chosenTickets).enqueue(object: Callback<List<Ticket>>{
                override fun onResponse(
                    call: Call<List<Ticket>>,
                    response: Response<List<Ticket>>
                ) {
                    if (response.isSuccessful)
                    {
                        this@BookingActivity.startActivity(Intent(this@BookingActivity, AccountActivity::class.java))
                    }
                    else {
                        Toast.makeText(
                            this@BookingActivity,
                            "Error: " + response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                    Toast.makeText(
                        this@BookingActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

}