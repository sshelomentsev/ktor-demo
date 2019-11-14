package com.sshelomentsev

import java.util.concurrent.atomic.AtomicLong

object SimpleInMemoryDB {

    private val tasks: MutableMap<Long, Task> = HashMap()

    private val counter: AtomicLong = AtomicLong()

    fun getTasks() = tasks.values

    fun getTask(id: Long) = tasks[id]

    fun addTask(form: TaskForm) {
        val id = counter.incrementAndGet()
        val task = Task(id, form)
        tasks[id] = task
    }

    fun updateTask(id: Long, form: TaskForm): Boolean {
        val oldTask = tasks[id] ?: return false
        val newTask = Task(id, form)
        tasks[id] = newTask
        return true
    }

    fun deleteTask(id: Long): Boolean {
        val task = tasks.remove(id) ?: return false
        return true
    }

}