package com.michaelgrigoryan.covidtracker

import com.michaelgrigoryan.covidtracker.api.Country
import com.michaelgrigoryan.covidtracker.api.Response
import com.michaelgrigoryan.covidtracker.api.Stats
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface COVID {
    @Headers(
        "x-rapidapi-key: 9058ab254bmsh06db2fa7427a4e6p1013e4jsn2217831e6a2e",
        "x-rapidapi-host: covid-193.p.rapidapi.com",
        "useQueryString: true"
    )
    @GET("/statistics")
    fun getStats(): Call<Stats>

    @Headers(
        "x-rapidapi-key: 9058ab254bmsh06db2fa7427a4e6p1013e4jsn2217831e6a2e",
        "x-rapidapi-host: covid-193.p.rapidapi.com",
        "useQueryString: true"
    )
    @GET("/countries")
    fun getCountries(): Call<Country>
}