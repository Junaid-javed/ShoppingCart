package com.example.data.model.request

import com.example.domain.model.request.AddCartRequestModel
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartRequest (
    val productId: Int,
    val quantity: Int
){
    companion object {
        fun fromAddCartRequestDataModel(addCartRequestModel: AddCartRequestModel) =
            AddToCartRequest(
                productId = addCartRequestModel.productId,
                quantity = addCartRequestModel.quantity
            )
    }
}