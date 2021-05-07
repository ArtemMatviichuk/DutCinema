package com.dut.cinemaapp.domain

data class PurchaseTicketsList(
    val sessionId: Long,
    val userId: Long,
    val places: MutableList<Place>
) {
}