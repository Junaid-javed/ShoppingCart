package com.example.domain.usecase

import com.example.domain.repository.CartRepository

class CartSummaryUseCase(private val cartRepository: CartRepository) {
    suspend fun execute(userId: Long) = cartRepository.getCartSummary(userId)

}