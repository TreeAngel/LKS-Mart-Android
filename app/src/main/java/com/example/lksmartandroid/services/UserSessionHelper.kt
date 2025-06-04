package com.example.lksmartandroid.services

import android.content.Context

object UserSessionHelper {
    private const val TOKEN_KEY = "ANDROID_MART_TOKEN"

    fun keepSession(context: Context) {
        try {
            val token = context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE)
            token.edit().apply {
                putString(TOKEN_KEY, ClientService.token)
                apply()
            }
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun checkSession(context: Context): Boolean {
        try {
            val token = context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE).getString(
                TOKEN_KEY, ""
            )
            if (!token.isNullOrEmpty()) {
                ClientService.token = token
                return true
            }
            return false
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun deleteSession(context: Context) {
        try {
            val token = context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE)
            token.edit().apply {
                remove(TOKEN_KEY)
                apply()
            }
            ClientService.token = ""
        } catch (ex: Exception) {
            throw ex
        }
    }
}