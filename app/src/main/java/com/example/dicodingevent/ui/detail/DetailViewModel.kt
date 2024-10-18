package com.example.dicodingevent.ui.detail
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.ResponseDetail
import com.example.dicodingevent.data.retrofit.ApiConfig

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _eventDetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<com.example.dicodingevent.util.Event<String>>()
    val snackbarText: LiveData<com.example.dicodingevent.util.Event<String>> = _snackbarText

    companion object{
        private const val TAG = "FinishedViewModel"
    }

    fun getEventDetail(eventId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(eventId)
        client.enqueue(object : Callback<ResponseDetail> {
            override fun onResponse(
                call: Call<ResponseDetail>,
                response: Response<ResponseDetail>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()?.event
                } else {
                    _snackbarText.value = com.example.dicodingevent.util.Event("Error: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = com.example.dicodingevent.util.Event("Network error: ${t.message}")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}