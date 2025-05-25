package com.example.shoppingcart.ui.feature.account.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shoppingcart.R
import com.example.shoppingcart.navigation.HomeScreen
import com.example.shoppingcart.navigation.RegisterScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = koinViewModel()) {

    val state = viewModel.loginState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.value) {
            is LoginState.Success -> {
                LaunchedEffect(state.value) {
                    navController.navigate(HomeScreen) {
                        popUpTo(HomeScreen) {
                            inclusive = true
                        }

                    }
                }
            }

            is LoginState.Error -> {
                Text(text = (state.value as LoginState.Error).message)
            }

            is LoginState.Loading -> {
                CircularProgressIndicator()
                Text(text = stringResource(R.string.loading), style = MaterialTheme.typography.bodyMedium)
            }

            else -> {
                LoginContent(
                    onSignClick = { email, password ->
                        viewModel.login(email, password)
                    },
                    onRegisterClick = {
                        navController.navigate(RegisterScreen)
                    })
            }
        }
    }
}

@Composable
fun LoginContent(onSignClick: (String, String) -> Unit, onRegisterClick: () -> Unit = {}) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.login), style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.email)) })
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                onSignClick(email.value, password.value)
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            enabled = email.value.isNotEmpty() && password.value.isNotEmpty()
        ) {
            Text(text = stringResource(R.string.login))
        }
        Text(
            text = stringResource(R.string.don_t_have_an_account_sign_up), modifier = Modifier
                .padding(8.dp)
                .clickable {
                    onRegisterClick()
                }, style = MaterialTheme.typography.bodyMedium
        )
    }

}

@Composable
@Preview(showBackground = true)
fun LoginPreview() {
    LoginContent(
        onSignClick = { email, password ->
        },
        onRegisterClick = {}
    )
}