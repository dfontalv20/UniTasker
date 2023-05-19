package com.example.unitasker

import android.app.ActionBar.LayoutParams
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import com.example.unitasker.models.Task
import com.orm.SugarRecord
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.Calendar
import java.util.GregorianCalendar

class CalendarFragment : Fragment() {

    private var calendar: CalendarView? = null
    private var tasksView: LinearLayout? = null

    private var yearSelected: Int = LocalDate.now().year
    private var monthSelected: Int = LocalDate.now().monthValue
    private var daySelected: Int = LocalDate.now().dayOfMonth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calendar, container, false)
        tasksView = view.findViewById(R.id.tasks_linear_layout)
        calendar = view.findViewById(R.id.home_calendar)
        loadTasks()
        calendar?.setOnDateChangeListener { _, year, month, day ->
            yearSelected = year
            monthSelected = month + 1
            daySelected = day
            loadTasks()
        }
        return view
    }

    private fun loadTasks() {
        tasksView?.removeAllViews()
            val xd = Month.of(monthSelected)
        val tasks: List<Task> = Task.findByDate(LocalDate.of(yearSelected, Month.of(monthSelected), daySelected))
        val context = this.context
        if (context === null || tasksView === null) {
            return
        }
        val cards = tasks.map { task ->
            val taskSubject = task.subject!!
            return@map TaskCard(
                context,
                TaskCardAttributes(
                    task.title, taskSubject.name, "${task.duration} ${getString(R.string.hours)}", Color.valueOf(taskSubject.color)
                )
            ) { onDeleteTask(task) }
        }
        cards.forEach { task -> tasksView?.addView(task) }
    }

    private fun onDeleteTask(task: Task) {
        val confirmDialog = AlertDialog.Builder(context)
        confirmDialog.setTitle(getString(R.string.delete_task))
        confirmDialog.setMessage(getString(R.string.are_you_sure_of_delete_this_task))
        confirmDialog.setPositiveButton(android.R.string.ok) { _, _ ->
            task.delete()
            loadTasks()
        }
        confirmDialog.setNegativeButton(android.R.string.cancel, null)
        confirmDialog.show()
    }
}