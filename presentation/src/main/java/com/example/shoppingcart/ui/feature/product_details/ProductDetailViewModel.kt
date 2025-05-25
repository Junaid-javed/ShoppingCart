package com.example.shoppingcart.ui.feature.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.request.AddCartRequestModel
import com.example.domain.usecase.AddToCartUseCase
import com.example.shoppingcart.ShopperSession
import com.example.shoppingcart.model.UiProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(val useCase: AddToCartUseCase,
    val shopperSession: ShopperSession) : ViewModel() {

    private val _state = MutableStateFlow<ProductDetailEvent>(ProductDetailEvent.Nothing)
    val state = _state.asStateFlow()
    val userDomainModel = shopperSession.getUser()

    fun addProductToCart(productModel: UiProductModel) {
        viewModelScope.launch {
            _state.value = ProductDetailEvent.Loading
            val result = useCase.execute(
                AddCartRequestModel(
                    productId = productModel.id,
                    productName = productModel.title,
                    price = productModel.price,
                    quantity = 1,
                    userId = 1
                ), userDomainModel!!.id!!.toLong()
            )
            when (result) {
                is com.example.domain.network.ResultWrapper.Success -> {
                    _state.value = ProductDetailEvent.Success("Product added to cart")
                }
                is com.example.domain.network.ResultWrapper.Failure -> {
                    _state.value = ProductDetailEvent.Error("something went wrong")
                }
            }

        }
    }


}

sealed class ProductDetailEvent {
    data object Loading : ProductDetailEvent()
    data object Nothing : ProductDetailEvent()
    data class Success(val message: String) : ProductDetailEvent()
    data class Error(val message: String) : ProductDetailEvent()

}