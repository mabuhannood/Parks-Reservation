package com.example.project_g06.networking

import com.example.project_g06.models.parkData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // url parameter variable
    @GET("api/v1/parks?api_key=MPAQUQIPvcRhklo3azUCYx6ieuVFnHQ56hbNDXKX")
    suspend fun getParks(@Query("stateCode") stateCode:String): Response<parkData>

}