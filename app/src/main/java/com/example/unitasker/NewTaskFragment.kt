package com.example.unitasker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.example.unitasker.models.Subject
import com.example.unitasker.models.Task
import com.google.android.flexbox.FlexboxLayout
import com.orm.SugarRecord
import java.time.LocalDate
import java.util.GregorianCalendar

class NewTaskFragment : Fragment() {

    private var editTextTaskName: EditText? = null
    private var layoutSubjectsList: FlexboxLayout? = null
    private var numberDuration: EditText? = null
    private var datePicker: DatePicker? = null
    private var timePicker: TimePicker? = null
    private var datePickerDeadline: DatePicker? = null
    private var checkBoxNotification: CheckBox? = null
    private var checkBoxAssignAuto: CheckBox? = null
    private var buttonSave: Button? = null

    private var selectedSubject: Subject? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.task_form, container, false)
        editTextTaskName = view.findViewById(R.id.task_form_edit_text_name)
        layoutSubjectsList = view.findViewById(R.id.task_form_subject_List)
        numberDuration = view.findViewById(R.id.task_form_duration)
        datePicker = view.findViewById(R.id.task_form_date)
        timePicker = view.findViewById(R.id.task_form_time)
        datePickerDeadline = view.findViewById(R.id.task_form_deadline)
        checkBoxAssignAuto = view.findViewById(R.id.task_form_assign_auto)
        checkBoxNotification = view.findViewById(R.id.task_form_notification)
        buttonSave = view.findViewById(R.id.task_form_button_save)
        buttonSave?.setOnClickListener { createTask() }
        renderSubjects()
        return view
    }

    private fun createTask() {
       val name = editTextTaskName?.text.toString()
       val date = LocalDate.of(datePickerDeadline!!.year, datePickerDeadline!!.month, datePickerDeadline!!.dayOfMonth)
       val duration = try {
           Integer.parseInt(numberDuration!!.text.toString())
       } catch (e: NumberFormatException) {
           -1
       }
       val allowNotification = checkBoxNotification!!.isChecked
       if (name == "") {
           Toast.makeText(context, getString(R.string.please_type_task_name), Toast.LENGTH_LONG).show()
           return
       }
        if (duration <= 0) {
            Toast.makeText(context, getString(R.string.the_task_duration_is_invalid), Toast.LENGTH_LONG).show()
            return
        }
       if (selectedSubject == null) {
          Toast.makeText(context, getString(R.string.please_select_a_subject), Toast.LENGTH_LONG).show()
          return
       }
       if (Task.findByName(name) != null) {
           Toast.makeText(context, getString(R.string.a_task_with_the_typed_name_already_exits), Toast.LENGTH_LONG).show()
           return
       }
       val newTask = Task(name, selectedSubject!!, date, duration, allowNotification)
       newTask.save()
       Toast.makeText(context, getString(R.string.task_created), Toast.LENGTH_LONG).show()
       (activity as MainActivity).changeView(CalendarFragment())
    }

    @SuppressLint("ResourceType")
    private fun subjectButton(subject: Subject): Button {
        val button = Button(ContextThemeWrapper(activity, R.style.Theme_UniTasker_Tag), null, R.style.Theme_UniTasker_Tag)
        val layout = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layout.setMargins(8)
        button.layoutParams = layout
        button.text = subject.name
        button.setBackgroundColor(if (selectedSubject?.id == subject.id) subject.color else Color.parseColor(resources.getString(R.color.gray)))
        button.setOnClickListener {
            selectedSubject = subject
            renderSubjects()
        }
        return button
    }

    private fun renderSubjects() {
        layoutSubjectsList?.removeAllViews()
        val subjects = SugarRecord.listAll(Subject::class.java)
        val subjectsButtons = subjects.map<Subject, Button> { subjectButton(it) }
        subjectsButtons.forEach { button -> layoutSubjectsList?.addView(button) }
    }
}