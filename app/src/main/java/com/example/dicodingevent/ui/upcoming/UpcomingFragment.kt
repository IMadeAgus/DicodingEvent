package com.example.dicodingevent.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar


class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val upcomingViewModel by viewModels<UpcomingViewModel>()
    private lateinit var adapter: UpcomingAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar = binding.searchBar
        val searchView = binding.searchView

        searchView.setupWithSearchBar(searchBar)
        searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                upcomingViewModel.searchEvents(query)
                searchView.hide()
                searchBar.setText(query)
                return@setOnEditorActionListener true
            }
            false
        }

        adapter = UpcomingAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }

        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            this.adapter = this@UpcomingFragment.adapter
        }

        upcomingViewModel.event.observe(viewLifecycleOwner) { events ->
            setEventData(events)
        }


        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        upcomingViewModel.snackbarText.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setEventData(listEvent: List<ListEventsItem>) {
        adapter.submitList(listEvent)
        binding.rvUpcomingEvent.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}