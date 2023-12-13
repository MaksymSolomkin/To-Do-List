package com.customappsms.to_dolist.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.customappsms.to_dolist.databinding.ItemTaskLayoutBinding
import com.customappsms.to_dolist.models.Task

class ToDoListAdapter(
    private val onDeleteClicked: (Task) -> Unit,
    private val onItemClicked: (Task) -> Unit,
    private val onCheckBoxClicked: (Int, Task) -> Unit
) : ListAdapter<Task, ToDoListAdapter.ToDoListViewHolder>(TaskDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val binding = ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ToDoListViewHolder(private val binding: ItemTaskLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteImageView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClicked.invoke(getItem(adapterPosition))
                }
            }

            binding.itemLayout.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked.invoke(getItem(adapterPosition))
                }
            }

            binding.checkbox.setOnCheckedChangeListener { _, isSelected ->
                updateCompletedView(isSelected)
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onCheckBoxClicked.invoke(adapterPosition, getItem(adapterPosition))
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
}

object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}