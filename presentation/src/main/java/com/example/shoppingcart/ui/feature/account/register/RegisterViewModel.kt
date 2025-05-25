package com.example.shoppingcart.ui.feature.account.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.RegisterUseCase
import com.example.shoppingcart.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase,
    private val shopperSession: ShopperSession) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState

    fun register(email: String, password: String, name: String) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            val result = registerUseCase.execute(email, password, name)
            when (result) {
                is ResultWrapper.Success -> {
                    shopperSession.storeUser(result.value)
                    _registerState.value = RegisterState.Success()
                }

                is ResultWrapper.Failure -> {
                    _registerState.value =
                        RegisterState.Error(result.exception.message ?: "Something went wrong")
                }
            }
        }
    }

}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    class Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}