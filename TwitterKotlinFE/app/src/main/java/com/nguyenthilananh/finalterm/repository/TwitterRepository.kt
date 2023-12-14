package com.nguyenthilananh.finalterm.repository

import ApiService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.nguyenthilananh.finalterm.ApiDatasource.instance
import com.nguyenthilananh.finalterm.MainActivity
import com.nguyenthilananh.finalterm.Model.*
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Console





class TwitterRepository {
    private val apiService: ApiService
    init {
        apiService = instance!!.create(ApiService::class.java)
    }


    fun login(email: String, password: String) : MutableLiveData<LoginResponse> {
        val call: Call<LoginResponse> = apiService.login(email, password)
        val loginResponse = MutableLiveData<LoginResponse>()
        call.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Handle the response
                    //consolo.log(responseBody)
                    Log.d("TAG", responseBody.toString())
                    loginResponse.setValue(response.body())

                    val success = loginResponse.value!!.success
                    val message = loginResponse.value!!.message
                    val data = loginResponse.value!!.data








                    if (success){





                    }
                    else{











                    }

                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                // Handle the failure
            }
        })

return loginResponse
    }
    fun register(email: String, password: String, firstName: String, picturePath: String) {
        val call: Call<RegisterResponse> = apiService.register(firstName,email, password,  picturePath)
        call.enqueue(object : Callback<RegisterResponse?> {
            override fun onResponse(
                call: Call<RegisterResponse?>,
                response: Response<RegisterResponse?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Handle the response
                    //consolo.log(responseBody)
                    Log.d("TAG", responseBody.toString())
                    val registerResponse =responseBody
                    Log.d("TAG", registerResponse!!.success.toString())





                    val success: Boolean = registerResponse.success
                    val statusCode: Int = registerResponse.statusCode
                    val message: String = registerResponse.message


                    if (success){

//                        SaveSettings(MainActivity().applicationContext).saveSettings(data.userId.toString())
//                        val intent = Intent(MainActivity().applicationContext, MainActivity::class.java)
//                        intent.putExtra("userId", data.userId.toString())
//                        intent.putExtra("firstName", data.firstName)
//                        intent.putExtra("email", data.email)
//                        intent.putExtra("picturePath", data.picturePath)
//                        startActivity(MainActivity().applicationContext,intent,null)
//                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()



                    }
                    else{

                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()










                    }

                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                // Handle the failure
            }
        })

    }
    fun getUserFollowing(userId: Int,followingUserId: Int,  op: Int) {
        val call: Call<UserFollowingResponse> = apiService.getUserFollowing(userId,followingUserId, op)
        call.enqueue(object : Callback<UserFollowingResponse?> {
            override fun onResponse(
                call: Call<UserFollowingResponse?>,
                response: Response<UserFollowingResponse?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Handle the response
                    //consolo.log(responseBody)
                    Log.d("TAG", responseBody.toString())
                    val userFollowingResponse =responseBody
                    Log.d("TAG", userFollowingResponse!!.success.toString())





                    val success: Boolean = userFollowingResponse.success
                    val statusCode: Int = userFollowingResponse.statusCode
                    val message: String = userFollowingResponse.message


                    if (success){

//                        SaveSettings(MainActivity().applicationContext).saveSettings(data.userId.toString())
//                        val intent = Intent(MainActivity().applicationContext, MainActivity::class.java)
//                        intent.putExtra("userId", data.userId.toString())
//                        intent.putExtra("firstName", data.firstName)
//                        intent.putExtra("email", data.email)
//                        intent.putExtra("picturePath", data.picturePath)
//                        startActivity(MainActivity().applicationContext,intent,null)
//                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()



                    }
                    else{

                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()










                    }

                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<UserFollowingResponse?>, t: Throwable) {
                // Handle the failure
            }
        })

    }
    fun isFollowing(userId: Int,followingUserId: Int) {
        val call: Call<IsFollowingResponse> = apiService.isFollowing(userId,followingUserId)
        call.enqueue(object : Callback<IsFollowingResponse?> {
            override fun onResponse(
                call: Call<IsFollowingResponse?>,
                response: Response<IsFollowingResponse?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Handle the response
                    //consolo.log(responseBody)
                    Log.d("TAG", responseBody.toString())
                    val isFollowingResponse =responseBody
                    Log.d("TAG", isFollowingResponse!!.success.toString())





                    val success: Boolean = isFollowingResponse.success
                    val statusCode: Int = isFollowingResponse.statusCode
                    val message: String = isFollowingResponse.message


                    if (success){

//                        SaveSettings(MainActivity().applicationContext).saveSettings(data.userId.toString())
//                        val intent = Intent(MainActivity().applicationContext, MainActivity::class.java)
//                        intent.putExtra("userId", data.userId.toString())
//                        intent.putExtra("firstName", data.firstName)
//                        intent.putExtra("email", data.email)
//                        intent.putExtra("picturePath", data.picturePath)
//                        startActivity(MainActivity().applicationContext,intent,null)
//                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()



                    }
                    else{

                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()










                    }

                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<IsFollowingResponse?>, t: Throwable) {
                // Handle the failure
            }
        })

    }
    fun addTweet(userId: Int,tweetText: String,tweetPicture: String) {
        val call: Call<TweetAddResponse> = apiService.addTweet(userId,tweetText,tweetPicture)
        call.enqueue(object : Callback<TweetAddResponse?> {
            override fun onResponse(
                call: Call<TweetAddResponse?>,
                response: Response<TweetAddResponse?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Handle the response
                    //consolo.log(responseBody)
                    Log.d("TAG", responseBody.toString())
                    val isFollowingResponse =responseBody
                    Log.d("TAG", isFollowingResponse!!.success.toString())





                    val success: Boolean = isFollowingResponse.success
                    val statusCode: Int = isFollowingResponse.statusCode
                    val message: String = isFollowingResponse.message


                    if (success){

//                        SaveSettings(MainActivity().applicationContext).saveSettings(data.userId.toString())
//                        val intent = Intent(MainActivity().applicationContext, MainActivity::class.java)
//                        intent.putExtra("userId", data.userId.toString())
//                        intent.putExtra("firstName", data.firstName)
//                        intent.putExtra("email", data.email)
//                        intent.putExtra("picturePath", data.picturePath)
//                        startActivity(MainActivity().applicationContext,intent,null)
//                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()



                    }
                    else{

                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()










                    }

                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<TweetAddResponse?>, t: Throwable) {
                // Handle the failure
            }
        })

    }
    fun getTweetList(userId: Int,op: Int) {
        val call: Call<TweetListResponse> = apiService.getTweetList(userId,op)
        call.enqueue(object : Callback<TweetListResponse?> {
            override fun onResponse(
                call: Call<TweetListResponse?>,
                response: Response<TweetListResponse?>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Handle the response
                    //consolo.log(responseBody)
                    Log.d("TAG", responseBody.toString())
                    val listTweetResponse =responseBody





                    val success: Boolean = listTweetResponse!!.success
                    val statusCode: Int = listTweetResponse.statusCode
                    val message: String = listTweetResponse.message
                    val data = listTweetResponse.data


                    if (success){

//                        SaveSettings(MainActivity().applicationContext).saveSettings(data.userId.toString())
//                        val intent = Intent(MainActivity().applicationContext, MainActivity::class.java)
//                        intent.putExtra("userId", data.userId.toString())
//                        intent.putExtra("firstName", data.firstName)
//                        intent.putExtra("email", data.email)
//                        intent.putExtra("picturePath", data.picturePath)
//                        startActivity(MainActivity().applicationContext,intent,null)
//                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()



                    }
                    else{

                        Toast.makeText(MainActivity().applicationContext,message,Toast.LENGTH_LONG).show()










                    }

                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<TweetListResponse?>, t: Throwable) {
                // Handle the failure
            }
        })

    }

    }
