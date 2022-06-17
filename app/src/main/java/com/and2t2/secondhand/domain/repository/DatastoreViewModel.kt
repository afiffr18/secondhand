package com.and2t2.secondhand.domain.repository

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class DatastoreViewModel(private val pref: DatastoreManager): ViewModel() {

    fun saveLoginState(value: Boolean) {
        viewModelScope.launch {
            pref.saveLoginStateToDataStore(value)
        }
    }

    fun getLoginState() : LiveData<Boolean> {
        return pref.readLoginStateFromDataStore().asLiveData()
    }

    fun saveAccessToken(value: String) {
        viewModelScope.launch {
            pref.saveAccessTokenToDataStore(value)
        }
    }

    fun getAccessToken() : LiveData<String> {
        return pref.readAccessTokenFromDataStore().asLiveData()
    }

    fun deleteAllData() {
        viewModelScope.launch {
            pref.removeFromDataStore()
        }
    }
}