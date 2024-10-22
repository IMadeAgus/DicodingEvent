package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        viewModel.getEventDetail(eventId.toString())

        viewModel.eventDetail.observe(this) { eventDetail ->
            showEventDetail(eventDetail)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.snackbarText.observe(this) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEventDetail(event: Event) {
        val remainQuota = event.quota-event.registrants
        binding.apply {
            tvDetailEventTitle.text = event.name
            tvOwnerName.text = event.ownerName
            tvQuota.text =  remainQuota.toString()
            tvTimeStart.text = event.beginTime
            tvTimeEnd.text = event.endTime
            Glide.with(this@DetailActivity)
                .load(event.mediaCover)
                .into(imgEventDetail)
            tvEventDescription.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY)

            btnEventCTA.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(intent)
            }
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "event_id"
    }
}