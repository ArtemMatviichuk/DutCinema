package com.dut.cinemaapp.repsenters

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dut.cinemaapp.R
import com.dut.cinemaapp.adapters.ReviewsAdapter
import com.dut.cinemaapp.domain.Movie
import com.dut.cinemaapp.domain.Review
import com.dut.cinemaapp.interfaces.Representer
import com.dut.cinemaapp.services.MoviesService
import com.dut.cinemaapp.services.ReviewsService
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_layout.*
import kotlinx.android.synthetic.main.reviews_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepresenter(
    private val id: Long,
    private val context: YouTubeBaseActivity
) : Representer {

    lateinit var movie: Movie
    lateinit var reviews: List<Review>

    override fun loadData() {
        MoviesService().getMovie(id).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    movie = response.body() as Movie
                    setMovieData()
                    loadReviews()
                } else
                    Toast.makeText(
                        context,
                        "Error " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Toast.makeText(context, "Error while getting movie", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun loadReviews() {
        ReviewsService().getComments(movie.id).enqueue(object : Callback<List<Review>> {
            override fun onResponse(
                call: Call<List<Review>>,
                response: Response<List<Review>>
            ) {
                if (response.isSuccessful) {
                    reviews = response.body() as List<Review>
                    setReviewsData()
                } else
                    Toast.makeText(
                        context,
                        "Error " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
            }

            override fun onFailure(call: Call<List<Review>>, t1: Throwable) {
                Toast.makeText(
                    context,
                    "Error while getting reviews",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieData() {
        context.movieTrailer.initialize(
            context.getString(R.string.youtubeApi),
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    wasRestored: Boolean
                ) {
                    when {
                        player == null -> return
                        wasRestored -> player.play()
                        else -> {
                            player.cueVideo(movie.trailerPath)
                            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        }
                    }
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    context.movieTrailer.layoutParams.height = 0
                }
            })

        Picasso.get().load(movie.posterPath).into(context.moviePoster)
        context.movieTitle.text = movie.title

        var h = (movie.duration / 3600).toString()
        var min = (movie.duration % 3600) / 60
        context.movieDuration.text = h + "h " + min + "min"

        var genres = ""
        movie.genres.forEach { e -> genres += e.name + ", " }

        if (genres.endsWith(", "))
            genres = genres.substring(0, genres.length - 2)

        context.movieGenres.text = genres

        context.movieDescription.text = movie.description
        context.movieActors.text = movie.actors
        context.movieCountry.text = movie.country
    }

    private fun setReviewsData() {
        if (reviews.count() > 0)
            context.reviewLabel.text = "Reviews:"
        else
            context.reviewLabel.text = "No reviews for now"

        context.reviews_recycler.adapter = ReviewsAdapter(reviews)
        context.reviews_recycler.layoutManager = LinearLayoutManager(context)
    }
}