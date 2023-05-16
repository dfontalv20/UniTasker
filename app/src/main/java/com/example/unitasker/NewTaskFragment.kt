package com.example.unitasker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.unitasker.models.Task

class NewTaskFragment : Fragment() {

    var editTextTaskName: EditText? = null
    var buttonSave: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.task_form, container, false)
        editTextTaskName = view.findViewById<EditText>(R.id.task_form_edit_text_name)
        buttonSave = view.findViewById<Button>(R.id.task_form_button_save)
        buttonSave?.setOnClickListener { createTask() }
        return view
    }

    private fun createTask() {
       val newTask = Task(editTextTaskName?.text.toString())
       newTask.save()
    }
}