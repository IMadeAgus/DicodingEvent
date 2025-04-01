package com.example.dicodingevent

import EventWorker
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dicodingevent.databinding.ActivityMainBinding
import com.example.dicodingevent.ui.setting.SettingPreferences
import com.example.dicodingevent.ui.setting.SettingViewModel
import com.example.dicodingevent.ui.setting.ViewModelFactory
import android.Manifest
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dicodingevent.ui.dialogs.NetworkDialog
import com.example.eventapp.utils.NetworkUtil
import com.example.eventapp.utils.NetworkUtil.isNetworkAvailable
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<SettingViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var networkChangeReceiver: NetworkUtil.NetworkChangeReceiver
    private var networkDialog: NetworkDialog? = null
    private var dataRefreshListener: NetworkChangeListener? = null

    private lateinit var workManager: WorkManager

    // Launcher untuk meminta izin kepada user untuk menampilkan notifikasi
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        viewModel.getThemeSettings().observe(this@MainActivity) {
            applyDarkMode(it)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()

        networkDialog = NetworkDialog(this)
        setupNetworkChangeReceiver()

    }

    private fun setupBottomNavigation(){
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,
                R.id.navigation_favorite, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupNetworkChangeReceiver() {
        networkChangeReceiver = NetworkUtil.NetworkChangeReceiver { isConnected ->
            if (!isConnected) {
                networkDialog?.showNoInternetDialog {
                    if (isNetworkAvailable(this)) {
                        networkDialog?.dismissDialog()
                        dataRefreshListener?.onNetworkChanged()
                    } else {
                        Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                networkDialog?.dismissDialog()
                dataRefreshListener?.onNetworkChanged()
            }
        }
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, filter)
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkDialog?.dismissDialog()
    }

    interface NetworkChangeListener {
        fun onNetworkChanged()
    }
}
