package com.dut.cinemaapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dut.cinemaapp.R
import com.dut.cinemaapp.adapters.TicketsAdapter
import com.dut.cinemaapp.domain.Ticket
import com.dut.cinemaapp.domain.UserLogged
import com.dut.cinemaapp.services.AccountService
import com.dut.cinemaapp.services.TicketsService
import com.dut.cinemaapp.services.UserService
import kotlinx.android.synthetic.main.activity_account_page.*
import kotlinx.android.synthetic.main.tool_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*

class AccountActivity : AppCompatActivity() {
    var user: UserLogged? = null
    var actualAdapter: TicketsAdapter? = null
    var historyAdapter: TicketsAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_page)

        tool_bar_title.text = "Account"
        setActionBar(tool_bar)

        tool_bar_btn.text = "Main"
        tool_bar_btn.setOnClickListener {
            this.startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        toolbar_acc_btn.text = "Logout"
        toolbar_acc_btn.setOnClickListener {
            AccountService.Singleton.getInstance()?.logout {
                this.startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        getUser()

        account_radio_group.setOnCheckedChangeListener { group, checkedId ->
            if (account_tickets.adapter?.equals(actualAdapter)!!)
                account_tickets.adapter = historyAdapter
            else
                account_tickets.adapter = actualAdapter
        }
    }

    private fun getUser() {
        UserService().getUserData().enqueue(object : Callback<UserLogged> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<UserLogged>, response: Response<UserLogged>) {
                if (response.isSuccessful) {
                    user = response.body()

                    account_username.text = user?.firstName + ' ' + user?.lastName
                    account_email.text = user?.email

                    TicketsService().getUserTickets().enqueue(object : Callback<List<Ticket>> {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onResponse(
                            call: Call<List<Ticket>>,
                            response: Response<List<Ticket>>
                        ) {
                            if (response.isSuccessful) {
                                var tickets = response.body()

                                actualAdapter = TicketsAdapter(tickets?.filter {
                                    LocalDateTime.parse(it.date).isAfter(LocalDateTime.now())
                                }!!)
                                historyAdapter = TicketsAdapter(tickets.filter {
                                    LocalDateTime.parse(it.date).isBefore(LocalDateTime.now())
                                })

                                account_tickets.adapter = actualAdapter

                                account_tickets.layoutManager =
                                    LinearLayoutManager(this@AccountActivity)
                            } else {
                                Toast.makeText(
                                    this@AccountActivity,
                                    "Error: " + response.code().toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                            Toast.makeText(this@AccountActivity, t.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    })

                } else
                    Toast.makeText(
                        this@AccountActivity,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
            }

            override fun onFailure(call: Call<UserLogged>, t: Throwable) {
                Toast.makeText(this@AccountActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}