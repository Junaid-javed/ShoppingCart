package com.example.shoppingcart.ui.feature.user_address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shoppingcart.model.UserAddress

const val USER_ADDRESS_SCREEN = "user_address_screen"

@Composable
fun UserAddressScreen(navController: NavController, userAddress: UserAddress?) {

    val addressLine = remember { mutableStateOf(userAddress?.addressLine ?: "") }
    val city = remember { mutableStateOf(userAddress?.city ?: "") }
    val state = remember { mutableStateOf(userAddress?.state ?: "") }
    val postalCode = remember { mutableStateOf(userAddress?.postalCode ?: "") }
    val country = remember { mutableStateOf(userAddress?.country ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = addressLine.value,
            onValueChange = { addressLine.value = it },
            label = { Text("Address Line") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = city.value,
            onValueChange = { city.value = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()

        )

        OutlinedTextField(
            value = state.value,
            onValueChange = { state.value = it },
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = postalCode.value,
            onValueChange = { postalCode.value = it },
            label = { Text("Postal Code") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = country.value,
            onValueChange = { country.value = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val updatedUserAddress = UserAddress(
                    addressLine = addressLine.value,
                    city = city.value,
                    state = state.value,
                    postalCode = postalCode.value,
                    country = country.value
                )
                val previousBackStackEntry = navController.previousBackStackEntry
                previousBackStackEntry?.savedStateHandle?.set(
                    USER_ADDRESS_SCREEN,
                    updatedUserAddress
                )
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = addressLine.value.isNotBlank() &&
                    city.value.isNotBlank() &&
                    state.value.isNotBlank() &&
                    postalCode.value.isNotBlank() &&
                    country.value.isNotBlank()
        ) {
            Text("Save")
        }
    }

}