package com.michaelgrigoryan.covidtracker.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.michaelgrigoryan.covidtracker.R
import com.michaelgrigoryan.covidtracker.api.APIService
import com.michaelgrigoryan.covidtracker.ui.adapter.RecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {
    private var countries = mutableListOf<String>()
    private var activeCases = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.search_results)
        rv.layoutManager = LinearLayoutManager(view.context)
        rv.adapter = RecyclerAdapter(countries, activeCases)
        val searchField = view.findViewById<TextInputEditText>(R.id.search_field)
        searchField.addTextChangedListener(textWatcher)

    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            fetch(s)
        }
    }

    private fun fetch(s: CharSequence?) {
        val pb = view?.findViewById<ProgressBar>(R.id.search_progress_bar)!!
        val rv = view?.findViewById<RecyclerView>(R.id.search_results)!!
        val notFound = view?.findViewById<TextView>(R.id.no_search_results)!!

        pb.visibility = View.VISIBLE
        notFound.visibility = View.GONE
        val api = Retrofit.Builder()
            .baseUrl("https://covid-193.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getStats().awaitResponse()

                if (response.isSuccessful) {
                    val data = response.body()!!
                    withContext(Dispatchers.Main) {

                        countries.clear()
                        activeCases.clear()

                        data.response.forEach {
                            if (it.country == s.toString() || it.country.contains(s.toString())) {
                                Timber.d(it.country)
                                countries.add(it.country)
                                activeCases.add(it.cases.active.toString())
                            }
                        }

                        countries.remove("All")

                        if (countries.size == 0) {
                            notFound.visibility = View.VISIBLE
                            notFound.text = "Nothing was found"
                        }

                        rv.visibility = View.VISIBLE
                        pb.visibility = View.GONE
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
