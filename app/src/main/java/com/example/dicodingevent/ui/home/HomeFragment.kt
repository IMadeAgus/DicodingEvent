package com.example.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
//    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var homeUpcomingAdapter: HomeUpcomingAdapter
    private lateinit var homeFinishedAdapter: HomeFinishedAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeUpcomingAdapter = HomeUpcomingAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }
        homeFinishedAdapter = HomeFinishedAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }

        binding.rvHomeUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = this@HomeFragment.homeUpcomingAdapter
        }
        binding.rvHomeFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            this.adapter = this@HomeFragment.homeFinishedAdapter
        }
//        homeViewModel.upcomingEvent.observe(viewLifecycleOwner) { upcomingEvents ->
//            setUpcomingEventData(upcomingEvents)
//        }
//        homeViewModel.finishedEvent.observe(viewLifecycleOwner) { finshedEvents ->
//            setFinishedEventData(finshedEvents)
//        }
//
//        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
//
//        homeViewModel.snackbarText.observe(viewLifecycleOwner) { event ->
//            event.getContentIfNotHandled()?.let { message ->
//                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun setFinishedEventData(listEvent: List<ListEventsItem>) {
        homeFinishedAdapter.submitList(listEvent)
        binding.rvHomeFinishedEvent.adapter = homeFinishedAdapter
    }

    private fun setUpcomingEventData(listEvent: List<ListEventsItem>) {
        homeUpcomingAdapter.submitList(listEvent)
        binding.rvHomeUpcomingEvent.adapter = homeUpcomingAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}