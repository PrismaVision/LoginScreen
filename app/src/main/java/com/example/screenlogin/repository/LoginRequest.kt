package com.example.screenlogin.repository

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
data class LoginResponse(
    val token: String,
    val prefix: String
)
