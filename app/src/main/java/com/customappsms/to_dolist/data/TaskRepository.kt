package com.customappsms.to_dolist.data

import com.customappsms.to_dolist.models.Task
import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository(
    val database: FirebaseFirestore
) {
    fun getTasks(): List<Task> {
        return emptyList()
    }
}