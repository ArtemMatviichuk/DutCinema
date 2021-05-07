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
import com.dut.cinemaapp.activities.MovieActivity
import com.dut.cinemaapp.domain.Movie
import com.dut.cinemaapp.interfaces.DataUpdatable
import com.dut.cinemaapp.services.MoviesService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.loading_comp.*
import kotlinx.android.synthetic.main.movie_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMoviesAdapter(private val movieList: List<Movie>, private val activityContext: Context) :
    RecyclerView.Adapter<AllMoviesAdapter.MovieViewHolder>(),
    DataUpdatable {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.movie_poster
        val movieTitle: TextView = itemView.movie_title
        val genres: TextView = itemView.movie_genres
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.movie_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = movieList[position]

        Picasso.get().load(currentItem.posterPath).into(holder.imageView)
        holder.movieTitle.text = currentItem.title

        var genres = ""

        currentItem.genres.forEach { e -> genres += e.name + ", " }

        if (genres.endsWith(", "))
            genres = genres.substring(0, genres.length - 2)

        holder.genres.text = genres

        holder.itemView.setOnClickListener {
            onClickListener(currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun updateData(holder: ViewPagerAdapter.Pager2ViewHolder) {
        MoviesService().getMovies().enqueue(object : Callback<List<Movie>> {
            @SuppressLint("ShowToast")
            override fun onFailure(call: Call<List<Movie>>?, t: Throwable?) {
                Toast.makeText(activityContext, t?.message, Toast.LENGTH_SHORT).show()
                holder.swipe.isRefreshing = false
            }

            @SuppressLint("ShowToast")
            override fun onResponse(call: Call<List<Movie>>?, response: Response<List<Movie>>?) {
                if (response?.isSuccessful!!) {
                    holder.recycler.adapter =
                        AllMoviesAdapter(response.body() as MutableList<Movie>, activityContext)
                } else
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