package com.example.lksmartandroid.services

import com.example.lksmartandroid.models.CheckoutItem
import com.example.lksmartandroid.models.UserModel
import com.example.lksmartandroid.models.request.LoginRequest
import com.example.lksmartandroid.models.request.RegisterRequest
import com.example.lksmartandroid.models.response.GetProductsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @POST("auth")
    suspend fun login(@Body request: LoginRequest): Response<String>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<String>

    @GET("me")
    suspend fun profile(@Header("Authorization") token: String = ClientService.token): Response<UserModel>

    @Multipart
    @POST("upload-profile")
    suspend fun uploadProfile(
        @Header("Authorization") token: String = ClientService.token,
        @Part("image") image: MultipartBody.Part
    ): Response<UserModel>

    @GET("products")
    suspend fun products(@Query("search") search: String?): Response<GetProductsResponse>

    @POST("checkout")
    suspend fun checkout(@Header("Authorization") token: String = ClientService.token, @Body request: ArrayList<CheckoutItem>): Response<String>
}