package com.michaelgrigoryan.covidtracker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michaelgrigoryan.covidtracker.R
import com.michaelgrigoryan.covidtracker.ui.fragment.MainFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val home = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, home)
            commit()
        }
    }
}
