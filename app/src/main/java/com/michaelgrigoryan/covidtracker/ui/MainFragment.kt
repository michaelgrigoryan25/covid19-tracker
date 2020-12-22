package com.michaelgrigoryan.covidtracker.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.michaelgrigoryan.covidtracker.COVID
import com.michaelgrigoryan.covidtracker.R
import com.michaelgrigoryan.covidtracker.RecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainFragment : Fragment(R.layout.fragment_main) {
    private var countries = mutableListOf<String>()
    private var activeCases = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<FloatingActionButton>(R.id.refresh_data)

        val rv = view.findViewById<RecyclerView>(R.id.data_list)
        rv.layoutManager = LinearLayoutManager(view.context)
        rv.adapter = RecyclerAdapter(countries, activeCases /*,newCases*/)

        button.setOnClickListener {
            stats()
        }

        stats()
    }

    private fun stats() {
        val spinner = view?.findViewById<ProgressBar>(R.id.progressBar)!!
        val rv = view?.findViewById<RecyclerView>(R.id.data_list)!!
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
                        countries.clear()
                        activeCases.clear()

                        data.response.forEach {
                            countries.add(it.country)
                            activeCases.add(it.cases.active.toString())
                        }
                        countries.remove("All")
                        rv.visibility = View.VISIBLE
                        spinner.visibility = View.GONE
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(view?.context, "Whoops, something bad happened", Toast.LENGTH_SHORT).show()
                    Log.e("ERROR", e.toString())
                }
            }
        }
    }
}