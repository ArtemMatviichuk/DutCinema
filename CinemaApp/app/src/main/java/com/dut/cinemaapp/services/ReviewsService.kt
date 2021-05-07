package com.dut.cinemaapp.services

import com.dut.cinemaapp.domain.Review
import com.dut.cinemaapp.dto.review.NewReview
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ReviewsService {
    fun getComments(id: Long): Call<List<Review>> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getReviews(
                id,
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )
    }

    fun createReview(newReview: NewReview): Call<Review> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .createReview(
                newReview,
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )
    }

    private interface ApiCaller {
        @GET("reviews/movie/{id}")
        fun getReviews(
            @Path("id") movieId: Long,
            @Header("Authorization") token: String
        ): Call<List<Review>>

        @POST("reviews")
        fun createReview(
            @Body newReview: NewReview,
            @Header("Authorization") token: String
        ): Call<Review>
    }
}