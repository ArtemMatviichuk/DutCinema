package com.dut.cinemaapp.repsenters

import android.annotation.SuppressLint
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dut.cinemaapp.adapters.PlaceAdapter
import com.dut.cinemaapp.domain.Place
import com.dut.cinemaapp.domain.Session
import com.dut.cinemaapp.domain.SessionTicketsList
import com.dut.cinemaapp.interfaces.Representer
import com.dut.cinemaapp.services.SessionsService
import kotlinx.android.synthetic.main.activity_booking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class BookingRepresenter(
    private val id: Long,
    private val context: AppCompatActivity
) : Representer {

    var session: Session? = null
    private var tickets: SessionTicketsList? = null
    var chosenTickets: MutableList<Place> = mutableListOf()

    override fun loadData() {
        SessionsService().getSession(id).enqueue(object : Callback<Session> {
            override fun onResponse(call: Call<Session>, response: Response<Session>) {
                if (response.isSuccessful) {
                    session = response.body()
                    SessionsService().getSessionTickets(id)
                        .enqueue(object : Callback<SessionTicketsList> {
                            @SuppressLint("ClickableViewAccessibility")
                            override fun onResponse(
                                call: Call<SessionTicketsList>,
                                response: Response<SessionTicketsList>
                            ) {
                                tickets = response.body()

                                context.booking_movie_title.text = session?.movieTitle
                                context.booking_session_hall.text = session?.hallName
                                context.booking_session_date.text = SimpleDateFormat("HH:mm\nd MMM")
                                    .format(
                                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                            .parse(session?.date!!)!!
                                    )

                                context.booking_places.numColumns = tickets?.place!!

                                context.booking_places.adapter = PlaceAdapter(
                                    context,
                                    tickets!!.tickets,
                                    tickets!!.rowsAmount,
                                    tickets!!.place,
                                    chosenTickets
                                ) { context.booking_chosen_places.adapter?.notifyDataSetChanged() }

                                context.booking_places.layoutParams = LinearLayout.LayoutParams(
                                    context.booking_places.width,
                                    tickets?.rowsAmount!! * 80,
                                )
                            }

                            override fun onFailure(call: Call<SessionTicketsList>, t: Throwable) {
                                Toast.makeText(
                                    context,
                                    t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                }
            }

            override fun onFailure(call: Call<Session>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}