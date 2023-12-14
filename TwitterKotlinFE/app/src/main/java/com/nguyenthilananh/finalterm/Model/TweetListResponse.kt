package com.nguyenthilananh.finalterm.Model

import com.google.gson.annotations.SerializedName

data class TweetListResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("records")
    val records: List<Record>,
    @SerializedName("totalRecords")
    val totalRecords: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class Record(
    @SerializedName("tweetId")
    val tweetId: Int,
    @SerializedName("tweetText")
    val tweetText: String,
    @SerializedName("tweetPicture")
    val tweetPicture: String,
    @SerializedName("tweetDate")
    val tweetDate: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("picturePath")
    val picturePath: String
)

