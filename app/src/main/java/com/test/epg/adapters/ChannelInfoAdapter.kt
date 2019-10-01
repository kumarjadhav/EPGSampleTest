package com.test.epg.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.epg.R
import com.test.epg.databinding.ItemShowHeaderBinding
import com.test.epg.model.EPGModel

class ChannelInfoAdapter(val epgModel: ArrayList<EPGModel>) :
    RecyclerView.Adapter<ChannelInfoAdapter.ShowViewHolder>() {


    fun updateData(epgModel: List<EPGModel>) {
        this.epgModel.clear()
        this.epgModel.addAll(epgModel)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemShowHeaderBinding>(
                inflater,
                R.layout.item_show_header,
                parent,
                false
            )
        return ShowViewHolder(view)
    }

    override fun getItemCount() = epgModel.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {

        holder.view.channelInfo = epgModel[position]


    }


    class ShowViewHolder(var view: ItemShowHeaderBinding) : RecyclerView.ViewHolder(view.root) {}

}