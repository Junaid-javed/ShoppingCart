package com.example.data.model.request

import com.example.domain.model.AddressDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class AddressDataModel(
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
){
    companion object{
        fun fromDomainAddress(address: AddressDomainModel): AddressDataModel{
            return AddressDataModel(
                addressLine = address.addressLine,
                city = address.city,
                state = address.state,
                postalCode = address.postalCode,
                country = address.country
            )
        }
    }
}