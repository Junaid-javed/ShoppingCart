package com.example.data.model.response

import com.example.domain.model.SummaryData

@kotlinx.serialization.Serializable
data class Summary(
    val discount: Double,
    val items: List<CartItem>,
    val shipping: Double,
    val subtotal: Double,
    val tax: Double,
    val total: Double
) {
    fun toSummaryData() = SummaryData(
        discount = discount,
        item = items.map { it.toCartItemModel() },
        shipping = shipping,
        subTotal = subtotal,
        tax = tax,
        total = total
    )
}

