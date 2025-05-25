package com.example.domain.repository

import com.example.domain.model.CategoryListModel
import com.example.domain.network.ResultWrapper

interface CategoryRepository{
    suspend fun getCategories(): ResultWrapper<CategoryListModel>

}