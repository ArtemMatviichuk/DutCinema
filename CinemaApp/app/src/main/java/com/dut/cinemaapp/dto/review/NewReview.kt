package com.dut.cinemaapp.dto.review

data class NewReview(
    val text: String,
    val movieId: Long,
    val authorId: Long
)