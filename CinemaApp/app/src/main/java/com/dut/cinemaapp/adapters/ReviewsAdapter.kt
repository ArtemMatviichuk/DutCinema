package com.dut.cinemaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dut.cinemaapp.R
import com.dut.cinemaapp.domain.Review
import kotlinx.android.synthetic.main.review_layout.view.*

class ReviewsAdapter(private val reviewList: List<Review>) :
    RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    private lateinit var activityContext: Context

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val author: TextView = itemView.reviewAuthor
        val date: TextView = itemView.reviewDate
        val text: TextView = itemView.reviewText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        activityContext = parent.context
        return ReviewViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.review_layout,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentItem = reviewList[position]

        holder.author.text = currentItem.firstName + " " + currentItem.lastName
        holder.date.text = currentItem.creationDate
        holder.text.text = currentItem.text
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}