package com.sameh.randomwords.data.api

import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET("3936e034-d9b2-43c8-bac0-4975f61f919b")
    suspend fun getRandomWords(): Response<List<String>>

}