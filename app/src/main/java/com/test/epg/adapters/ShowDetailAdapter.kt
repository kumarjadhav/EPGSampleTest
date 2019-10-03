package com.test.epg.adapters

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.epg.R
import com.test.epg.databinding.ItemShowDetailsBinding
import com.test.epg.model.ChannelShowModel
import com.test.epg.util.getDate
import java.util.*
import kotlin.collections.ArrayList

class ShowDetailAdapter(
    val channelShowModel: ArrayList<ChannelShowModel>,
    var currentSelectedDate: Calendar,
    var currentShowPosition: Int
) :
    RecyclerView.Adapter<ShowDetailAdapter.ShowDetailViewHolder>() {


    fun updateData(
        channelShowModel: ArrayList<ChannelShowModel>,
        currentSelectedDate: Calendar,
        currentShowPosition: Int
    ) {
        this.channelShowModel.clear()
        this.channelShowModel.addAll(channelShowModel)
        this.currentShowPosition = currentShowPosition
        this.currentSelectedDate = currentSelectedDate
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemShowDetailsBinding>(
                inflater,
                R.layout.item_show_details,
                parent,
                false
            )
        return ShowDetailViewHolder(view)
    }

    override fun getItemCount() = channelShowModel.size

    override fun onBindViewHolder(holder: ShowDetailViewHolder, position: Int) {

        holder.view.showInfo = channelShowModel[position]

        var currentDate = getDate(Calendar.getInstance().timeInMillis, "dd/MMM/yyyy")
        val currentSelectedDateString = getDate(currentSelectedDate.timeInMillis, "dd/MMM/yyyy")

        if (currentDate.equals(currentSelectedDateString)) {
            if (position == this.currentShowPosition) {
                holder.view.tvChannelName.setTextColor(
                    ContextCompat.getColor(
                        holder.view.tvChannelName.context,
                        R.color.colorAccent
                    )
                )
                holder.view.verticalLine.visibility = VISIBLE
            } else {
                holder.view.tvChannelName.setTextColor(
                    ContextCompat.getColor(
                        holder.view.tvChannelName.context,
                        R.color.black
                    )
                )
                holder.view.verticalLine.visibility = GONE
            }
        } else {
            holder.view.tvChannelName.setTextColor(
                ContextCompat.getColor(
                    holder.view.tvChannelName.context,
                    R.color.black
                )
            )
            holder.view.verticalLine.visibility = GONE
        }


        /*holder.view.verticalLine.visibility =
            if (position == this.currentShowPosition) VISIBLE else GONE*/

    }


    class ShowDetailViewHolder(var view: ItemShowDetailsBinding) :
        RecyclerView.ViewHolder(view.root) {}

}