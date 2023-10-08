package com.customappsms.to_dolist.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.customappsms.to_dolist.databinding.ItemTaskLayoutBinding
import com.customappsms.to_dolist.models.Task
import com.customappsms.to_dolist.utils.hide
import com.customappsms.to_dolist.utils.show

class ToDoListAdapter(
    val onItemClicked: (Int, Task) -> Unit,
    val onCheckBoxClicked: (Int, Task) -> Unit,
    val onDeleteClicked: (Int, Task) -> Unit,
) : RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>() {

    private var list: MutableList<Task> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val itemView = ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Task>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemChanged(position)
    }

    inner class ToDoListViewHolder(private val binding: ItemTaskLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.titleTextView.text = item.title
            binding.checkbox.isSelected = item.isCompleted

            binding.deleteImageView.setOnClickListener { onDeleteClicked.invoke(adapterPosition, item) }
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition, item) }
            binding.checkbox.setOnCheckedChangeListener { _, isSelected ->
                updateCompletedView(isSelected)
                onCheckBoxClicked(adapterPosition, item)
            }
        }

        private fun updateCompletedView(isSelected: Boolean) {
            if (isSelected) {
                binding.crossLineView.show()
            } else {
                binding.crossLineView.hide()
            }
        }
    }
}