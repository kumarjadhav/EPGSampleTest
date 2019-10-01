package com.test.epg.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.epg.R
import com.test.epg.databinding.ItemTimeHeaderBinding
import com.test.epg.util.getDate

class TimeHeaderAdapter(val timeInMilisecondsArray: ArrayList<Long>) :
    RecyclerView.Adapter<TimeHeaderAdapter.ShowViewHolder>() {


    fun updateData(timeList: ArrayList<Long>) {
        this.timeInMilisecondsArray.clear()
        this.timeInMilisecondsArray.addAll(timeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemTimeHeaderBinding>(
                inflater,
                R.layout.item_time_header,
                parent,
                false
            )
        return ShowViewHolder(view)
    }

    override fun getItemCount() = timeInMilisecondsArray.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        
        holder.view.tvTime.text =
            getDate(timeInMilisecondsArray.get(position), "hh:mm a")

    }


    class ShowViewHolder(var view: ItemTimeHeaderBinding) : RecyclerView.ViewHolder(view.root) {}

}