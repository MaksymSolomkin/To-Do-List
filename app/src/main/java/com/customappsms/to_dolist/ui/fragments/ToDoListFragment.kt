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
import com.customappsms.to_dolist.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ToDoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTasks()
        viewModel.task.observe(viewLifecycleOwner) {
            // Handle the observed task here
        }

        updateView()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        val hour = LocalDateTime.now().hour

        val title = when (true) {
            (hour in 0..12) -> getString(R.string.title_goodMorning)
            (hour in 12..17) -> getString(R.string.title_goodAfternoon)
            else -> getString(R.string.title_goodEvening)
        }

        binding.welcomeTextView.text = title + getString(R.string.wave_hand).parseAsHtml()
    }
}