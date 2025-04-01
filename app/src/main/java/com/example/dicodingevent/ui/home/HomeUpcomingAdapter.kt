package com.example.dicodingevent.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.databinding.ItemHomeUpcomingEventBinding

class HomeUpcomingAdapter(
    private var isLoading: Boolean = true,
    private val onFavoriteClick: (EventEntity) -> Unit,
    private val onItemClick: (EventEntity) -> Unit,
) : ListAdapter<EventEntity, HomeUpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  MyViewHolder {
        val binding = ItemHomeUpcomingEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onFavoriteClick, onItemClick)
    }

    class MyViewHolder(private val binding: ItemHomeUpcomingEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            event: EventEntity,
            onFavoriteClick: (EventEntity) -> Unit,
            onItemClick: (EventEntity) -> Unit
        ) {
            binding.apply {
                tvTitle.text = event.name

                Glide.with(itemView.context)
                    .load(event.mediaCover)
                    .centerCrop()
                    .into(imgMediaCover)

                // Set favorite icon
                ivFavorite.setImageResource(
                    if (event.isFavorite) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                )

                // Handle clicks
                ivFavorite.setOnClickListener {
                    onFavoriteClick(event)
                }

                root.setOnClickListener {
                    onItemClick(event)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}