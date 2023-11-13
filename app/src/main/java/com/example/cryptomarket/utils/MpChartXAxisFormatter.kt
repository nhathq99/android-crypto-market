package com.example.cryptomarket.utils

import android.R.attr
import android.util.Log
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MpChartXAxisFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        // Convert float value to date string
        // Convert from seconds back to milliseconds to format time  to show to the user

        // Convert float value to date string
        // Convert from seconds back to milliseconds to format time  to show to the user
        val emissionsMilliSince1970Time = value.toLong()
        // Show time in local version

        // Show time in local version
        val timeMilliseconds = Date(emissionsMilliSince1970Time)
        val dateTimeFormat = SimpleDateFormat("HH:MM", Locale.getDefault())

        return dateTimeFormat.format(timeMilliseconds)
    }
}
