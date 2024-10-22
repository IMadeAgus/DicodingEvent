package com.example.dicodingevent.ui.finished

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventBinding

class FinishedAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, FinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,onItemClick)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val events = getItem(position)
        holder.bind(events)
    }

    class MyViewHolder(
        private val binding: ItemEventBinding,
        private val onItemClick: (ListEventsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvTitle.text = event.name
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .centerCrop()
                .into(binding.imgMediaCover)

            itemView.setOnClickListener { onItemClick(event) }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}