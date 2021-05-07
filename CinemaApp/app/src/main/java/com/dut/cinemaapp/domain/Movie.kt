package com.dut.cinemaapp.domain

data class Movie(
    val id: Long,
    val title: String,
    val description: String,
    val posterPath: String,
    val trailerPath: String,
    val duration: Int,
    val actors: String,
    val genres: Set<Genre>,
    val country: String
)
