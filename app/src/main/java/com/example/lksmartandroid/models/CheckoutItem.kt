package com.example.lksmartandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CheckoutItem(
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("qty")
    var qty: Int,
    val name: String,
    val price: Int,
    var subtotal: Int = qty * price,
) : Parcelable