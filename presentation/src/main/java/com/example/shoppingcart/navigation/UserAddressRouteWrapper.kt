package com.example.shoppingcart.navigation

import android.os.Parcelable
import com.example.shoppingcart.model.UserAddress
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserAddressRouteWrapper(
    val userAddress: UserAddress?
) : Parcelable