package com.example.dicodingevent.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventBinding

class UpcomingAdapter(private val onItemClick: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, UpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
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