package com.customappsms.to_dolist.data

import com.customappsms.to_dolist.R
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.utils.FireStoreTables
import com.customappsms.to_dolist.utils.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    private val currentUser: FirebaseUser?
        get ()  = firebaseAuth.currentUser

    fun getTasks(result: (UIState<List<Task>>) -> Unit) {
        database.collection(FireStoreTables.TASKS)
            .whereEqualTo("userId", currentUser?.uid)
            .get()
            .addOnSuccessListener {
                val tasks = arrayListOf<Task>()
                for (document in it) {
                    val task = document.toObject(Task::class.java)
                    tasks.add(task)
                }

                result.invoke(
                    UIState.Success(tasks)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UIState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    fun updateTask(task: Task, result: (UIState<Int>) -> Unit) {
        task.userId = currentUser?.uid ?: "unknown"

        val isNewTask = task.id.isEmpty()
        val document = if (isNewTask)
            database.collection(FireStoreTables.TASKS).document()
        else
            database.collection(FireStoreTables.TASKS).document(task.id)

        document.set(task)
            .addOnSuccessListener {
                result.invoke(
                    UIState.Success(
                        if (isNewTask) R.string.message_taskWasSuccessfullyAdded
                        else R.string.message_taskWasSuccessfullyUpdated
                    )
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UIState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    fun deleteTask(task: Task, result: (UIState<Int>) -> Unit) {
        val document = database.collection(FireStoreTables.TASKS).document(task.id)
        document.delete()
            .addOnSuccessListener {
                result.invoke(
                    UIState.Success(R.string.message_taskWasSuccessfullyDeleted)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UIState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
}