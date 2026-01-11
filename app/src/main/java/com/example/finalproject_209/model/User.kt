package com.example.finalproject_209.model

import android.os.Message
import kotlinx.serialization.Serializable

@Serializable
data class DataUser(
    val id: Int,
    val name: String,
    val username: String,
    val password: String? = null,
    val email: String,
)
//Login
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val success: Boolean = false,
    val message: String = "",
    val token: String? = null,
    val user: DataUser? = null
)