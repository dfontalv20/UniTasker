package com.example.unitasker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.timepicker.TimeFormat
import eltos.simpledialogfragment.SimpleDialog.OnDialogResultListener
import eltos.simpledialogfragment.SimpleTimeDialog
import java.lang.Error
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AvailabilityItem @JvmOverloads constructor(
    context: Context, startTime: LocalTime, endTime: LocalTime
) : LinearLayout(context, null) {
    var onDelete: ()->Unit = fun () {}
    var onStartTimeClick: ()->Unit = fun () {}
    var onEndTimeClick: ()->Unit = fun () {}
    var startTime: LocalTime = startTime
        set(value) {
            field = value
            setTimesText()
        }
    var endTime: LocalTime = endTime
        set(value) {
            field = value
            setTimesText()
        }

    private var startTimeText: TextView? = null
    private var endTimeText: TextView? = null
    private var deleteButton: ImageButton? = null

    init {
        this.endTime = endTime
        this.startTime = startTime
        val item = LayoutInflater.from(context)
            .inflate(R.layout.availability_item, this, true)

        startTimeText = item.findViewById(R.id.availability_item_start_time)
        endTimeText = item.findViewById(R.id.availability_item_end_time)
        deleteButton = item.findViewById(R.id.availability_item_delete_button)

        startTimeText?.setOnClickListener { onStartTimeClick() }
        endTimeText?.setOnClickListener { onEndTimeClick() }
        deleteButton?.setOnClickListener { onDelete?.let { it1 -> it1() } }
        setTimesText()
    }

    private fun setTimesText() {
        startTimeText?.text = startTime.format(DateTimeFormatter.ofPattern("h:mm a"))
        endTimeText?.text = endTime.format(DateTimeFormatter.ofPattern("h:mm a"))
    }

}