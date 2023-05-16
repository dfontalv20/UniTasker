package com.example.unitasker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import com.example.unitasker.models.Task

class TaskCard @JvmOverloads constructor(
    context: Context, task: Task
) :
    CardView(context, null) {
    init {
        this.cardElevation = 0F
        val card = LayoutInflater.from(context)
            .inflate(R.layout.task_card, this, true)
        val title = card.findViewById<TextView>(R.id.task_card_title)
        title.text = task.title
    }
}
