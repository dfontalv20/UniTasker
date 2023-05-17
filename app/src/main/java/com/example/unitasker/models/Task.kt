package com.example.unitasker.models

import com.orm.SugarRecord
import java.time.LocalDate
import java.util.*


class Task : SugarRecord<Task> {
    var title: String = ""
    var subject: Subject? = null
    var endDate: LocalDate? = null
    var duration: Int? = null
    var notificationEnabled: Boolean = false

    constructor()
    constructor(title: String, subject: Subject, endDate: LocalDate, duration: Int, notification: Boolean) {
        this.title = title
        this.subject = subject
        this.endDate = endDate
        this.duration = duration
        this.notificationEnabled = notification
    }

    companion object {
        fun findByDate(date: LocalDate): List<Task> {
            return find(
                Task::class.java,
                "end_date = ?",
                date.toString()
            )
        }

        fun findByName(name: String): Task? {
            val tasks = find(
                Task::class.java,
                "title = ?",
                name
            )
            return if (tasks.size == 0) null else tasks[0]
        }
    }
}