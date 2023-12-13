package com.customappsms.to_dolist.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.customappsms.to_dolist.R
import com.customappsms.to_dolist.databinding.FragmentTaskDetailsBinding
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.utils.UIState
import com.customappsms.to_dolist.utils.hide
import com.customappsms.to_dolist.utils.hideKeyboard
import com.customappsms.to_dolist.utils.toast
import com.customappsms.to_dolist.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: TaskViewModel by viewModels()
    private val defaultErrorMessage: String
        get() {
            return getString(R.string.message_somethingWentWrong)
        }

    var dismissCallback: (() -> Unit)? = null
    var task: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        dismissCallback?.invoke()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateView()

        binding.editText.requestFocus()

        binding.confirmButton.setOnClickListener {
            if (validation()) {
                viewModel.updateTask(
                    Task(
                        id = task?.id ?: "",
                        title = binding.editText.text.toString()
                    )
                )
            }
        }

        viewModel.updateTask.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Loading -> {
                }
                is UIState.Failure -> {
                    toast(state.error ?: defaultErrorMessage)
                }
                is UIState.Success -> {
                    binding.editText.hideKeyboard()
                    toast(getString(state.data))
                    Handler(Looper.getMainLooper()).postDelayed({
                        dismiss()
                    }, 1000)
                }
            }
        }
    }

    private fun updateView() {
        binding.editText.setText(task?.title)
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editText.text.toString().isEmpty()) {
            isValid = false
            toast(getString(R.string.message_taskCanNotBeEmpty))
        }

        return isValid
    }
}