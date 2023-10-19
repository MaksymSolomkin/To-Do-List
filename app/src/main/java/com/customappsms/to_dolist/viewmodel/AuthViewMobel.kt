package com.customappsms.to_dolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.customappsms.to_dolist.data.AuthRepository
import com.customappsms.to_dolist.utils.UIState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableLiveData<UIState<FirebaseUser>?>(null)
    val loginFlow: LiveData<UIState<FirebaseUser>?>
        get() = _loginFlow

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = UIState.Success(repository.currentUser!!)
        }
    }

    fun anonymousLogin() {
        _loginFlow.value = UIState.Loading
        repository.login {
            _loginFlow.value = it
        }
    }
}