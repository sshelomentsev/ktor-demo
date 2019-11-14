package com.sshelomentsev

data class Task(

    val id: Long,
    val name: String,
    val description: String,
    val complexity: Int

) {

    constructor(id: Long, form: TaskForm): this(id, form.name, form.description, form.complexity)

}