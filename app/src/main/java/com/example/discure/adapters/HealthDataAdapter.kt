package com.example.discure.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discure.R
import com.example.discure.models.HealthData

class HealthDataAdapter(
    private var healthDataList: List<HealthData>,
    private val onMoreClickListener: (HealthData, ImageButton?) -> Unit
) :
    RecyclerView.Adapter<HealthDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        val moreButton = view.findViewById<ImageButton>(R.id.btn_more)
        return ViewHolder(view,moreButton)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val healthData = healthDataList[position]
        holder.bind(healthData)

        val moreButton = holder.itemView.findViewById<ImageButton>(R.id.btn_more)
        moreButton.setOnClickListener {
            onMoreClickListener.invoke(healthData,moreButton)
        }
    }

    override fun getItemCount(): Int {
        return healthDataList.size
    }

    fun setData(newList: List<HealthData>) {
        healthDataList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val moreButton: ImageButton) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        private val valueTextView: TextView = itemView.findViewById(R.id.tv_value)

        fun bind(healthData: HealthData) {
            dateTextView.text = healthData.date
            valueTextView.text = healthData.value
        }
    }
}
