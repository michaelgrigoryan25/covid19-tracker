package com.michaelgrigoryan.covidtracker.api

import com.michaelgrigoryan.covidtracker.model.CovidData
import com.michaelgrigoryan.covidtracker.model.Stats
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("/history")
    fun getHistory(
            @Query("country") country: String?,
            @Query("day") day: String
    ): Call<CovidData>

    @GET("/statistics")
    fun getStats(): Call<Stats>
}