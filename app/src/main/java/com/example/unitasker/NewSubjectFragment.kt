package com.example.unitasker

import android.R.attr.buttonStyle
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout.LayoutParams
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.example.unitasker.models.Subject
import com.google.android.flexbox.FlexboxLayout
import com.orm.SugarRecord
import eltos.simpledialogfragment.SimpleDialog.OnDialogResultListener
import eltos.simpledialogfragment.color.SimpleColorWheelDialog


class NewSubjectFragment : Fragment(), OnDialogResultListener {

    private val defaultColor: Color = Color.valueOf(Color.RED)

    private var buttonColor: Button? = null
    private var editTextName: EditText? = null
    private var buttonSave: Button? = null
    private var layoutSubjectsList: FlexboxLayout? = null

    var selectedColor: Color = defaultColor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.subject_form, container, false)
        buttonColor = view.findViewById(R.id.subject_form_color_button)
        buttonColor?.setBackgroundColor(selectedColor.toArgb())
        editTextName = view.findViewById(R.id.subject_form_name_edit_text)
        buttonSave = view.findViewById(R.id.subject_form_save_button)
        layoutSubjectsList = view.findViewById(R.id.subject_form_subjects_list)
        buttonColor?.setOnClickListener { openColorPickerDialog() }
        buttonSave?.setOnClickListener { createSubject() }
        renderSubjects()
        return view
    }

    private fun openColorPickerDialog() {
        SimpleColorWheelDialog.build()
            .hideHexInput(true)
            .show(this, "COLOR_PICKER")
    }

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        if (which != OnDialogResultListener.BUTTON_POSITIVE) return true
        @ColorInt val color = extras.getInt(SimpleColorWheelDialog.COLOR)
        selectedColor = Color.valueOf(color)
        buttonColor?.setBackgroundColor(color)
        return true
    }

    private fun createSubject() {
        val name = editTextName?.text.toString()
        if (name == "") {
            Toast.makeText(this.context, getString(R.string.please_type_a_name), Toast.LENGTH_LONG).show()
            return
        }
        val newSubject = Subject(name, selectedColor)
        newSubject.save()
        resetForm()
        Toast.makeText(activity, getString(R.string.subject_created), Toast.LENGTH_LONG).show()
        renderSubjects()
    }

    private fun resetForm() {
        editTextName?.setText("")
        selectedColor = defaultColor
        buttonColor?.setBackgroundColor(selectedColor.toArgb())
    }

    private fun subjectButton(subject: Subject): Button {
        val button = Button(ContextThemeWrapper(activity, R.style.Theme_UniTasker_Tag), null, R.style.Theme_UniTasker_Tag)
        val layout = LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT)
        layout.setMargins(8)
        button.layoutParams = layout
        button.text = subject.name
        button.setBackgroundColor(subject.color)
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_trash, 0);
        button.setOnClickListener { onDeleteSubject(subject) }
        return button
    }

    private fun onDeleteSubject(subject: Subject) {
        val confirmDialog = AlertDialog.Builder(context)
        confirmDialog.setTitle("Delete subject")
        confirmDialog.setMessage("Are you sure you wanna delete this subject? All tasks assigned to this subject will be removed too")
        confirmDialog.setPositiveButton(android.R.string.ok) { _, _ ->
            subject.delete()
            renderSubjects()
        }
        confirmDialog.setNegativeButton(android.R.string.cancel, null)
        confirmDialog.show()
    }

    private fun renderSubjects() {
        layoutSubjectsList?.removeAllViews()
        val subjects = SugarRecord.listAll(Subject::class.java)
        val subjectsButtons = subjects.map<Subject, Button> { subjectButton(it) }
        subjectsButtons.forEach { button -> layoutSubjectsList?.addView(button) }
    }
}