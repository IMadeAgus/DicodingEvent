package com.example.dicodingevent.ui.finished
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.remote.response.EventResponse
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _event = MutableLiveData<List<ListEventsItem>>()
    val event: LiveData<List<ListEventsItem>> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    companion object{
        private const val TAG = "FinishedViewModel"
        private const val PARAM_FINISHED_EVENT = 0 }

    init {
        showAllEvent()
    }


    private fun showAllEvent() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFinishedEvent(PARAM_FINISHED_EVENT,40)
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
}