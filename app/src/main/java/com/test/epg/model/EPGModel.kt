package com.test.epg.model

import com.google.gson.annotations.SerializedName

data class EPGModel(
    @SerializedName("id") val id: String,
    @SerializedName("channelName") val channelName: String,
    @SerializedName("channelLogo") val channelLogo: String,
    @SerializedName("showList") val showsList: ArrayList<ChannelShowModel>?
) {
    override fun toString(): String {
        return "EPGModel(id='$id', channelName='$channelName', channelLogo='$channelLogo', showsList=$showsList)"
    }
}