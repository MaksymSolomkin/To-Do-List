package com.customappsms.to_dolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.customappsms.to_dolist.databinding.ActivityMainBinding
import com.customappsms.to_dolist.utils.UIState
import com.customappsms.to_dolist.utils.hide
import com.customappsms.to_dolist.utils.show
import com.customappsms.to_dolist.utils.toast
import com.customappsms.to_dolist.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}