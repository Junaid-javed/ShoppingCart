package com.example.shoppingcart.ui.feature.summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.domain.model.CartItemModel
import com.example.domain.model.CartSummary
import com.example.shoppingcart.BottomBarScreen
import com.example.shoppingcart.BottomNavigationBar
import com.example.shoppingcart.R
import com.example.shoppingcart.model.UserAddress
import com.example.shoppingcart.navigation.HomeScreen
import com.example.shoppingcart.navigation.UserAddressRoute
import com.example.shoppingcart.navigation.UserAddressRouteWrapper
import com.example.shoppingcart.ui.feature.user_address.USER_ADDRESS_SCREEN
import com.example.shoppingcart.utils.CurrencyUtils
import org.koin.androidx.compose.koinViewModel


@Composable
fun CartSummaryScreen(
    navController: NavController,
    viewModel: CartSummaryViewModel = koinViewModel()
) {
    val address = remember { mutableStateOf<UserAddress?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = "Card Summary",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        val uiState = viewModel.uiState.collectAsState()

        LaunchedEffect(navController) {
            val savedState = navController.currentBackStackEntry?.savedStateHandle
            savedState?.getStateFlow(USER_ADDRESS_SCREEN, address.value)?.collect {
                address.value = it
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val state = uiState.value) {
                is CartSummaryEvent.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading...", style = MaterialTheme.typography.titleMedium)
                    }
                }

                is CartSummaryEvent.Success -> {
                    // Show cart summary
                    Column {
                        AddressBar(address.value?.toString() ?: "", onClick = {
                            navController.navigate(UserAddressRoute(UserAddressRouteWrapper(address.value)))
                        })
                        Spacer(modifier = Modifier.size(8.dp))
                        SummaryScreenContent(cartSummary = state.data)

                    }
                }

                is CartSummaryEvent.Error -> {
                    // Show error message
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CartSummaryEvent.PlaceOrder -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painter = painterResource(R.drawable.bags), contentDescription = null)
                        Text(
                            text = "Order Placed: ${state.orderId}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(onClick = {
                            navController.popBackStack(
                                HomeScreen,
                                inclusive = false
                            )
                        }) {
                            Text(text = "Back to Home", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }
        if (uiState.value !is CartSummaryEvent.PlaceOrder) {
            Button(
                onClick = { viewModel.placeOrder(address.value!!) },
                modifier = Modifier.fillMaxWidth(),
                enabled = address.value != null
            ) {
                Text(text = "Checkout", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun SummaryScreenContent(cartSummary: CartSummary) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(0.4f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        item {
            Text(
                text = "Order Summary", style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(cartSummary.data.item) { item ->
            ProductRaw(cartItemModel = item)
        }
        item {
            Column {
                AmountRaw("Sub Total", cartSummary.data.total)
                AmountRaw("Tax", cartSummary.data.tax)
                AmountRaw("Shipping", cartSummary.data.shipping)
                AmountRaw("Discount", cartSummary.data.discount)
                AmountRaw("Total", cartSummary.data.total)

            }
        }
    }
}

@Composable
fun AmountRaw(title: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp
        )
        Text(
            text = CurrencyUtils.formatPrice(amount), style = MaterialTheme.typography.titleSmall,
            fontSize = 14.sp
        )
    }

}

@Composable
fun ProductRaw(cartItemModel: CartItemModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = cartItemModel.productName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall, fontSize = 14.sp

        )
        Text(
            text = "${CurrencyUtils.formatPrice(cartItemModel.price)} x ${cartItemModel.quantity}",
            style = MaterialTheme.typography.titleSmall,
            fontSize = 14.sp
        )
    }

}

@Composable
fun AddressBar(address: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(8.dp)
            .clickable { onClick.invoke() }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_location), contentDescription = "address",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color = Color.LightGray.copy(0.4f)),
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.size(8.dp))

        Column {
            Text(
                text = "Shipping Address",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
