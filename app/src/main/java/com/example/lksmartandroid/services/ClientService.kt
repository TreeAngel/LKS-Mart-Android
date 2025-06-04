package com.example.lksmartandroid.services

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.lksmartandroid.R
import com.example.lksmartandroid.models.CheckoutItem
import com.example.lksmartandroid.models.UserModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.Format
import java.text.NumberFormat
import java.util.Locale

object ClientService {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    var token: String = ""
    var session: UserModel? = null
    var cart: ArrayList<CheckoutItem> = arrayListOf()

    val service: RetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl("${BASE_URL}api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }

    fun loadImage(context: Context, imageView: ImageView, imagePath: String) {
        Glide.with(context)
            .load("${BASE_URL}${imagePath}")
            .fitCenter()
            .error(R.drawable.warning)
            .into(imageView)
    }

    fun formatPrice(price: Int, local: Locale = Locale("id", "ID")): String {
        return NumberFormat.getCurrencyInstance(local).format(price)
    }
}