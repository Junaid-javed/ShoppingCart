package com.example.shoppingcart.ui.feature.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CartSummary
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.CartSummaryUseCase
import com.example.domain.usecase.PlaceOrderUseCase
import com.example.shoppingcart.ShopperSession
import com.example.shoppingcart.model.UserAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartSummaryViewModel(
    private val cartSummaryUseCase: CartSummaryUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val shopperSession: ShopperSession
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartSummaryEvent>(CartSummaryEvent.Loading)
    val uiState = _uiState.asStateFlow()
    val userDomainModel = shopperSession.getUser()

    init {
        getCartSummary(userDomainModel!!.id!!.toLong())
    }

    private fun getCartSummary(userId: Long) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            try {
                val response = cartSummaryUseCase.execute(userId)
                when (response) {
                    is ResultWrapper.Success -> {
                        _uiState.value = CartSummaryEvent.Success(response.value)
                    }

                    is ResultWrapper.Failure -> {
                        _uiState.value =
                            CartSummaryEvent.Error(response.exception.message ?: "Unknown error")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CartSummaryEvent.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun placeOrder(address: UserAddress) {
        viewModelScope.launch {
            _uiState.value = CartSummaryEvent.Loading
            try {
                val response = placeOrderUseCase.execute(address.toAddressDomainModel(),userDomainModel!!.id!!.toLong())
                when (response) {
                    is ResultWrapper.Success -> {
                        _uiState.value = CartSummaryEvent.PlaceOrder(response.value)
                    }

                    is ResultWrapper.Failure -> {
                        _uiState.value =
                            CartSummaryEvent.Error(response.exception.message ?: "Unknown error")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CartSummaryEvent.Error(e.message ?: "Unknown error")
            }
        }
    }

}

sealed class CartSummaryEvent {
    data object Loading : CartSummaryEvent()
    data class Success(val data: CartSummary) : CartSummaryEvent()
    data class Error(val message: String) : CartSummaryEvent()
    data class PlaceOrder(val orderId: Long) : CartSummaryEvent()

}