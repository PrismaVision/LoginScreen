package com.example.screenlogin.repository

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
val nickName: String,
val email: String,
val password: String
)
data class RegisterResponse(
    val message: String
)

