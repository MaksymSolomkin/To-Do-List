package com.customappsms.to_dolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.customappsms.to_dolist.data.TaskRepository
import com.customappsms.to_dolist.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val task: LiveData<List<Task>>
        get() = _tasks

    fun getTasks() {
        _tasks.value = repository.getTasks()
    }
}