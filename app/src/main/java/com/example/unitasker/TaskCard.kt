package com.example.unitasker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView

class TaskCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) :
    CardView(context, attrs) {
    init {
        this.cardElevation = 0F
        LayoutInflater.from(context)
            .inflate(R.layout.task_card, this, true)
    }
}
