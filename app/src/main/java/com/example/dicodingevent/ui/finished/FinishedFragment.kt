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
    private lateinit var verticalAdapter: FinishedAdapter

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
        verticalAdapter = FinishedAdapter() { events ->
            if (events.isFavorite == true) {
                viewModel.deleteEvents(events)
            } else {
                viewModel.saveEvents(events)
            }
        }
        verticalAdapter.setLoadingState(true)

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.eventapp.utils.Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    verticalAdapter.setLoadingState(true)
                }

                is com.example.eventapp.utils.Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    verticalAdapter.setLoadingState(false)
                    verticalAdapter.submitList(result.data)
                }

                is com.example.eventapp.utils.Result.Error -> {
                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(context, "An error occurred" + result.error, Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        }

        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = verticalAdapter
        }

        setupSearchView()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}