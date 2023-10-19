package com.customappsms.to_dolist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.customappsms.to_dolist.databinding.ActivityEntryBinding
import com.customappsms.to_dolist.utils.UIState
import com.customappsms.to_dolist.utils.hide
import com.customappsms.to_dolist.utils.show
import com.customappsms.to_dolist.utils.toast
import com.customappsms.to_dolist.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntryBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loginFlow.observe(this) {
            when(it) {
                is UIState.Loading -> {
                    binding.progressBar.show()
                }
                is UIState.Failure -> {
                    binding.progressBar.hide()
                    toast(it.error)
                }
                is UIState.Success -> {
                    binding.progressBar.hide()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    viewModel.anonymousLogin()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.loginFlow.removeObservers(this)
    }
}