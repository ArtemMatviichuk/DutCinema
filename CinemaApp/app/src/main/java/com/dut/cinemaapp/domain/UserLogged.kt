package com.dut.cinemaapp.domain

data class UserLogged(
    val userId: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: MutableList<String>
)