package com.nguyenthilananh.finalterm.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UserData
)

data class UserData(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("picturePath")
    val picturePath: String
)
