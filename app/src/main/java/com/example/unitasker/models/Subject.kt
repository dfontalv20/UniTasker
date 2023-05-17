package com.example.unitasker.models

import android.graphics.Color
import com.orm.SugarRecord

class Subject : SugarRecord<Subject> {
    var name: String = ""
    var color: Int = Color.RED

    private constructor()
    constructor(name: String, color: Color) {
        this.name = name
        this.color = color.toArgb()
    }

    override fun delete() {
        val subjectTasks = find(Task::class.java, "subject = ?", this.id.toString())
        subjectTasks.forEach{ task -> task.delete() }
        super.delete()
    }
}