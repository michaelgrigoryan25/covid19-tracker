package com.michaelgrigoryan.covidtracker.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.michaelgrigoryan.covidtracker.R
import com.michaelgrigoryan.covidtracker.api.RetrofitClient
import com.michaelgrigoryan.covidtracker.ui.adapter.ReportsAdapter
import com.michaelgrigoryan.covidtracker.util.formatDate
import com.michaelgrigoryan.covidtracker.util.formatNumber
import com.michaelgrigoryan.covidtracker.util.getCurrentDate
import com.michaelgrigoryan.covidtracker.util.getLastUpdate
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main) {
    private var currentDate: String? = null
    private lateinit var reportsAdapter: ReportsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Get Current Date*/
        currentDate = getCurrentDate()

        /*Initialise reports adapter*/
        reportsAdapter = ReportsAdapter()
        statistics.apply {
            statistics.adapter = reportsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        /*Handle date picker*/
        datePicker.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(getString(R.string.select_date))

            val constraintsBuilder = CalendarConstraints.Builder()
            val calendar = Calendar.getInstance()
            val dateValidator = DateValidatorPointBackward.before(calendar.timeInMillis)
            constraintsBuilder.setValidator(dateValidator)
            builder.setCalendarConstraints(
                constraintsBuilder
                    .build()
            )

            val picker = builder.build()
            picker.show(childFragmentManager, picker.toString())

            picker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val timeZoneOffset = timeZone.getOffset(Date().time * -1)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateFormat = Date(it + timeZoneOffset)

                date.text = sdf.format(dateFormat)

                currentDate = sdf.format(dateFormat)

                history(sdf.format(dateFormat))

            }
        }

        /*Click to refresh history and statistics*/
        refreshData.setOnClickListener {
            history(currentDate!!)
            statistics()
        }

        /*Fetch history*/
        history(currentDate!!)

        /*Fetch Statistics*/
        statistics()
    }

    private fun history(currentDate: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitClient.getRetrofit().getAPIService().getHistory("all", currentDate)
                        .awaitResponse()

                if (response.isSuccessful) {
                    val data = response.body()!!
                    withContext(Dispatchers.Main) {

                        /*Fetch latest response item*/
                        val responseData = data.response[0]

                        /*Show last update information*/
                        lastUpdate.text = getString(R.string.last_updated).plus(" ")
                            .plus(getLastUpdate(data.response[0].time))

                        /*Show current history date*/
                        date.text = formatDate(responseData.time)

                        /*Recoveries*/
                        totalRecoveries.text = formatNumber(responseData.cases.recovered)

                        /*Deaths*/
                        totalDeaths.text = formatNumber(responseData.deaths.total)
                        newDeaths.text = " (+".plus(formatNumber(responseData.deaths.new.toInt())).plus(")")

                        /*Active*/
                        totalActive.text = formatNumber(responseData.cases.active)
                        critical.text = " (".plus(formatNumber(responseData.cases.critical)).plus(")")

                        /*Cases*/
                        totalCases.text = formatNumber(responseData.cases.total)
                        newCases.text = " (+".plus(formatNumber(responseData.cases.new.toInt())).plus(")")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        view?.context,
                        "It looks like you're not connected to the internet",
                        Toast.LENGTH_SHORT
                    ).show()
                    Timber.d(e)
                }
            }
        }
    }

    private fun statistics() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getRetrofit().getAPIService().getStats().awaitResponse()

            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    /*Pass response data to recycler adapter */
                    reportsAdapter.setData(data!!.response)
                }
            }
        }
    }
}