package com.example.dicodingevent.ui.setting

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dicodingevent.MainActivity
import com.example.dicodingevent.databinding.FragmentSettingBinding
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        val pref = SettingPreferences.getInstance(mainActivity.getDataStore())
        val factory = ViewModelFactory(pref)
        settingViewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        val switchTheme = binding.switchTheme
//        val switchNotification = binding.switchEventNotifications

//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val workRequest = PeriodicWorkRequestBuilder<EventWorker>(1, TimeUnit.DAYS)
//            .setConstraints(constraints)
//            .build()

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }

//        settingViewModel.getNotificationSettings().observe(viewLifecycleOwner) { isNotificationEnabled: Boolean ->
//            switchNotification.isChecked = isNotificationEnabled
//        }
//
//        switchNotification.setOnCheckedChangeListener { _, isChecked ->
//            settingViewModel.saveNotificationSetting(isChecked)
//            if (isChecked) {
//                // Check permission saat switch diaktifkan
//                if (Build.VERSION.SDK_INT >= 33) {
//                    if (checkNotificationPermission()) {
//                        enableNotifications(workRequest)
//                    } else {
//                        (requireActivity() as MainActivity).requestPermissionLauncher
//                            .launch(Manifest.permission.POST_NOTIFICATIONS)
//                    }
//                } else {
//                    enableNotifications(workRequest)
//                }
//            } else {
//                disableNotifications()
//            }
//        }
        return binding.root
    }
//    private fun checkNotificationPermission(): Boolean {
//        return if (Build.VERSION.SDK_INT >= 33) {
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true
//        }
//    }
//
//    private fun enableNotifications(workRequest: PeriodicWorkRequest) {
//        settingViewModel.saveNotificationSetting(true)
//        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
//            "EventNotificationWork",
//            ExistingPeriodicWorkPolicy.REPLACE,
//            workRequest
//        )
//    }
//
//    private fun disableNotifications() {
//        settingViewModel.saveNotificationSetting(false)
//        WorkManager.getInstance(requireContext()).cancelUniqueWork("EventNotificationWork")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}