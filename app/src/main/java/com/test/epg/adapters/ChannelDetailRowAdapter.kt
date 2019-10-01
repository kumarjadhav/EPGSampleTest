package com.test.epg.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.epg.R
import com.test.epg.databinding.ItemChannelDetailRowBinding
import com.test.epg.model.EPGModel
import android.content.Context


class ChannelDetailRowAdapter(
    var epgModel: ArrayList<EPGModel>,
    var date: String,
    var currentShowPosition: Int
) :
    RecyclerView.Adapter<ChannelDetailRowAdapter.ChannelDetailViewHolder>() {


    fun updateData(epgModel: List<EPGModel>, date: String, currentShowPosition: Int) {
        this.epgModel.clear()
        this.epgModel.addAll(epgModel)
        this.date = date
        this.currentShowPosition = currentShowPosition
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemChannelDetailRowBinding>(
                inflater,
                R.layout.item_channel_detail_row,
                parent,
                false
            )
        return ChannelDetailViewHolder(view)
    }

    override fun getItemCount() = epgModel!!.size

    override fun onBindViewHolder(holder: ChannelDetailViewHolder, position: Int) {

        val showDetailsAdapter =
            ShowDetailAdapter(arrayListOf(), currentShowPosition)

        holder.view.recyclerViewRow.apply {

            layoutManager =
                CustomLinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = showDetailsAdapter

            epgModel.get(position).showsList?.let {
                showDetailsAdapter.updateData(it, currentShowPosition)
            }

            ViewCompat.setNestedScrollingEnabled(holder.view.recyclerViewRow, false)
            //holder.view.recyclerViewRow.isNestedScrollingEnabled


        }


    }

    class ChannelDetailViewHolder(var view: ItemChannelDetailRowBinding) :
        RecyclerView.ViewHolder(view.root) {

    }

}


class CustomLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {


    override fun canScrollHorizontally(): Boolean {
        return false
    }
}