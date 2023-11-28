package com.customappsms.to_dolist.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.customappsms.to_dolist.R
import com.customappsms.to_dolist.databinding.FragmentTodoListBinding
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
    private val adapter by lazy {
        ToDoListAdapter(
            onItemClicked = { pos, item ->

            },
            onDeleteClicked = { pos, item ->

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
            val bottomSheet = TaskDetailsBottomSheetFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        viewModel.tasks.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Loading -> {
                    binding.progressBar.show()
                }
                is UIState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UIState.Success -> {
                    binding.progressBar.hide()
                    adapter.submitList(state.data)
                }
            }
        }

        updateView()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        val hour = LocalDateTime.now().hour

        val title = when (true) {
            (hour in 0..11) -> getString(R.string.title_goodMorning)
            (hour in 12..16) -> getString(R.string.title_goodAfternoon)
            else -> getString(R.string.title_goodEvening)
        }

        binding.welcomeTextView.text = title + getString(R.string.wave_hand).parseAsHtml()
    }
}