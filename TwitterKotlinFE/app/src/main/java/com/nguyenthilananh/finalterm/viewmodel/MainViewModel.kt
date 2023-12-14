package com.nguyenthilananh.finalterm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenthilananh.finalterm.ApiDatasource
import com.nguyenthilananh.finalterm.Model.LoginResponse
import com.nguyenthilananh.finalterm.repository.TwitterRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse
    val twitterRepository = TwitterRepository()

    init {

    }


    fun login(email: String, password: String, onLoginComplete: Function1<LoginResponse, Unit>) {

        onLoginComplete




        twitterRepository.login(email, password)


    }
    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {

            twitterRepository.register(email, password, firstName, lastName)

        }
    }
    fun getUserFollowing(userId: Int, followingUserId: Int, op: Int) {
        viewModelScope.launch {

            twitterRepository.getUserFollowing(userId, followingUserId, op)

        }
    }
    fun isFollowing(userId: Int, followingUserId: Int) {
        viewModelScope.launch {

            twitterRepository.isFollowing(userId, followingUserId)


        }
    }
    fun addTweet(userId: Int, tweetText: String, tweetPicture: String) {
        viewModelScope.launch {

            twitterRepository.addTweet(userId, tweetText, tweetPicture)

        }
    }
    fun getTweetList(userId: Int, op: Int) {
        viewModelScope.launch {

            twitterRepository.getTweetList(userId, op)

        }
    }
}