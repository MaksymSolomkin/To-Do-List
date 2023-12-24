package com.customappsms.to_dolist.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.customappsms.to_dolist.R
import com.customappsms.to_dolist.databinding.ItemHeaderLayoutBinding
import com.customappsms.to_dolist.databinding.ItemTaskLayoutBinding
import com.customappsms.to_dolist.models.Task

sealed class ListItem {
    data class TaskItem(val task: Task) : ListItem()
    data class HeaderItem(val headerText: String) : ListItem()
}

class ToDoListAdapter(
    private val onDeleteClicked: (Task) -> Unit,
    private val onItemClicked: (Task) -> Unit,
    private val onCheckBoxClicked: (Int, Task) -> Unit
) : ListAdapter<ListItem, RecyclerView.ViewHolder>(ListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_task_layout -> {
                val binding = ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TaskViewHolder(binding)
            }
            R.layout.item_header_layout -> {
                val headerBinding = ItemHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(headerBinding)
            }
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.TaskItem -> (holder as TaskViewHolder).bind(item.task)
            is ListItem.HeaderItem -> (holder as HeaderViewHolder).bind(item.headerText)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.TaskItem -> R.layout.item_task_layout
            is ListItem.HeaderItem -> R.layout.item_header_layout
        }
    }

    inner class TaskViewHolder(private val binding: ItemTaskLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteImageView.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    when (val listItem = getItem(absoluteAdapterPosition)) {
                        is ListItem.TaskItem -> onDeleteClicked.invoke(listItem.task)
                        is ListItem.HeaderItem -> {}
                    }
                }
            }

            binding.itemLayout.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    when (val listItem = getItem(absoluteAdapterPosition)) {
                        is ListItem.TaskItem -> onItemClicked.invoke(listItem.task)
                        is ListItem.HeaderItem -> {}
                    }
                }
            }

            binding.checkbox.setOnCheckedChangeListener { _, isSelected ->
                updateCompletedView(isSelected)
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    when (val listItem = getItem(absoluteAdapterPosition)) {
                        is ListItem.TaskItem -> onCheckBoxClicked.invoke(absoluteAdapterPosition, listItem.task)
                        is ListItem.HeaderItem -> {}
                    }
                }
            }
        }

        fun bind(item: Task) {
            binding.titleTextView.text = item.title
            binding.checkbox.isSelected = item.isCompleted
            updateCompletedView(item.isCompleted)
        }

        private fun updateCompletedView(isSelected: Boolean) {
            binding.crossLineView.visibility = if (isSelected) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(headerText: String) {
            // Binding logic for header items
        }
    }
}

object ListDiffCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is ListItem.TaskItem && newItem is ListItem.TaskItem -> {
                oldItem.task.id == newItem.task.id
            }

            oldItem is ListItem.HeaderItem && newItem is ListItem.HeaderItem -> {
                oldItem.headerText == newItem.headerText
            }

            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is ListItem.TaskItem && newItem is ListItem.TaskItem -> {
                oldItem.task.id == newItem.task.id &&
                        oldItem.task.title == newItem.task.title &&
                        oldItem.task.isCompleted == newItem.task.isCompleted
            }

            oldItem is ListItem.HeaderItem && newItem is ListItem.HeaderItem -> {
                oldItem.headerText == newItem.headerText
            }

            else -> oldItem == newItem
        }
    }
}