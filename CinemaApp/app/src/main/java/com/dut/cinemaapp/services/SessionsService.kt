package com.dut.cinemaapp.services

import com.dut.cinemaapp.domain.Session
import com.dut.cinemaapp.domain.SessionShort
import com.dut.cinemaapp.domain.SessionTicketsList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class SessionsService {

    fun getSessions(): Call<List<Session>> {

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getSessions("Bearer_" + AccountService.Singleton.getInstance()?.token!!)
    }

    fun getSession(sessionId: Long): Call<Session> {

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getSession(sessionId,"Bearer_" + AccountService.Singleton.getInstance()?.token!!)
    }

    fun getSessionTickets(sessionId: Long): Call<SessionTicketsList> {

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getSessionTickets(sessionId,"Bearer_" + AccountService.Singleton.getInstance()?.token!!)
    }

    fun getShortSessions(movieId: Long): Call<List<SessionShort>> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getShortSessions(movieId, "Bearer_" + AccountService.Singleton.getInstance()?.token!!)
    }

    interface ApiCaller {
        @GET("sessions/actual")
        fun getSessions(@Header("Authorization") token: String): Call<List<Session>>

        @GET("movies/{id}/sessions")
        fun getShortSessions(
            @Path("id") movieId: Long,
            @Header("Authorization") token: String
        ): Call<List<SessionShort>>

        @GET("sessions/{id}")
        fun getSession(
            @Path("id") sessionId: Long,
            @Header("Authorization") token: String
        ): Call<Session>

        @GET("sessions/{id}/tickets")
        fun getSessionTickets(
            @Path("id") sessionId: Long,
            @Header("Authorization") token: String
        ): Call<SessionTicketsList>
    }

}