package com.example.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.eventapp.utils.Result
class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val viewModel: UpcomingViewModel by viewModels<UpcomingViewModel>{
        UpcomingViewModelFactory.getInstance(requireActivity())
    }
    private val binding get() = _binding!!
    private lateinit var verticalAdapter: UpcomingAdapter

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

        verticalAdapter = UpcomingAdapter() { events ->
            if (events.isFavorite == true) {
                viewModel.deleteEvents(events)
            } else {
                viewModel.saveEvents(events)
            }
        }
        verticalAdapter.setLoadingState(true)

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    verticalAdapter.setLoadingState(true)
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    verticalAdapter.setLoadingState(false)
                    verticalAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(context, "An error occurred" + result.error, Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        }

        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = verticalAdapter
        }

        setupSearchView()
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                val query = searchView.text.toString()
                searchBar.setText(searchView.text)
                searchView.hide()
                viewModel.searchUpcomingEvents(query).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            updateUI(isLoading = true, isEmpty = false)
                        }

                        is Result.Success -> {
                            verticalAdapter.submitList(result.data)
                            updateUI(isLoading = false, isEmpty = result.data.isEmpty())
                        }

                        is Result.Error -> {
                            updateUI(isLoading = false, isEmpty = true)
                        }
                    }
                }
                false
            }
        }
    }

    private fun updateUI(isLoading: Boolean, isEmpty: Boolean) {
        with(binding) {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            noDataFoundLottie.visibility = if (isEmpty && !isLoading) View.VISIBLE else View.GONE
            rvUpcomingEvent.visibility = if (!isEmpty && !isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}