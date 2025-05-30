package com.example.domain.usecase

import com.example.domain.model.CartItemModel
import com.example.domain.repository.CartRepository

class UpdateQuantityUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(model: CartItemModel, userId: Long) =
        cartRepository.updateQuantity(model, userId)
}