package com.nguyenthilananh.finalterm.Model

import com.google.gson.annotations.SerializedName

data class UserFollowingResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
)
