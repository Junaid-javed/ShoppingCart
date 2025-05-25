package com.example.data.repository

import com.example.domain.network.NetworkService
import com.example.domain.repository.UserRepository

class UserRepositoryImpl(private val networkService: NetworkService) : UserRepository {
    override suspend fun login(
        username: String,
        password: String
    ) = networkService.login(username, password)

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ) = networkService.register(email, password, name)
}