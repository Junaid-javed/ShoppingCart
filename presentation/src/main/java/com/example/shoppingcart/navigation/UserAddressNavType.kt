package com.example.shoppingcart.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.util.Base64

val userAddressNavType = object : NavType<UserAddressRouteWrapper>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UserAddressRouteWrapper? {
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          bundle.getParcelable(key, UserAddressRouteWrapper::class.java)
      } else {
          bundle.getParcelable(key) as? UserAddressRouteWrapper
      }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun parseValue(value: String): UserAddressRouteWrapper {
       val item = Json.decodeFromString<UserAddressRouteWrapper>(value)
        return item
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serializeAsValue(value: UserAddressRouteWrapper): String {
        return Json.encodeToString(value)

    }

    override fun put(bundle: Bundle, key: String, value: UserAddressRouteWrapper) {
        bundle.putParcelable(key, value)
    }

}