package com.example.shoppingcart.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.example.shoppingcart.model.UiProductModel
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.Base64

val productNavType = object : NavType<UiProductModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UiProductModel? {
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          bundle.getParcelable(key, UiProductModel::class.java)
      } else {
          bundle.getParcelable(key) as? UiProductModel
      }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun parseValue(value: String): UiProductModel {
       val item = Json.decodeFromString<UiProductModel>(value)
        return item.copy(
            image = URLDecoder.decode(item.image, "UTF-8"),
            title = String(Base64.getDecoder().decode(item.title.replace("_","/"))),
            description = String(Base64.getDecoder().decode(item.description.replace("_","/"))),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serializeAsValue(value: UiProductModel): String {
        val encoded = value.copy(
            image = URLEncoder.encode(value.image, "UTF-8"),
            description = String(Base64.getEncoder().encode(value.description.toByteArray())).replace("/","_"),
            title = String(Base64.getEncoder().encode(value.title.toByteArray())).replace("/","_")
        )
        return Json.encodeToString(encoded)

    }

    override fun put(bundle: Bundle, key: String, value: UiProductModel) {
        bundle.putParcelable(key, value)
    }

}