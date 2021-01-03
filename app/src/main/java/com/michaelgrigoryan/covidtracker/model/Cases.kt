package com.michaelgrigoryan.covidtracker.model

data class Cases(
        val `1M_pop`: String,
        var active: Int,
        val critical: Int,
        var new: String,
        val recovered: Int,
        val total: Int
)