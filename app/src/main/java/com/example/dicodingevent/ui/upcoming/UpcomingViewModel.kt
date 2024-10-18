package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import com.example.dicodingevent.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _event = MutableLiveData<List<ListEventsItem>>()
    val event: LiveData<List<ListEventsItem>> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _searchQuery = MutableLiveData<String>()

    companion object{
        private const val TAG = "UpcomingViewModel"
        private const val PARAM_UPCOMING_EVENT = 1 }

    init {
        showAllEvent()
    }


    private fun showAllEvent() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUpcomingEvent(PARAM_UPCOMING_EVENT,40)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false

                if(response.isSuccessful) {
                    _event.value = response.body()?.listEvents
                } else {
                    _snackbarText.value = Event("Error: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Network error: ${t.message}")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun searchEvents(query: String) {
        _searchQuery.value = query
        _isLoading.value = true

        val client = ApiConfig.getApiService().searchEvent(1, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _event.value = response.body()?.listEvents
                } else {
                    _snackbarText.value = Event("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Network error: ${t.message}")
            }
        })
    }
}