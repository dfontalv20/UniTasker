package com.example.unitasker

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import com.example.unitasker.models.Task
import com.orm.SugarRecord

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tasks = loadTasks()
        val view = inflater.inflate(R.layout.calendar, container, false)
        val tasksView = view.findViewById<LinearLayout>(R.id.tasks_linear_layout)
        tasks.forEach { task -> tasksView.addView(task) }
        return view
    }

    private fun loadTasks(): List<TaskCard> {
        val tasks: List<Task> = SugarRecord.listAll(Task::class.java)
        val context = this.context
        if (context === null) return listOf()
        return tasks.map { TaskCard(context, it) }
    }
}