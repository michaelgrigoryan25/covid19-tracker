package com.michaelgrigoryan.covidtracker.model

data class CovidData(
        val get: String,
        val parameters: Parameters,
        val errors: List<Any>,
        val results: Int,
        val response: List<Response>
)
