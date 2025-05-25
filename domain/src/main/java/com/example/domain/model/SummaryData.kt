package com.example.domain.model

data class SummaryData(
    val discount: Double,
    val item: List<CartItemModel>,
    val shipping: Double,
    val subTotal: Double,
    val tax: Double,
    val total: Double
)
