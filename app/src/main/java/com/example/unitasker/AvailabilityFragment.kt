package com.example.unitasker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

class AvailabilityFragment : Fragment() {

    var startTime: TextView? = null
    var endTime: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.availability_form, container, false)
        startTime = view?.findViewById(R.id.start_time)
        endTime = view?.findViewById(R.id.end_time)
        startTime?.setOnClickListener { showStartTimePicker() }
        endTime?.setOnClickListener { showEndTimePicker() }
        return view
    }

    private fun showStartTimePicker() {
        showDialog()
    }
    private fun showEndTimePicker() {
        showDialog()
    }
    private fun showDialog() {
        TimePickerFragment().show(this.parentFragmentManager, "time-picker")
    }

    class TimePickerFragment : DialogFragment() {
        private var hour: Int
        private var minute: Int
        init {
            val now = LocalTime.now()
            hour = now.hour
            minute = now.minute
        }
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return TimePickerDialog(requireActivity(),null, hour, minute, false)
        }
    }
}