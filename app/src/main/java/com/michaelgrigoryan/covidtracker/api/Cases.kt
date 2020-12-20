package com.michaelgrigoryan.covidtracker.api

data class Cases(
        val `1M_pop`: String,
        var active: Int,
        val critical: Any,
        var new: Any,
        val recovered: Int,
        val total: Int
)