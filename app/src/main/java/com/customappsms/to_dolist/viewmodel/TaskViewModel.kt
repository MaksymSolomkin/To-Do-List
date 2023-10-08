package com.customappsms.to_dolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.customappsms.to_dolist.data.TaskRepository
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.utils.UIState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    private val _tasks = MutableLiveData<UIState<List<Task>>>()
    val tasks: LiveData<UIState<List<Task>>>
        get() = _tasks

    private val _addTask = MutableLiveData<UIState<String>>()
    val addTask: LiveData<UIState<String>>
        get() = _addTask

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun getTasks() {
        _tasks.value = UIState.Loading
        repository.getTasks {
            _tasks.value = it
        }
    }

    fun addTask(task: Task) {
        _addTask.value = UIState.Loading
        repository.addTask(task) {
            _addTask.value = it
        }
    }
}