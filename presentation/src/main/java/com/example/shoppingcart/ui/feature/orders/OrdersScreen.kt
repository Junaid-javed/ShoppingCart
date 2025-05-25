package com.example.shoppingcart.ui.feature.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.OrdersData
import com.example.shoppingcart.navigation.OrdersScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersScreen(viewModel: OrdersViewModel = koinViewModel()) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Orders", modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium
            )
        }

        val uiState = viewModel.orders.collectAsState()

        val tabs = listOf("All", "Pending", "Delivered", "Cancelled")
        val selectedTab = remember {
            mutableIntStateOf(0)
        }
        TabRow(selectedTab.intValue) {
            tabs.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier.clickable{
                        selectedTab.intValue = index
                    }
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        when (uiState.value) {
            is OrderEvent.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(text = "Loading")
                }
            }

            is OrderEvent.Success -> {
                val orders = (uiState.value as OrderEvent.Success).data
                when (selectedTab.intValue) {
                    0 -> {
                        OrderList(orders = orders)
                    }

                    1 -> {
                        OrderList(orders = viewModel.filterOrders(orders, "Pending"))
                    }

                    2 -> {
                        OrderList(orders = viewModel.filterOrders(orders, "Delivered"))
                    }

                    3 -> {
                        OrderList(orders = viewModel.filterOrders(orders, "Cancelled"))
                    }
                }
            }

            is OrderEvent.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = (uiState.value as OrderEvent.Error).message)
                }
            }
        }

    }

}

@Composable
fun OrderList(orders: List<OrdersData>) {

    if (orders.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No orders found")
        }
    } else {

        LazyColumn {
            items(
                orders,
                key = { orders -> orders.id }) {
                OrderItem(it)
            }

        }
    }
}

@Composable
fun OrderItem(ordersData: OrdersData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.4f))
            .padding(8.dp)
    ) {
        Text(text = "Order Id: ${ordersData.id}")
        Text(text = "Order Date: ${ordersData.orderDate}")
        Text(text = "Total Amount: ${ordersData.totalAmount}")
        Text(text = "Status: ${ordersData.status}")

    }
}