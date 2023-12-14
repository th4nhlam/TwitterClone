package com.nguyenthilananh.finalterm.Model

import com.google.gson.annotations.SerializedName

data class IsFollowingResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Boolean
)
