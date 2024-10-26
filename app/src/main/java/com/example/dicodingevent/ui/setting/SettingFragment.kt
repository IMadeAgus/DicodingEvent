package com.example.dicodingevent.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.dicodingevent.MainActivity
import com.example.dicodingevent.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        val pref = SettingPreferences.getInstance(mainActivity.getDataStore())
        val factory = ViewModelFactory(pref)
        settingViewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]
        workManager = WorkManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        val switchTheme = binding.switchTheme
        val switchNotification = binding.switchEventNotifications

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            switchTheme.isChecked = isDarkModeActive
        }
        settingViewModel.getNotificationSettings().observe(viewLifecycleOwner) { isNotificationActive: Boolean ->
            switchNotification.isChecked = isNotificationActive
        }


        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveNotificationSetting(isChecked)
        }
        return binding.root
    }
   override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
