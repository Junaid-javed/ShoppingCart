package com.example.shoppingcart.ui.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.OrdersData

import com.example.domain.usecase.OrderListUseCase
import com.example.shoppingcart.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val orderListUseCase: OrderListUseCase,
    private val shopperSession: ShopperSession
) : ViewModel() {

    private val _orders = MutableStateFlow<OrderEvent>(OrderEvent.Loading)
    val orders = _orders.asStateFlow()
    val userDomainModel = shopperSession.getUser()

    init {
        getOrdersList()
    }


    fun filterOrders(list: List<OrdersData>, filter: String): List<OrdersData> {
        val filterList  = list.filter { it.status == filter }
        return filterList
    }

   private fun getOrdersList() {
        viewModelScope.launch {
            val result = orderListUseCase.execute(userDomainModel!!.id!!.toLong())
            when (result) {
                is com.example.domain.network.ResultWrapper.Success -> {
                    val data = result.value
                    _orders.value = OrderEvent.Success(data.`data`)
                }

                is com.example.domain.network.ResultWrapper.Failure -> {
                    _orders.value = OrderEvent.Error(result.exception.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class OrderEvent {
    object Loading : OrderEvent()
    data class Success(val data: List<OrdersData>) : OrderEvent()
    data class Error(val message: String) : OrderEvent()


}