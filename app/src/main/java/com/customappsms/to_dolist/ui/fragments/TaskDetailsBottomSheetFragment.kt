package com.customappsms.to_dolist.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.customappsms.to_dolist.R
import com.customappsms.to_dolist.databinding.FragmentTaskDetailsBinding
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.utils.UIState
import com.customappsms.to_dolist.utils.hideKeyboard
import com.customappsms.to_dolist.utils.toast
import com.customappsms.to_dolist.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import com.google.firebase.Timestamp
import java.util.Date

@AndroidEntryPoint
class TaskDetailsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: TaskViewModel by viewModels()
    private val defaultErrorMessage: String
        get() {
            return getString(R.string.message_somethingWentWrong)
        }
    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
    private val currentLocalDate = LocalDate.now()
    private var selectedDate: Date? = null

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
        binding.dateViewPicker.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext())
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.datePicker.maxDate = calculateOneMonthForward()
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                handleDate("$dayOfMonth.${month + 1}.$year")
            }
            datePicker.show()
        }

        binding.confirmButton.setOnClickListener {
            if (validation()) {
                viewModel.updateTask(
                    Task(
                        id = task?.id ?: "",
                        title = binding.editText.text.toString(),
                        date = Timestamp(selectedDate ?: Date())
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
        task?.let {
            binding.editText.setText(it.title)

            selectedDate = it.date.toDate()
            handleDate(dateFormatter.format(selectedDate!!))
        }
    }

    private fun handleDate(date: String) {
        selectedDate = dateFormatter.parse(date)

        val parsedDate = selectedDate?.toInstant()?.atZone(ZoneId.systemDefault())
            ?.toLocalDate()

        val calendarTextViewTitle = when (parsedDate) {
            currentLocalDate -> { getString(R.string.button_title_today) }
            currentLocalDate.plusDays(1) -> { getString(R.string.button_title_tomorrow) }
            currentLocalDate.minusDays(1) -> { getString(R.string.button_title_yesterday) }
            else -> { date }
        }
        binding.calendarTextView.text = calendarTextViewTitle
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editText.text.toString().isEmpty()) {
            isValid = false
            toast(getString(R.string.message_taskCanNotBeEmpty))
        }

        return isValid
    }

    private fun calculateOneMonthForward(): Long {
        val currentDate = currentLocalDate
        currentDate.plusMonths(1)

        return currentDate.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}