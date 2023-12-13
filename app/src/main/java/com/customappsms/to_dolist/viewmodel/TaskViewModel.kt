package com.customappsms.to_dolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.customappsms.to_dolist.data.TaskRepository
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    private val _tasks = MutableLiveData<UIState<List<Task>>>()
    val tasks: LiveData<UIState<List<Task>>>
        get() = _tasks

    private val _updateTask = MutableLiveData<UIState<Int>>()
    val updateTask: LiveData<UIState<Int>>
        get() = _updateTask

    private val _deleteTask = MutableLiveData<UIState<Int>>()
    val deleteTask: LiveData<UIState<Int>>
        get() = _deleteTask

    fun getTasks() {
        _tasks.value = UIState.Loading
        repository.getTasks {
            _tasks.value = it
        }
    }

    fun updateTask(task: Task) {
        _updateTask.value = UIState.Loading
        repository.updateTask(task) {
            _updateTask.value = it
        }
    }

    fun deleteTask(task: Task) {
        _deleteTask.value = UIState.Loading
        repository.deleteTask(task) {
            _deleteTask.value = it
        }
    }
}