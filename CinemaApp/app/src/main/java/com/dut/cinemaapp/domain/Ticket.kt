package com.dut.cinemaapp.domain

data class Ticket(
    val id: Long,
    val sessionId: Long,
    val movieId: Long,
    val movieTitle: String,
    val date: String,
    val hallName: String,
    val row: Int,
    val place: Int,
    val posterPath: String,
    val isCanceled: Boolean
)
