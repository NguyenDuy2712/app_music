package com.example.musicapp.utils.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {
    lateinit var job: Job
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val noInternet : MutableLiveData<Boolean> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}

