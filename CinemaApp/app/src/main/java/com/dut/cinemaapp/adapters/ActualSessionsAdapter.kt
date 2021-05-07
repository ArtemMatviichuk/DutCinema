package com.dut.cinemaapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dut.cinemaapp.R
import com.dut.cinemaapp.activities.LoginActivity
import com.dut.cinemaapp.activities.MovieActivity
import com.dut.cinemaapp.domain.Session
import com.dut.cinemaapp.interfaces.DataUpdatable
import com.dut.cinemaapp.services.AccountService
import com.dut.cinemaapp.services.SessionsService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.loading_comp.*
import kotlinx.android.synthetic.main.session_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ActualSessionsAdapter(
    private val sessionsList: List<Session>,
    private val activityContext: Context
) :
    RecyclerView.Adapter<ActualSessionsAdapter.ActualSession>(), DataUpdatable {

    inner class ActualSession(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.session_image
        val movieTitle: TextView = itemView.session_title
        val date: TextView = itemView.session_date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActualSession {
        return ActualSession(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.session_item,
                    parent,
                    false
                )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ActualSession, position: Int) {
        val currentItem = sessionsList[position]

        Picasso.get().load(currentItem.moviePoster).into(holder.imageView)
        holder.movieTitle.text = currentItem.movieTitle

        holder.date.text = SimpleDateFormat("HH:mm\nd MMM")
            .format(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(currentItem.date)
            )

        holder.itemView.setOnClickListener {
            onClickListener(currentItem.movieId)
        }
    }

    override fun getItemCount(): Int {
        return sessionsList.size
    }

    override fun updateData(holder: ViewPagerAdapter.Pager2ViewHolder) {
        SessionsService().getSessions().enqueue(object : Callback<List<Session>> {
            @SuppressLint("ShowToast")
            override fun onFailure(call: Call<List<Session>>?, t: Throwable?) {
                Toast.makeText(activityContext, t?.message, Toast.LENGTH_SHORT).show()
                holder.swipe.isRefreshing = false
                (activityContext as AppCompatActivity).loading.visibility = View.GONE
            }

            @SuppressLint("ShowToast")
            override fun onResponse(
                call: Call<List<Session>>?,
                response: Response<List<Session>>?
            ) {
                if (response?.isSuccessful!!)
                    holder.recycler.adapter =
                        ActualSessionsAdapter(
                            response.body() as MutableList<Session>,
                            activityContext
                        )
                else if (response.code() == 403)
                    AccountService.Singleton.getInstance()?.logout {
                        activityContext.startActivity(
                            Intent(
                                activityContext,
                                LoginActivity::class.java
                            )
                        )
                    }
                else
                    Toast.makeText(
                        activityContext,
                        "Error " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                (activityContext as AppCompatActivity).loading.visibility = View.GONE
                holder.swipe.isRefreshing = false
            }
        })
    }

    private fun onClickListener(id: Long) {
        var intent = Intent(activityContext, MovieActivity::class.java)
        intent.putExtra("id", id)
        activityContext.startActivity(intent)
    }
}