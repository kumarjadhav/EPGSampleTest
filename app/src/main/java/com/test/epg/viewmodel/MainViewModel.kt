package com.test.epg.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.test.epg.R
import com.test.epg.model.EPGModel
import com.test.epg.util.getCurrentDateTime
import com.test.epg.util.getDate
import com.test.epg.util.getMilliFromStringDate
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


public class MainViewModel(application: Application) : AndroidViewModel(application) {

    val epgModel = MutableLiveData<List<EPGModel>>()
    val timeMutableLiveData = MutableLiveData<ArrayList<Long>>()
    val currentShowPosition = MutableLiveData<Int>()
    val calendarSelectedDate = MutableLiveData<Calendar>()
    val progressDialog = MutableLiveData<Boolean>()

    fun refresh() {
        getTimeData()
        getEPGData()
    }

    fun getEPGData() {
        val today = getDate(Calendar.getInstance().timeInMillis, "dd/MM/yyyy");
        val calendarTommorow = Calendar.getInstance()
        calendarTommorow.add(Calendar.DAY_OF_MONTH, 1)
        val tommorow = getDate(calendarTommorow.timeInMillis, "dd/MM/yyyy")
        val calendarTheDayAfterTommoriw = Calendar.getInstance()
        calendarTheDayAfterTommoriw.add(Calendar.DAY_OF_MONTH, 2)
        val theDayAfterTommorow = getDate(calendarTheDayAfterTommoriw.timeInMillis, "dd/MM/yyyy")


        calendarSelectedDate.value?.timeInMillis?.let {
            val selectedDate = getDate(it, "dd/MM/yyyy")
            if (selectedDate.equals(today)!!) {
                readJsonFromRawAsset(getApplication(), R.raw.epg_data).execute()
            } else if (selectedDate.equals(tommorow)!!) {
                readJsonFromRawAsset(getApplication(), R.raw.epg_data_tommorow).execute()
            } else if (selectedDate.equals(theDayAfterTommorow)!!) {
                readJsonFromRawAsset(getApplication(), R.raw.epg_data_day_after_tommorow).execute()
            } else {
                readJsonFromRawAsset(getApplication(), R.raw.epg_data).execute()
            }
        }


    }

    fun getTimeData() {

        var calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.time

        var arrayListTime = arrayListOf<Long>()
        for (i in 0..48) {
            arrayListTime.add(calendar.timeInMillis)
            calendar.add(Calendar.MINUTE, 30)
        }


        timeMutableLiveData.value = arrayListTime
    }

    fun currentRunningShowPosition(timeInMilisecondsArray: ArrayList<Long>) {
        val currentTimeInMilis = getCurrentDateTime().time
        for ((position, value) in timeInMilisecondsArray.withIndex()) {
            if (timeInMilisecondsArray.get(position) < currentTimeInMilis.toLong() && currentTimeInMilis.toLong() < timeInMilisecondsArray.get(
                    position + 1
                )
            ) {
                //Current Running shows
                currentShowPosition.value = position
            }
        }
    }

    fun updateCalendarDate(calendar: Calendar) {
        calendarSelectedDate.value = calendar
    }

    inner class readJsonFromRawAsset(val context: Context, val file: Int) :
        AsyncTask<Void, Void, String>() {


        override fun doInBackground(vararg params: Void?): String? {

            var json: String? = null
            try {

                val inputStream: InputStream =
                    context.resources.openRawResource(file)
                json = inputStream.bufferedReader().use { it.readText() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }
            return json
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.value = true
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var arrayList: List<EPGModel>
            var gson = Gson()
            arrayList = gson.fromJson(result, Array<EPGModel>::class.java).asList()
            epgModel.value = arrayList
            progressDialog.value = false

        }

    }


}