package com.example.shoppingcart.ui.feature.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.model.Product
import com.example.shoppingcart.R
import com.example.shoppingcart.ShopperSession
import com.example.shoppingcart.model.UiProductModel
import com.example.shoppingcart.navigation.CartScreen
import com.example.shoppingcart.navigation.ProductDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, shopperSession: ShopperSession, viewModel: HomeViewModel = koinViewModel()) {

    val uiState = viewModel.uiState.collectAsState().value
    val loading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }
    val featured = remember { mutableStateOf<List<Product>>(emptyList()) }
    val popularProducts = remember { mutableStateOf<List<Product>>(emptyList()) }
    val categories = remember { mutableStateOf<List<String>>(emptyList()) }



    when (uiState) {
        is HomeScreenUIEvents.Loading -> {
            loading.value = true
            error.value = null
        }

        is HomeScreenUIEvents.Success -> {
            val data = (uiState as HomeScreenUIEvents.Success)
            loading.value = false
            error.value = null
            featured.value = data.featured
            popularProducts.value = data.popularProducts
            categories.value = data.categories
        }

        is HomeScreenUIEvents.Error -> {
            // Show error state
            val errorMessage = uiState.message
            loading.value = false
            error.value = errorMessage
        }

    }
    HomeContent(
        featured.value,
        popularProducts.value,
        categories.value,
        isLoading = loading.value,
        error.value,
        onClick = {
            navController.navigate(ProductDetails(UiProductModel.fromProduct(it)))
        },
        onCartClick = {
            navController.navigate(CartScreen)
        },
        shopperSession
    )
}


@Composable
fun ProfileHeader(onCartClick: () -> Unit = {}, shopperSession: ShopperSession) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.baseline_person_24),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = shopperSession.getUser()?.name ?: "Guest",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_notifications_24),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = Color.LightGray.copy(alpha = 0.3f))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.ic_cart),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = Color.LightGray.copy(alpha = 0.3f))
                    .padding(8.dp)
                    .clickable{
                        onCartClick()
                    }
            )
        }

    }
}

@Composable
fun SearchBar(value: String, onTextChange: (String) -> Unit) {

    TextField(
        value = value,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp),
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.baseline_search_24),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)
        ),
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.bodySmall
            )
        }
    )

}


@Composable
fun HomeContent(
    featured: List<Product>,
    popularProducts: List<Product>,
    categories: List<String>,
    isLoading: Boolean = false,
    errorMsg: String? = null,
    onClick: (Product) -> Unit = {},
    onCartClick: () -> Unit,
    session: ShopperSession
) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileHeader(onCartClick, session)
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(value = "", onTextChange = {})
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp)
                    )
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }

            errorMsg?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            if (categories.isNotEmpty()) {
                LazyRow {
                    items(categories, key = { it }) { category ->
                        val isVisible = remember { mutableStateOf(false) }
                        LaunchedEffect(true) {
                            isVisible.value = true
                        }
                        AnimatedVisibility(
                            visible = isVisible.value,
                            enter = fadeIn() + expandVertically()
                        ) {
                            Text(
                                text = category.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color = MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (featured.isNotEmpty()) {
                HomeProductRow(products = featured, title = "Featured", onClick)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (popularProducts.isNotEmpty()) {
                HomeProductRow(products = popularProducts, title = "Popular Products", onClick)
            }
        }
    }
}

@Composable
fun HomeProductRow(products: List<Product>, title: String, onClick: (Product) -> Unit = {}) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyRow {
            items(products, key = { it.id }) { product ->
                val isVisible = remember { mutableStateOf(false) }
                LaunchedEffect(true) {
                    isVisible.value = true
                }
                AnimatedVisibility(
                    visible = isVisible.value,
                    enter = fadeIn() + expandVertically()
                ) {
                    ProductItem(product, onClick)
                }
            }
        }
    }

}

@Composable
fun ProductItem(product: Product, onClick: (Product) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(width = 150.dp, height = 144.dp)
            .clickable {
                onClick(product)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .background(color = Color.White),

                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${product.title}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}