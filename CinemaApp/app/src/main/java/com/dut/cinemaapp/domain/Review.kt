package com.dut.cinemaapp.domain

data class Review(
    val id: Long,
    val text: String,
    val creationDate: String,
    val movieId: Long,
    val movieTitle: String,
    val authorId: Long,
    val firstName: String,
    val lastName: String
)
