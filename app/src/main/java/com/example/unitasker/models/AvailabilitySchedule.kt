package com.example.unitasker.models

import com.orm.SugarRecord
import com.orm.dsl.Ignore
import java.lang.Error
import java.time.LocalDate
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
    }
}