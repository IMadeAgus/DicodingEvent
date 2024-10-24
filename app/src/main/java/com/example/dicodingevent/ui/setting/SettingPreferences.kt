package com.example.dicodingevent.ui.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    // Mengambil pengaturan notifikasi
//    fun getNotificationSetting(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[NOTIFICATION_KEY] ?: false
//        }
//    }

    // Menyimpan pengaturan notifikasi
//    suspend fun saveNotificationSetting(isNotificationEnabled: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[NOTIFICATION_KEY] = isNotificationEnabled
//        }
//    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
//        private val NOTIFICATION_KEY = booleanPreferencesKey("notification_setting")

        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}