package com.example.unitasker.models

import com.orm.SugarRecord
import com.orm.dsl.Ignore
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AvailabilitySchedule : SugarRecord<AvailabilitySchedule> {
    var day: Int = 0

    // SugarORM doesn't support LocalTime, so we use a string instead
    private var startTimeString: String = "00:00"
    private var endTimeString: String = "00:00"

    @field:Ignore val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    @field:Ignore var startTime: LocalTime = LocalTime.now()
        get() = LocalTime.parse(startTimeString, timeFormatter)
        set(value) {
            field = value
            this.startTimeString = value.format(timeFormatter)
        }

    @field:Ignore var endTime: LocalTime = LocalTime.now()
        get() = LocalTime.parse(endTimeString, timeFormatter)
        set(value) {
            field = value
            this.endTimeString = value.format(timeFormatter)
        }

    private constructor()
    constructor(day: Int, startTime: LocalTime, endTime: LocalTime) {
        this.day = day
        this.startTime = startTime
        this.endTime = endTime
    }

    companion object {
        fun findByDay(day: Int): List<AvailabilitySchedule> {
            return find(AvailabilitySchedule::class.java, "day = ?", day.toString())
        }

        fun findScheduleAt(date: LocalDateTime): AvailabilitySchedule? {
            val daySchedules = findByDay( date.dayOfWeek.value)
            return daySchedules.find { schedule ->
                schedule.startTime.isBefore(date.toLocalTime()) && schedule.endTime.isAfter(date.toLocalTime())
            }
        }

        fun availableSchedule(startDate: LocalDateTime, limitDate: LocalDateTime, duration: Int): LocalDateTime? {
            var currentDate = startDate
            while (currentDate.isBefore(limitDate)) {
                val schedule = findScheduleAt(currentDate)
                if (schedule === null) {
                    currentDate = currentDate.plusHours(duration.toLong())
                    continue
                }
                val task = Task.findByAssignmentDate(currentDate)
                if (task.isEmpty()) {
                    return currentDate
                }
                currentDate = startDate.plusHours(duration.toLong())
            }
            return null
        }
    }
}