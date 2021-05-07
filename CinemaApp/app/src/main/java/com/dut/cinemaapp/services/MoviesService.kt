package com.dut.cinemaapp.services

import com.dut.cinemaapp.domain.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class MoviesService {

    fun getMovies(): Call<List<Movie>> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getMovies("Bearer_" + AccountService.Singleton.getInstance()?.token!!)
    }

    fun getMovie(id: Long): Call<Movie> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getMovie(
                id,
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )
    }

    private interface ApiCaller {
        @GET("movies/all")
        fun getMovies(
            @Header("Authorization") token: String
        ): Call<List<Movie>>

        @GET("movies/{id}")
        fun getMovie(
            @Path("id") movieId: Long,
            @Header("Authorization") token: String
        ): Call<Movie>
    }
}