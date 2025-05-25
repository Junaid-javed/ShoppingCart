package com.example.shoppingcart.model

import android.os.Parcelable
import com.example.data.model.request.AddressDataModel
import com.example.domain.model.AddressDomainModel
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class UserAddress(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
) : Parcelable{
    override fun toString(): String {
        return "$addressLine, $city, $state, $postalCode, $country"
    }

    fun toAddressDomainModel(): AddressDomainModel {
        return AddressDomainModel(
            addressLine = addressLine,
            city = city,
            state = state,
            postalCode = postalCode,
            country = country
        )
    }
}