package com.example.unitasker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class ManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.management, container, false)
        val subjectsButton = view?.findViewById<Button>(R.id.btn_subjects)
        val availabilityButton = view?.findViewById<Button>(R.id.btn_availability)
        subjectsButton?.setOnClickListener {
            (activity as MainActivity).changeView(NewSubjectFragment())
        }
        availabilityButton?.setOnClickListener {
            (activity as MainActivity).changeView(AvailabilityFragment())
        }
        return view
    }
}