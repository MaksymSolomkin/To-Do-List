package com.customappsms.to_dolist.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val id: String = "",
    val title: String = "",
    val date: Timestamp = Timestamp.now(),
    val isCompleted: Boolean = false,
    var userId: String = ""
)
