package com.customappsms.to_dolist.data

import com.customappsms.to_dolist.utils.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository  @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser: FirebaseUser?
        get ()  = firebaseAuth.currentUser

    fun login(result: (UIState<FirebaseUser>) -> Unit) {
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                result.invoke(
                    UIState.Success(it.user!!)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UIState.Failure(it.localizedMessage)
                )
            }
    }
}