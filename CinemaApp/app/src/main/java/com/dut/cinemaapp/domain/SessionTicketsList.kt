package com.dut.cinemaapp.domain

data class SessionTicketsList(
    val sessionId: Long,
    val rowsAmount: Int,
    val place: Int,
    val tickets: List<Place>
)
