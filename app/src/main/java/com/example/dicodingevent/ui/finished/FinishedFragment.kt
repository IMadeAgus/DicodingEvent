package com.example.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.ui.upcoming.UpcomingAdapter
import com.example.dicodingevent.ui.upcoming.UpcomingViewModel
import com.example.dicodingevent.ui.upcoming.UpcomingViewModelFactory
import com.google.android.material.snackbar.Snackbar


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val viewModel: FinishedViewModel by viewModels<FinishedViewModel>{
        FinishedViewModelFactory.getInstance(requireActivity())
    }
    private val binding get() = _binding!!

    private val adapter by lazy {
        FinishedAdapter(
            onItemClick = { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
                }
                startActivity(intent)
            },
            onFavoriteClick = { event ->
                if(event.isFavorited) {
                    viewModel.deleteFavoritedEvent(event)
                } else {
                    viewModel.toggleFavorite(event)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FinishedFragment.adapter
        }
    }

    private fun observeData() {
        viewModel.finishedEvent.observe(viewLifecycleOwner, {result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val newsData = result.data
                        adapter.submitList(newsData)
                    }

                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}