package com.customappsms.to_dolist.data

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
    val currentUser: FirebaseUser?
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
                println("MAX__ data - ${it.documents}")

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

    fun addTask(task: Task, result: (UIState<String>) -> Unit) {
        task.userId = currentUser!!.uid

        database.collection(FireStoreTables.TASKS)
            .add(task)
            .addOnSuccessListener {
                result.invoke(
                    UIState.Success(it.id)
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