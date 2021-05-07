package com.dut.cinemaapp.services

import com.dut.cinemaapp.domain.Place
import com.dut.cinemaapp.domain.PurchaseTicketsList
import com.dut.cinemaapp.domain.Ticket
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class TicketsService {
    fun bookTickets(sessionId: Long, places: MutableList<Place>): Call<List<Ticket>> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .book(
                PurchaseTicketsList(
                    sessionId,
                    AccountService.Singleton.getInstance()?.id!!,
                    places
                ),
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )
    }

    fun getUserTickets(): Call<List<Ticket>> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .getTickets(
                AccountService.Singleton.getInstance()?.id!!,
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )
    }

    fun removeBooking(id: Long): Call<Void> {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCaller::class.java)
            .removeBooking(
                id,
                "Bearer_" + AccountService.Singleton.getInstance()?.token!!
            )
    }

    private interface ApiCaller {
        @POST("tickets/purchaselist")
        fun book(
            @Body purchaseList: PurchaseTicketsList,
            @Header("Authorization") token: String
        ): Call<List<Ticket>>

        @GET("tickets/user/{id}")
        fun getTickets(
            @Path("id") userId: Long,
            @Header("Authorization") token: String
        ): Call<List<Ticket>>

        @DELETE("tickets/{id}")
        fun removeBooking(
            @Path("id") id: Long,
            @Header("Authorization") token: String
        ): Call<Void>
    }
}