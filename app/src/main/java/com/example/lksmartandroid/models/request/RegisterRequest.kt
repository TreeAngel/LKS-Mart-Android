package com.example.lksmartandroid.models.request


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class RegisterRequest(
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String
) : Parcelable