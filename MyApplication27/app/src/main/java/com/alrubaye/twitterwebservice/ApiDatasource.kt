//package com.alrubaye.twitterwebservice
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object ApiDatasource {
//    private const val BASE_URL = "https://raw.githubusercontent.com/"
//    private var retrofit: Retrofit? = null
//    val instance: Retrofit?
//        get() {
//            if (retrofit == null) {
//                retrofit = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            }
//            return retrofit
//        }
//}