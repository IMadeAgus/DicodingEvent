package com.example.dicodingevent.ui.finished

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventsRepository
import com.example.dicodingevent.di.Injection

class FinishedViewModelFactory private constructor(
    private val eventsRepository: EventsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventsRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: FinishedViewModelFactory? = null

        fun getInstance(context: Context): FinishedViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FinishedViewModelFactory(
                    Injection.provideRepository(context)
                ).also { instance = it }
            }
    }
}