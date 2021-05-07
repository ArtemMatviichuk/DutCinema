package com.dut.cinemaapp.domain

data class TicketView(
    val sessionId: Long,
    val movieId: Long,
    val movieTitle: String,
    val date: String,
    val hallName: String,
    val posterPath: String,
    val amount: Int,
    val isCanceled: Boolean,
    val places: MutableList<Pair<Long, Place>>
)
