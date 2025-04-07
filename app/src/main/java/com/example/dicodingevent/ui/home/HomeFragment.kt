package com.example.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.eventapp.utils.Result


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>{
        HomeViewModelFactory.getInstance(requireActivity())
    }
    private val binding get() = _binding!!
    private lateinit var verticalAdapter: HomeFinishedAdapter
    private lateinit var horizontalAdapter: HomeUpcomingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        horizontalAdapter = HomeUpcomingAdapter {
            if (it.isFavorite == true) {
                viewModel.deleteEvents(it)
            } else {
                viewModel.saveEvents(it)
            }
        }
        verticalAdapter = HomeFinishedAdapter {
            if (it.isFavorite == true) {
                viewModel.deleteEvents(it)
            } else {
                viewModel.saveEvents(it)
            }
        }

        horizontalAdapter.setLoadingState(true)
        verticalAdapter.setLoadingState(true)

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarUpcoming.visibility = View.VISIBLE
                    horizontalAdapter.setLoadingState(true)
                }

                is Result.Success -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    horizontalAdapter.setLoadingState(false)
                    horizontalAdapter.submitList(result.data.take(5))
                }

                is Result.Error -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                }
            }
        }

        viewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarUpcoming.visibility = View.VISIBLE
                    horizontalAdapter.setLoadingState(true)
                }

                is Result.Success -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                    horizontalAdapter.setLoadingState(false)
                    horizontalAdapter.submitList(result.data.take(5))
                }

                is Result.Error -> {
                    binding.progressBarUpcoming.visibility = View.GONE
                }
            }
        }



        binding.rvHomeUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalAdapter
        }

        binding.rvHomeFinishedEvent.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = verticalAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}