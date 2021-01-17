package com.michaelgrigoryan.covidtracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michaelgrigoryan.covidtracker.R
import com.michaelgrigoryan.covidtracker.model.Response
import com.michaelgrigoryan.covidtracker.util.formatNumber
import kotlinx.android.synthetic.main.item_report.view.*
import java.util.*


class ReportsAdapter : RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>() {
    private var dataList: List<Response> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        return ReportsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) =
        holder.bind(dataList[position])

    fun setData(data: List<Response>) {
        this.dataList = data
        notifyDataSetChanged()
    }

    class ReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Response) = with(itemView) {
            countryName.text = data.country.plus(" - ").plus(data.continent)
            total.text = formatNumber(data.cases.total)
            recovered.text = formatNumber(data.cases.recovered)
            deaths.text = formatNumber(data.deaths.total)
        }
    }
}