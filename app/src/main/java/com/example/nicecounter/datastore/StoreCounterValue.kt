package com.example.nicecounter.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreCounterValue(private val context : Context) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("CounterValue")
        val COUNTER_VALUE_KEY = intPreferencesKey("counter_value")
    }

    val getCounterValue: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[COUNTER_VALUE_KEY] ?: 0
        }

    suspend fun saveCounterValue(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[COUNTER_VALUE_KEY] = value
        }
    }
}
