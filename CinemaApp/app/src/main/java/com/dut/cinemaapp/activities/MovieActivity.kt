package com.dut.cinemaapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dut.cinemaapp.R
import com.dut.cinemaapp.domain.Review
import com.dut.cinemaapp.dto.review.NewReview
import com.dut.cinemaapp.repsenters.MovieRepresenter
import com.dut.cinemaapp.services.AccountService
import com.dut.cinemaapp.services.ReviewsService
import com.google.android.youtube.player.YouTubeBaseActivity
import kotlinx.android.synthetic.main.loading_comp.*
import kotlinx.android.synthetic.main.movie_layout.*
import kotlinx.android.synthetic.main.review_create.*
import kotlinx.android.synthetic.main.reviews_layout.*
import kotlinx.android.synthetic.main.tool_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieActivity : YouTubeBaseActivity() {

    var id: Long = -1
    private lateinit var movieRepresenter: MovieRepresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        tool_bar.title = ""
        setActionBar(tool_bar)

        tool_bar_btn.setOnClickListener { onBackPressed() }

        toolbar_acc_btn.setOnClickListener {
            this.startActivity(Intent(this, AccountActivity::class.java))
        }

        id = intent.extras?.getLong("id")!!

        initializeRepresenter()
        setOnCreateListener()
        setSessionsButton()
    }

    private fun initializeRepresenter() {
        movieRepresenter = MovieRepresenter(
            id,
            this,
        )

        movieRepresenter.loadData()
    }

    private fun setOnCreateListener() {
        reviewCreateButton.setOnClickListener {
            ReviewsService().createReview(
                NewReview(
                    reviewCreateText.text.toString(), id,
                    AccountService.Singleton.getInstance()?.id!!
                )
            )
                .enqueue(
                    object :
                        Callback<Review> {
                        override fun onResponse(call: Call<Review>, response: Response<Review>) {
                            if (response.isSuccessful) {
                                movieRepresenter.loadReviews()
                                reviewCreateText.setText("")
                            } else
                                Toast.makeText(
                                    this@MovieActivity,
                                    "Error " + response.code().toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                        }

                        override fun onFailure(call: Call<Review>, t: Throwable) {
                            Toast.makeText(
                                this@MovieActivity,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSessionsButton() {
        movieSessions.text = "Sessions"
        movieSessions.setOnClickListener {
            val intent = Intent(this, SessionShortActivity::class.java)
            intent.putExtra("title", movieRepresenter.movie.title)
            intent.putExtra("id", movieRepresenter.movie.id)
            this.startActivity(intent)
        }
    }
}