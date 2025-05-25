package com.example.domain.usecase

import com.example.domain.repository.CartRepository

class DeleteQuantityUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(cartItemId: Int, userId: Long) = cartRepository.deleteItem(cartItemId, userId)
}