package com.test.epg.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.epg.R
import com.test.epg.util.convertDpToPx
import com.test.epg.util.getDate
import com.test.epg.adapters.ChannelDetailRowAdapter
import com.test.epg.adapters.ChannelInfoAdapter
import com.test.epg.viewmodel.MainViewModel
import com.test.epg.adapters.TimeHeaderAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.Calendar.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val showAdapter = ChannelInfoAdapter(arrayListOf())
    private val channelDetailRowAdapter =
        ChannelDetailRowAdapter(arrayListOf(), Calendar.getInstance(), 0)
    private val timeHeaderAdapter = TimeHeaderAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initViews()
        initRecyclerViews()
        observeModel()

    }


    private fun initRecyclerViews() {
        tableD.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = channelDetailRowAdapter
        }

        tableC.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = showAdapter
        }

        timeHeader.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = timeHeaderAdapter
        }
    }

    private fun scrollToCurrentShow() {
        var currentDate = getDate(Calendar.getInstance().timeInMillis, "dd/MMM/yyyy")
        val currentSelectedDateString =
            getDate(viewModel.calendarSelectedDate.value!!.timeInMillis, "dd/MMM/yyyy")

        if (currentDate.equals(currentSelectedDateString)) {
            var currentRunningShowPosition =
                convertDpToPx(tableD.context, 100f) * viewModel.currentShowPosition.value!!
            horizontalScrollView.smoothScrollTo(currentRunningShowPosition, 0)
            horizontalHeaderScrollView.smoothScrollTo(currentRunningShowPosition, 0)
        } else {
            horizontalScrollView.smoothScrollTo(0, 0)
            horizontalHeaderScrollView.smoothScrollTo(0, 0)
        }


    }

    private fun initViews() {

        ViewCompat.setNestedScrollingEnabled(tableC, false)
        tableD.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(tableD, false)
        ViewCompat.setNestedScrollingEnabled(timeHeader, false)

        horizontalScrollView.setOnScrollChangeListener() { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            horizontalHeaderScrollView.scrollTo(i, 0)

        }

        horizontalHeaderScrollView.setOnScrollChangeListener() { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            horizontalScrollView.scrollTo(i, 0)
        }

        floatingActionButtonRefresh.setOnClickListener({
            scrollToCurrentShow()
        })


        floatingActionButtonChangeDate.setOnClickListener({
            openDatePickerDialog()
        })
    }


    private fun observeModel() {
        viewModel.updateCalendarDate(Calendar.getInstance())
        viewModel.refresh()


        viewModel.epgModel.observe(this, Observer { epgModel ->
            epgModel?.let {
                showAdapter.updateData(epgModel)
                channelDetailRowAdapter.updateData(
                    epgModel, viewModel.calendarSelectedDate.value!!, 0
                )
                viewModel.currentRunningShowPosition(viewModel.timeMutableLiveData.value!!)
            }
        })

        viewModel.timeMutableLiveData.observe(this, Observer { time ->
            time?.let {
                timeHeaderAdapter.updateData(time)
            }
        })

        viewModel.currentShowPosition.observe(this, Observer { currentPosition ->
            channelDetailRowAdapter.updateData(
                viewModel.epgModel.value!!, viewModel.calendarSelectedDate.value!!,
                viewModel.currentShowPosition.value!!
            )

            Timer("ScrollingToCurrentShow", false).schedule(5000) {
                scrollToCurrentShow()
            }

        })

        viewModel.calendarSelectedDate.observe(this, Observer { calendar ->
            tvDate.text = getDate(calendar.timeInMillis, "dd/MMM/yyyy")
        })

        viewModel.progressDialog.observe(this, Observer { boleanFlag ->
            if (boleanFlag) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }


    fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog =
            DatePickerDialog(
                this,
                R.style.SpinnerDatePickerDialog,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(DAY_OF_MONTH, dayOfMonth)
                    calendar.set(YEAR, year)
                    calendar.set(MONTH, monthOfYear)
                    viewModel.updateCalendarDate(calendar)
                    viewModel.refresh()
                },
                calendar.get(YEAR),
                calendar.get(MONTH),
                calendar.get(DAY_OF_MONTH)
            )


        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        calendar.add(DAY_OF_MONTH, 2)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }
}
