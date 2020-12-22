package com.michaelgrigoryan.covidtracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
        private var countries: List<String>,
        private  var activeCases: List<String>) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder> () {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val country: TextView = itemView.findViewById(R.id.tv_title)
        val activeCases: TextView = itemView.findViewById(R.id.active_cases)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.data_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (activeCases[position].toInt() > 1000) {
            holder.activeCases.setTextColor(Color.parseColor("#FE4A49"))
        } else {
            holder.activeCases.setTextColor(Color.parseColor("#3BC14A"))
        }
        holder.country.text = countries[position]
        holder.activeCases.text = activeCases[position]
    }
}