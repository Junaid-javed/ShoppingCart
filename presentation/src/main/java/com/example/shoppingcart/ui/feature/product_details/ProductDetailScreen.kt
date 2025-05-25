package com.example.shoppingcart.ui.feature.product_details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shoppingcart.R
import com.example.shoppingcart.model.UiProductModel
import com.example.shoppingcart.ui.feature.home.HomeScreenUIEvents.Loading
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailsScreen(
    navController: NavController,
    product: UiProductModel,
    viewModel: ProductDetailViewModel = koinViewModel()
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.weight(1f)) {
            AsyncImage(
                model = product.image,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()

            )

            Image(
                painter = painterResource(R.drawable.ic_back), contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color = Color.LightGray.copy(alpha = 0.3f))
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            )

            Image(
                painter = painterResource(R.drawable.ic_favourite), contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color = Color.LightGray.copy(alpha = 0.3f))
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
            )

        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )

                Text(
                    text = "$${product.price}", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(R.drawable.ic_star), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "(10 Reviews)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Size",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                repeat(4) {
                    SizeItem(
                        size = "${it + 1}",
                        isSelected = it == 0,
                        onClick = { }

                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = { viewModel.addProductToCart(product) },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Buy now")
                }
                Spacer(modifier = Modifier.size(8.dp))
                IconButton(
                    onClick = { viewModel.addProductToCart(product) },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 16.dp),
                    colors = IconButtonDefaults.iconButtonColors()
                        .copy(containerColor = Color.LightGray.copy(alpha = 0.4f))
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_cart),
                        contentDescription = null
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        val uiState = viewModel.state.collectAsState()
        val loading = remember {
            mutableStateOf(false)
        }
        val error = remember {
            mutableStateOf<String?>(null)
        }

        LaunchedEffect(uiState.value) {
            when (uiState.value) {
                is ProductDetailEvent.Loading -> {
                    loading.value = true
                }

                is ProductDetailEvent.Success -> {
                    loading.value = false
                    Toast.makeText(
                        navController.context,
                        "Product added to cart",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                is ProductDetailEvent.Error -> {
                    loading.value = false
                    Toast.makeText(
                        navController.context,
                        (uiState.value as ProductDetailEvent.Error).message, Toast.LENGTH_SHORT
                    ).show()
                }

                ProductDetailEvent.Nothing -> {
                    loading.value = false
                }
            }
        }
        if (loading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.7f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                )
                Text(
                    text = "Adding to cart...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SizeItem(size: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) Color.White else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            .padding(8.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = size,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.align(Alignment.Center)

        )
    }
}