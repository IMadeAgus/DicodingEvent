package com.example.dicodingevent.ui.upcoming

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.databinding.ItemNormalVerticalBinding
import com.example.dicodingevent.databinding.ItemShimmerVerticalBinding
import com.example.dicodingevent.ui.detail.DetailActivity

class UpcomingAdapter(
    private var isLoading: Boolean = true,
    private val onFavoriteClick: (EventEntity) -> Unit,
) : ListAdapter<EventEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerVerticalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemNormalVerticalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder && !isLoading) {
            val event = getItem(position)
            holder.bind(event)
            val favoriteImageView = holder.binding.favoriteImageView
            favoriteImageView.setImageResource(
                if (event.isFavorite == true) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
            favoriteImageView.setOnClickListener {
                onFavoriteClick(event)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }


    override fun getItemCount(): Int {
        return if (isLoading) 5 else super.getItemCount()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    class ShimmerViewHolder(binding: ItemShimmerVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MyViewHolder(val binding: ItemNormalVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.titleTextView.text = event.name
            binding.ownerTextView.text = event.ownerName
            binding.chip.text = "${event.category}"
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .transform(RoundedCorners(16))
                .into(binding.imageView)
            itemView.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java)
                intent.putExtra("EXTRA_EVENT", event)
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
