package com.test.epg.model

import com.google.gson.annotations.SerializedName

data class ChannelShowModel(
    @SerializedName("id") val id: String,
    @SerializedName("showName") val name: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val showLength: String,
    @SerializedName("coverImage") val coverImage: String
)