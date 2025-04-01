package com.example.dicodingevent.ui.upcoming

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.data.local.entity.EventsEntity

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val viewModel: UpcomingViewModel by viewModels<UpcomingViewModel>{
        UpcomingViewModelFactory.getInstance(requireActivity())
    }
    private val binding get() = _binding!!

    private val adapter by lazy {
        UpcomingAdapter(
            onItemClick = { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
                }
                Log.d("DetailActivity", "eventId: $event.id")
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

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeData()
    }


    private fun setupSearch() {
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                performSearch(query)
                closeSearchBar()
                true
            } else {
                false
            }
        }
    }
    private fun performSearch(query: String) {
        viewModel.searchEvents(query)
        binding.searchView.hide()
        closeSearchBar()
    }

    private fun observeData() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<List<EventsEntity>>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                adapter.submitList(result.data)
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Terjadi kesalahan: ${result.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (result is Result.Success && result.data.isEmpty()) {
            Toast.makeText(context, "Event tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.adapter
        }
    }
    private fun closeSearchBar() {
        binding.searchView.clearFocus()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}