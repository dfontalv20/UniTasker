package com.example.unitasker.models

import com.orm.SugarRecord


class Task : SugarRecord<Task> {
    var title: String = ""

    constructor()
    constructor(title: String) {
        this.title = title
    }
}