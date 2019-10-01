package com.test.epg.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.epg.R
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException
import kotlin.math.roundToInt


fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

fun getDate(milliSeconds: Long, dateFormat: String): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds)
    return formatter.format(calendar.getTime())
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}


fun getMilliFromStringDate(dateString: String, format: String): Long {
    var date: Date? = Date()
    val formatter = SimpleDateFormat(format)
    try {
        date = formatter.parse(dateString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date?.time!!
}


fun convertDpToPx(context: Context, dp: Float): Int {
    return (dp * context.resources.displayMetrics.density).roundToInt()
}

