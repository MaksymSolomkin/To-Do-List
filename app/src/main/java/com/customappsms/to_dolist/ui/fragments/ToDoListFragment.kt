package com.customappsms.to_dolist.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.customappsms.to_dolist.R
import com.customappsms.to_dolist.databinding.FragmentTodoListBinding
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.ui.adapters.ToDoListAdapter
import com.customappsms.to_dolist.utils.UIState
import com.customappsms.to_dolist.utils.hide
import com.customappsms.to_dolist.utils.show
import com.customappsms.to_dolist.utils.toast
import com.customappsms.to_dolist.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ToDoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TaskViewModel by viewModels()
    private val defaultErrorMessage: String
        get() {
            return getString(R.string.message_somethingWentWrong)
        }
    private val adapter by lazy {
        ToDoListAdapter(
            onItemClicked = { item ->
                openBottomSheet(item)
            },
            onDeleteClicked = { item ->
                viewModel.deleteTask(item)
            },
            onCheckBoxClicked = { pos, item ->

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getTasks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.addNoteImageView.setOnClickListener {
            openBottomSheet()
        }

        viewModel.tasks.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Loading -> {
                    binding.progressBar.show()
                }
                is UIState.Failure -> {
                    showError(state.error ?: defaultErrorMessage)
                }
                is UIState.Success -> {
                    showSuccess(state.data)
                }
            }
        }

        viewModel.deleteTask.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Loading -> {
                    binding.progressBar.show()
                }
                is UIState.Failure -> {
                    showError(state.error ?: defaultErrorMessage)
                }
                is UIState.Success -> {
                    viewModel.getTasks()
                    toast(getString(state.data))
                }
            }
        }

        updateView()
    }

    private fun openBottomSheet(task: Task? = null) {
        val bottomSheet = TaskDetailsBottomSheetFragment()
        bottomSheet.task = task
        bottomSheet.dismissCallback = {
            viewModel.getTasks()
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showError(error: String) {
        binding.progressBar.hide()
        toast(error)
    }

    private fun showSuccess(data: List<Task>) {
        binding.progressBar.hide()
        adapter.submitList(data)
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        val hour = LocalDateTime.now().hour

        val title = when (true) {
            (hour in 0..11) -> getString(R.string.title_goodMorning)
            (hour in 12..16) -> getString(R.string.title_goodAfternoon)
            else -> getString(R.string.title_goodEvening)
        }

        binding.welcomeTextView.text = "$title ${getString(R.string.wave_hand).parseAsHtml()}"
    }
}