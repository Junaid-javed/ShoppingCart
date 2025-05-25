package com.example.shoppingcart.ui.feature.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.data.model.response.CartItem
import com.example.domain.model.CartItemModel
import com.example.shoppingcart.R
import com.example.shoppingcart.navigation.CartScreen
import com.example.shoppingcart.navigation.CartSummaryScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = koinViewModel()) {

    val uiState = viewModel.state.collectAsState()
    val cartItem = remember { mutableStateOf(emptyList<CartItemModel>()) }
    val loading = remember {
        mutableStateOf(false)
    }
    val errorMsg = remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is CartEvent.Loading -> {
                loading.value = true
                errorMsg.value = null
            }

            is CartEvent.Success -> {
                loading.value = false
                val data = (uiState.value as CartEvent.Success).message
                if (data.isNotEmpty()) {
                    cartItem.value = data
                } else {
                    errorMsg.value = "No item in cart"
                }
            }

            is CartEvent.Error -> {
                loading.value = false
                errorMsg.value = (uiState.value as CartEvent.Error).message

            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(true) {
            viewModel.getCart()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Cart", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.size(8.dp))
            val shouldShowList = !loading.value && errorMsg.value == null
            AnimatedVisibility(
                visible = shouldShowList,
                enter = fadeIn(),
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn {
                    items(cartItem.value) {
                        CartItem(
                            cartItem = it,
                            onIncrement = { viewModel.incrementQuantity(it) },
                            onDecrement = { viewModel.decrementQuantity(it) },
                            onRemove = { viewModel.removeFromCart(it) })

                    }
                }
            }
            if (shouldShowList) {
                Button(onClick = {
                    navController.navigate(CartSummaryScreen)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Checkout")
                }
            }
            if (loading.value) {
                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                    Text(text = "Loading...")
                }
            }
            if (errorMsg.value != null) {
                Text(
                    text = errorMsg.value ?: "Something went wrong", modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }
        }
    }
}

@Composable
fun CartItem(
    cartItem: CartItemModel,
    onIncrement: (CartItemModel) -> Unit,
    onDecrement: (CartItemModel) -> Unit,
    onRemove: (CartItemModel) -> Unit,

    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.LightGray.copy(alpha = 0.4f))
    ) {
        AsyncImage(
            model = cartItem.imageUrl, contentDescription = null,
            modifier = Modifier.size(126.dp, 96.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = cartItem.productName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "${cartItem.price}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = {
                onRemove.invoke(cartItem)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.trash_full),
                    contentDescription = null
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onIncrement.invoke(cartItem)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.add_plus_square),
                        contentDescription = null
                    )
                }
                Text(text = cartItem.quantity.toString())
                IconButton(onClick = {
                    onDecrement.invoke(cartItem)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.add_minus_square),
                        contentDescription = null
                    )
                }
            }
        }
    }
}