package com.example.lksmartandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("alamat")
    val alamat: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("telepon")
    val telepon: String,
    @SerializedName("image")
    val image: String
) : Parcelable