package com.example.data.model

import com.example.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class DataProductModel(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val title: String
) {
    fun toProduct() = Product(
        id = id,
        title = title,
        categoryId = categoryId,
        description = description,
        price = price,
        image = image
    )

}