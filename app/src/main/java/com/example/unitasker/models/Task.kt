package com.example.unitasker.models

import com.orm.SugarRecord
import com.orm.dsl.Ignore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Task : SugarRecord<Task> {
    var title: String = ""
    var subject: Subject? = null
    var endDate: LocalDate? = null
    var duration: Int? = null
    private var assignmentStartDateString: String? = null
    private var assignmentEndDateString: String? = null
    @field:Ignore var assignmentStartDate: LocalDateTime? = null
        get() = LocalDateTime.parse(assignmentStartDateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        set(value) {
            field = value
            assignmentStartDateString = value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    @field:Ignore var assignmentEndDate: LocalDateTime? = null
        get() = LocalDateTime.parse(assignmentEndDateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        set(value) {
            field = value
            assignmentEndDateString = value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
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
        fun findByDeadLine(date: LocalDate): List<Task> {
            return find(
                Task::class.java,
                "end_date = ?",
                date.toString()
            )
        }

        fun findByAssignmentDate(date: LocalDate): List<Task> {
            return find(
                Task::class.java,
                "DATE(assignment_start_date_string) = ?",
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