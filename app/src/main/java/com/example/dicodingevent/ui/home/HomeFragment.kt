package com.example.dicodingevent.ui.home

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
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.detail.DetailActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>{
        HomeViewModelFactory.getInstance(requireActivity())
    }
    private val binding get() = _binding!!

    private val upcomingadapter by lazy {
        HomeUpcomingAdapter(
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

    private val finishedAdapter by lazy {
        HomeFinishedAdapter(
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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUpcomingRecyclerView()
        observeDataUpcoming()
        setupFinishedRecyclerView()
        observeDataFinished()

    }

    private fun setupUpcomingRecyclerView() {
        binding.rvHomeUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.upcomingadapter
        }
    }
    private fun observeDataUpcoming() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarUpcoming.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBarUpcoming.visibility = View.GONE
                        val newsData = result.data
                        upcomingadapter.submitList(newsData)
                    }

                    is Result.Error -> {
                        binding.progressBarUpcoming.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupFinishedRecyclerView() {
        binding.rvHomeFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.finishedAdapter
            binding.progressBarFinished.visibility = View.GONE
        }
    }

    private fun observeDataFinished() {
        viewModel.finishedEvent.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarFinished.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBarFinished.visibility = View.GONE
                        val newsData = result.data
                        finishedAdapter.submitList(newsData)
                    }

                    is Result.Error -> {
                        binding.progressBarFinished.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}