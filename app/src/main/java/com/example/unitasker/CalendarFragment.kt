package com.example.unitasker

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import com.example.unitasker.models.Task
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

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
        val selectedDate = LocalDate.of(yearSelected, Month.of(monthSelected), daySelected)
        val deadlineTasks: List<Task> = Task.findByDeadLine(selectedDate)
        val assignmentTasks: List<Task> = Task.findByAssignmentDate(selectedDate)
        val context = this.context
        if (context === null || tasksView === null) {
            return
        }
        val deadlineCards = deadlineTasks.map { task ->
            val taskSubject = task.subject!!
            return@map TaskCard(
                context,
                TaskCardAttributes(
                    task.title, taskSubject.name, getString(R.string.final_day), Color.valueOf(taskSubject.color)
                )
            ) { onDeleteTask(task) }
        }
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        val assignmentCards = assignmentTasks.map { task ->
            val taskSubject = task.subject!!
            return@map TaskCard(
                context,
                TaskCardAttributes(
                    task.title, taskSubject.name,
                    "${getString(R.string.do_at)} ${task.assignmentStartDate?.format(timeFormatter)}-${task.assignmentEndDate?.format(timeFormatter)}", Color.valueOf(taskSubject.color)
                )
            ) { onDeleteTask(task) }
        }
        assignmentCards.forEach { task -> tasksView?.addView(task) }
        deadlineCards.forEach { task -> tasksView?.addView(task) }
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