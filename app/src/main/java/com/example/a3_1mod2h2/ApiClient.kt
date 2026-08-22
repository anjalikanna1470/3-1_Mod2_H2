package com.example.a3_1mod2h2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    /*
     * Android Emulator:
     * 10.0.2.2 points to the host computer's localhost.
     */
    private const val BASE_URL = "http://10.0.2.2:5000/"

    val api: ChatApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ChatApi::class.java)
    }
}