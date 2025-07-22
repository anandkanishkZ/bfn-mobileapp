package com.natrajtechnology.bfn.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natrajtechnology.bfn.data.model.BloodRequest
import com.natrajtechnology.bfn.data.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodRequestViewModel @Inject constructor(
    private val bloodRepository: BloodRepository
) : ViewModel() {
    private val _requests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val requests: StateFlow<List<BloodRequest>> = _requests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadRecentRequests(limit: Int = 5) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = bloodRepository.getBloodRequests(limit)
            _isLoading.value = false
            if (result.isSuccess) {
                _requests.value = result.getOrDefault(emptyList())
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun createRequest(request: BloodRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = bloodRepository.createBloodRequest(request)
            if (result.isSuccess) {
                onSuccess()
                loadRecentRequests()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun updateRequest(request: BloodRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = bloodRepository.updateBloodRequest(request)
            if (result.isSuccess) {
                onSuccess()
                loadRecentRequests()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
