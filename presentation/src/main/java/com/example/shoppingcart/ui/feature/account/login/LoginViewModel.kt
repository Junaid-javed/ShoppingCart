package com.example.shoppingcart.ui.feature.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.LoginUseCase
import com.example.shoppingcart.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(val loginUseCase: LoginUseCase,
    private val shopperSession: ShopperSession) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = loginUseCase.execute(username, password)
            when (result) {
                is ResultWrapper.Success -> {
                    shopperSession.storeUser(result.value)
                    _loginState.value = LoginState.Success()
                }

                is ResultWrapper.Failure -> {
                    _loginState.value =
                        LoginState.Error(result.exception.message ?: "Something went wrong")
                }
            }
        }
    }

}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    class Success : LoginState()
    data class Error(val message: String) : LoginState()
}