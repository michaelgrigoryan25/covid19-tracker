package com.example.usingapis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var countries = mutableListOf<String>()
    private var activeCases = mutableListOf<String>()
    private var newCases = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<FloatingActionButton>(R.id.refresh_data)

        val rv = findViewById<RecyclerView>(R.id.data_list)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RecyclerAdapter(countries, activeCases /*,newCases*/)

        button.setOnClickListener {
            stats()
        }

        stats()
    }

    private fun stats() {
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        val rv = findViewById<RecyclerView>(R.id.data_list)
        rv.visibility = View.GONE
        spinner.visibility = View.VISIBLE
        val api = Retrofit.Builder()
            .baseUrl("https://covid-193.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(COVID::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getStats().awaitResponse()

                if (response.isSuccessful) {
                    val data = response.body()!!
                    withContext(Dispatchers.Main) {

                        countries.removeAll(countries)
                        activeCases.removeAll(activeCases)
                        newCases.removeAll(activeCases)


                        data.response.forEach {
                            countries.add(it.country)
                            activeCases.add(it.cases.active.toString())
//                            newCases.add(it.cases.new.toString())
                        }

                        countries.remove("All")
                        countries.sort()

                        rv.visibility = View.VISIBLE
                        spinner.visibility = View.GONE
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Whoops, something bad happened", Toast.LENGTH_SHORT).show()
                    Log.e("ERROR", e.toString())
                }
            }
        }
    }
}