package com.example.lksmartandroid.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ProductModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("kode")
    val kode: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("expiredDate")
    val expiredDate: String,
    @SerializedName("jumlah")
    val jumlah: Int,
    @SerializedName("satuan")
    val satuan: String,
    @SerializedName("hargaSatuan")
    val hargaSatuan: Int,
    @SerializedName("image")
    val image: String
) : Parcelable