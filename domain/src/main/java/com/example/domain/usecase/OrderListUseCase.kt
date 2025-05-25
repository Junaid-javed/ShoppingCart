package com.example.domain.usecase

import com.example.domain.repository.OrderRepository

class OrderListUseCase(private val orderRepository: OrderRepository) {
    suspend fun execute(userId: Long) = orderRepository.getOrdersList(userId)

}