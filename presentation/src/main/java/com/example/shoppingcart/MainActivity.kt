package com.example.shoppingcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.domain.model.Product
import com.example.shoppingcart.model.UiProductModel
import com.example.shoppingcart.navigation.CartScreen
import com.example.shoppingcart.navigation.CartSummaryScreen
import com.example.shoppingcart.navigation.HomeScreen
import com.example.shoppingcart.navigation.LoginScreen
import com.example.shoppingcart.navigation.OrdersScreen
import com.example.shoppingcart.navigation.ProductDetails
import com.example.shoppingcart.navigation.ProfileScreen
import com.example.shoppingcart.navigation.RegisterScreen
import com.example.shoppingcart.navigation.UserAddressRoute
import com.example.shoppingcart.navigation.UserAddressRouteWrapper
import com.example.shoppingcart.navigation.productNavType
import com.example.shoppingcart.navigation.userAddressNavType
import com.example.shoppingcart.ui.feature.account.login.LoginScreen
import com.example.shoppingcart.ui.feature.account.register.RegisterScreen
import com.example.shoppingcart.ui.feature.cart.CartScreen
import com.example.shoppingcart.ui.feature.home.HomeScreen
import com.example.shoppingcart.ui.feature.orders.OrdersScreen
import com.example.shoppingcart.ui.feature.product_details.ProductDetailsScreen
import com.example.shoppingcart.ui.feature.profile.ProfileScreen
import com.example.shoppingcart.ui.feature.summary.CartSummaryScreen
import com.example.shoppingcart.ui.feature.user_address.UserAddressScreen
import com.example.shoppingcart.ui.theme.ShoppingCartTheme
import org.koin.android.ext.android.inject
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val shopperSession: ShopperSession by inject()
            ShoppingCartTheme {
                val shouldShowBottomNav = remember {
                    mutableStateOf(true)
                }
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(visible = shouldShowBottomNav.value, enter = fadeIn()) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        val startDestination = if (shopperSession.getUser() != null) {
                            HomeScreen
                        } else {
                            LoginScreen
                        }
                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable<HomeScreen> {
                                shouldShowBottomNav.value = true
                                HomeScreen(navController, shopperSession)
                            }
                            composable<CartScreen> {
                                shouldShowBottomNav.value = true
                                CartScreen(navController)
                            }

                            composable<OrdersScreen> {
                                shouldShowBottomNav.value = true
                                OrdersScreen()
                            }
                            composable<ProfileScreen> {
                                shouldShowBottomNav.value = true
                                ProfileScreen(navController, shopperSession)
                            }



                            composable<CartSummaryScreen> {
                                shouldShowBottomNav.value = false
                                CartSummaryScreen(navController)
                            }

                            composable<LoginScreen> {
                                shouldShowBottomNav.value = false
                                LoginScreen(navController)
                            }

                            composable<RegisterScreen> {
                                shouldShowBottomNav.value = false
                                RegisterScreen(navController)
                            }

                            composable<ProductDetails>(
                                typeMap = mapOf(typeOf<UiProductModel>() to productNavType)
                            ) {
                                shouldShowBottomNav.value = false
                                val productRoute = it.toRoute<ProductDetails>()
                                ProductDetailsScreen(navController, productRoute.product)
                            }

                            composable<UserAddressRoute>(
                                typeMap = mapOf(typeOf<UserAddressRouteWrapper>() to userAddressNavType)
                            ) {
                                shouldShowBottomNav.value = false
                                val userAddressRoute = it.toRoute<UserAddressRoute>()
                                UserAddressScreen(
                                    navController,
                                    userAddressRoute.userAddressRouteWrapper.userAddress
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val items = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Orders,
            BottomBarScreen.Profile
        )
        items.forEach {
            val isSelected = currentRoute?.substringBefore("?") == it.route::class.qualifiedName
            NavigationBarItem(
                selected = isSelected,
                onClick = { navController.navigate(it.route) },
                label = { Text(text = it.title) },
                icon = {
                    Image(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                        colorFilter = ColorFilter.tint(
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

sealed class BottomBarScreen(val route: Any, val title: String, val icon: Int) {
    object Home : BottomBarScreen(route = HomeScreen, title = "Home", icon = R.drawable.ic_home)
    object Orders :
        BottomBarScreen(route = OrdersScreen, title = "Orders", icon = R.drawable.ic_cart)

    object Profile :
        BottomBarScreen(
            route = ProfileScreen,
            title = "Profile",
            icon = R.drawable.baseline_person_24
        )

}