package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.EventsEntity
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.data.Result

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        if (eventId != -1) {
            viewModel.getEventDetail(eventId)
        } else {
            Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventDetail.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showEventDetail(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showError(result.error)
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE}
            }
        }

    }


    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEventDetail(event: EventsEntity) {
        binding.apply {
            tvDetailEventTitle.text = event.name
            tvOwnerName.text = event.ownerName
            tvQuota.text = (event.quota - event.registrants).toString()
            tvTimeStart.text = event.beginTime
            tvTimeEnd.text = event.endTime

            Glide.with(this@DetailActivity)
                .load(event.mediaCover)
                .placeholder(R.drawable.ic_error_image)
                .error(R.drawable.ic_error_image)
                .into(imgEventDetail)

            tvEventDescription.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            btnEventCTA.setOnClickListener {
                openEventLink(event.link)
            }

            updateFavoriteIcon(event.isFavorited)
            ivFavorite.setOnClickListener {
                toggleFavoriteStatus(event)
            }
        }
    }

    private fun openEventLink(link: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        } catch (e: Exception) {
            showError("Couldn't open the event link")
        }
    }

    private fun toggleFavoriteStatus(event: EventsEntity) {
        if (event.isFavorited) {
            viewModel.deleteFavoritedEvent(event)
        } else {
            viewModel.toggleFavorite(event)
        }
        updateFavoriteIcon(!event.isFavorited)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.ivFavorite.setImageResource(
            if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        )
    }

    companion object {
        const val EXTRA_EVENT_ID = "event_id"
    }
}
