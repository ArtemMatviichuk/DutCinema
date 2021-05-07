package com.dut.cinemaapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dut.cinemaapp.R
import com.dut.cinemaapp.adapters.SessionShortAdapter
import com.dut.cinemaapp.domain.SessionShort
import com.dut.cinemaapp.services.SessionsService
import kotlinx.android.synthetic.main.activity_movie_sessions.*
import kotlinx.android.synthetic.main.tool_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionShortActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_sessions)

        //tool_bar_title.text = intent.getStringExtra("title")
        setActionBar(tool_bar)
        tool_bar_btn.setOnClickListener { onBackPressed() }

        setSessionsData()
    }

    private fun setSessionsData(){
        SessionsService().getShortSessions(intent.getLongExtra("id",-1L)).enqueue(object:
            Callback<List<SessionShort>>{
            override fun onResponse(
                call: Call<List<SessionShort>>,
                response: Response<List<SessionShort>>
            ) {
                if (response.isSuccessful){
                    var sessions = response.body() as List<SessionShort>
                    
                    if (sessions.isEmpty())
                        no_sessions_text.visibility = View.VISIBLE

                    short_sessions.adapter = SessionShortAdapter(sessions, this@SessionShortActivity)
                    short_sessions.layoutManager = LinearLayoutManager(this@SessionShortActivity)
                }
                else
                    Toast.makeText(
                        this@SessionShortActivity,
                        "Error " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()

            }

            override fun onFailure(call: Call<List<SessionShort>>, t: Throwable) {
                Toast.makeText(
                    this@SessionShortActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}