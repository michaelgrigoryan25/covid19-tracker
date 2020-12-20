package com.michaelgrigoryan.covidtracker.api

data class Country(
    val errors: List<Any>,
    val `get`: String,
    val parameters: List<Any>,
    val response: List<String>,
    val results: Int
)