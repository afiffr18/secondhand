package com.and2t2.secondhand.domain.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastoreManager(private val context: Context) {

    companion object {
        private const val DATA_STORE_NAME = "datastore_preference"
        private val LOGINSTATE_KEY = booleanPreferencesKey("loginstate_key")
        private val ACCESSTOKEN_KEY = stringPreferencesKey("accesstoken_key")
        private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)
    }

    suspend fun saveLoginStateToDataStore(value: Boolean) {
        context.dataStore.edit { pref ->
            pref[LOGINSTATE_KEY] = value
        }
    }

    fun readLoginStateFromDataStore(): Flow<Boolean> {
        return context.dataStore.data.map { pref ->
            pref[LOGINSTATE_KEY] ?: false
        }
    }

    suspend fun saveAccessTokenToDataStore(value: String) {
        context.dataStore.edit { pref ->
            pref[ACCESSTOKEN_KEY] = value
        }
    }

    fun readAccessTokenFromDataStore(): Flow<String> {
        return context.dataStore.data.map { pref ->
            pref[ACCESSTOKEN_KEY] ?: "default value"
        }
    }

    suspend fun removeFromDataStore() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }
}