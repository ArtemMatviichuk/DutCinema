package com.dut.cinemaapp.domain

data class Session(
    val id: Long,
    val movieId: Long,
    val hallId: Long,
    val movieTitle: String,
    val moviePoster: String,
    val hallName: String,
    val date: String,
    val isCanceled: Boolean
)
