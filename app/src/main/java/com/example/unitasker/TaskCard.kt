package com.example.unitasker

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import androidx.core.view.marginTop
import com.example.unitasker.models.Task

data class TaskCardAttributes(val title: String, val subtitle: String, val timeText: String, val taskColor: Color)

class TaskCard @JvmOverloads constructor(
    context: Context, attributes: TaskCardAttributes, deleteCallBack: ()->Unit
) :
    CardView(context, null) {
    init {
        this.cardElevation = 0F
        val card = LayoutInflater.from(context)
            .inflate(R.layout.task_card, this, true)

        val title = card.findViewById<TextView>(R.id.task_card_title)
        val subject = card.findViewById<TextView>(R.id.task_card_subject)
        val schedule = card.findViewById<TextView>(R.id.task_card_schedule)
        val container = card.findViewById<CardView>(R.id.task_card_container)
        val timeIcon = card.findViewById<ImageView>(R.id.task_card_time_icon)
        val deleteIcon = card.findViewById<ImageView>(R.id.tasK_card_delete_icon)

        title.text = attributes.title
        subject.text = attributes.subtitle
        schedule.text = attributes.timeText

        val containerColor = ColorUtils.setAlphaComponent(attributes.taskColor.toArgb(), 55)

        subject.setTextColor(attributes.taskColor.toArgb())
        container.setCardBackgroundColor(containerColor)
        timeIcon.setColorFilter(attributes.taskColor.toArgb())

        deleteIcon.setOnClickListener { deleteCallBack() }
    }
}
