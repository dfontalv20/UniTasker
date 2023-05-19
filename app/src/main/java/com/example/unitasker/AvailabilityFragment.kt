package com.example.unitasker

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.unitasker.models.AvailabilitySchedule
import com.google.android.flexbox.FlexboxLayout
import eltos.simpledialogfragment.SimpleDialog.OnDialogResultListener
import eltos.simpledialogfragment.SimpleTimeDialog
import java.time.LocalTime

class AvailabilityFragment : Fragment(), OnDialogResultListener {

    private val timePickerStartTag = "START_TIME_PICKER"
    private val timePickerEndTag = "END_TIME_PICKER"

    private var schedulesLayout: LinearLayout? = null
    private var daysLayout: FlexboxLayout? = null
    private val days = listOf(
        R.string.monday, R.string.tuesday, R.string.wednesday,
        R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday
    )

    private var selectedSchedule: AvailabilitySchedule? = null

    private var selectedDay = days[0]
        set(value) {
            field = value
            daysButtons.forEach { button ->
                val valueText = getString(value)
                val textColor = if (valueText === button.text) R.color.primary else R.color.secondary
                button.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
                loadSchedules()
            }
        }

    private var daysButtons = listOf<Button>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.availability_form, container, false)
        daysLayout = view.findViewById(R.id.availability_days_layout)
        schedulesLayout = view.findViewById(R.id.availability_item_layout)
        val addButton = view.findViewById<Button>(R.id.availability_add_schedule_button)

        loadSchedules()

        daysButtons = days.map { day ->
            val button = Button(ContextThemeWrapper(activity, R.style.Theme_UniTasker_TextButton))
            button.text = getString(day)
            button.setTextColor(ResourcesCompat.getColor(resources, R.color.secondary, null))
            button.setOnClickListener { selectedDay = day }
            return@map button
        }
        daysButtons.forEach { daysLayout?.addView(it) }

        addButton.setOnClickListener { addSchedule() }
        return view
    }

    private fun loadSchedules() {
        schedulesLayout?.removeAllViews()
        val schedules = AvailabilitySchedule.findByDay(days.indexOf(selectedDay))
        schedules.forEach { schedule ->
            val scheduleItem = AvailabilityItem(requireContext(), schedule.startTime, schedule.endTime)
            scheduleItem?.onDelete = {
                schedule.delete()
                Toast.makeText(context, "Schedule deleted", Toast.LENGTH_SHORT).show()
                loadSchedules()
            }
            scheduleItem?.onStartTimeClick = {
                selectedSchedule = schedule
                showStartTimePicker(schedule.startTime)
            }
            scheduleItem?.onEndTimeClick = {
                selectedSchedule = schedule
                showEndTimePicker(schedule.endTime)
            }
            schedulesLayout?.addView(scheduleItem)
        }
    }

    private fun showStartTimePicker(startTime: LocalTime) {
        SimpleTimeDialog.build()
            .hour(startTime.hour)
            .minute(startTime.hour)
            .neg()
            .show(this, timePickerStartTag)
    }
    private fun showEndTimePicker(endTime: LocalTime) {
        SimpleTimeDialog.build()
            .hour(endTime.hour)
            .minute(endTime.hour)
            .neg()
            .show(this, timePickerEndTag)
    }

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        if (which != OnDialogResultListener.BUTTON_POSITIVE) return true
        val hour = extras.getInt(SimpleTimeDialog.HOUR)
        val minute = extras.getInt(SimpleTimeDialog.MINUTE)
        val newTime = LocalTime.of(hour, minute, 0)
        val isEditingStartTime = dialogTag == timePickerStartTag
        if (
            (isEditingStartTime && newTime.isAfter(selectedSchedule?.endTime)) ||
            (!isEditingStartTime && newTime.isBefore(selectedSchedule?.startTime))
        ) {
            Toast.makeText(requireContext(), "Start time must be before end time", Toast.LENGTH_SHORT).show()
            selectedSchedule = null
            return true
        }
        when (dialogTag) {
            timePickerStartTag -> selectedSchedule?.startTime = newTime
            timePickerEndTag -> selectedSchedule?.endTime = newTime
        }
        selectedSchedule?.save()
        selectedSchedule = null
        loadSchedules()
        return true
    }

    private fun addSchedule() {
        AvailabilitySchedule(
            days.indexOf(selectedDay),
            LocalTime.of(2, 0),
            LocalTime.of(4, 0)
        ).save()
        loadSchedules()
    }
}