package com.example.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val finishedViewModel by viewModels<FinishedViewModel>()
    private lateinit var adapter: FinishedAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = FinishedAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }

        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            this.adapter = this@FinishedFragment.adapter
        }
        finishedViewModel.event.observe(viewLifecycleOwner) { events ->
            setEventData(events)
        }


        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        finishedViewModel.snackbarText.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun setEventData(listEvent: List<ListEventsItem>) {
        adapter.submitList(listEvent)
        binding.rvFinishedEvent.adapter = adapter

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}