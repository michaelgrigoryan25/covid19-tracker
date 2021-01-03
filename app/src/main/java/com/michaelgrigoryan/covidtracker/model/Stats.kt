package com.michaelgrigoryan.covidtracker.model

data class Stats(
    val errors: List<Any>,
    val `get`: String,
    val parameters: List<Any>,
    val response: List<Response>,
    val results: Int
)