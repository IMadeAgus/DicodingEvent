package com.example.dicodingevent.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.EventsEntity
import com.example.dicodingevent.databinding.ItemEventBinding

class FavoriteAdapter (
    private val onFavoriteClick: (EventsEntity) -> Unit,
    private val onItemClick: (EventsEntity) -> Unit,
) : ListAdapter<EventsEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onFavoriteClick, onItemClick)
    }

    class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            event: EventsEntity,
            onFavoriteClick: (EventsEntity) -> Unit,
            onItemClick: (EventsEntity) -> Unit
        ) {
            binding.apply {
                tvTitle.text = event.name

                Glide.with(itemView.context)
                    .load(event.mediaCover)
                    .centerCrop()
                    .into(imgMediaCover)

                // Set favorite icon
                ivFavorite.setImageResource(
                    if (event.isFavorited) R.drawable.baseline_favorite_24
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventsEntity>() {
            override fun areItemsTheSame(
                oldItem: EventsEntity,
                newItem: EventsEntity
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: EventsEntity,
                newItem: EventsEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}