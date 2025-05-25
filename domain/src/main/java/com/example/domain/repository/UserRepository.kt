package com.example.domain.repository

import com.example.domain.model.UserDomainModel
import com.example.domain.network.ResultWrapper

interface  UserRepository {
    suspend fun login(username: String, password: String): ResultWrapper<UserDomainModel>
    suspend fun register(email: String, password: String, name: String): ResultWrapper<UserDomainModel>

}