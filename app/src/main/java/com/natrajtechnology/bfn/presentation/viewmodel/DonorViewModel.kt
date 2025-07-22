package com.natrajtechnology.bfn.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natrajtechnology.bfn.data.model.Donor
import com.natrajtechnology.bfn.data.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonorViewModel @Inject constructor(
    private val bloodRepository: BloodRepository
) : ViewModel() {
    private val _donors = MutableStateFlow<List<Donor>>(emptyList())
    val donors: StateFlow<List<Donor>> = _donors.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadDonors(limit: Int = 50) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = bloodRepository.getDonors(limit)
            _isLoading.value = false
            if (result.isSuccess) {
                _donors.value = result.getOrDefault(emptyList())
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun registerDonor(donor: Donor, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = bloodRepository.registerDonor(donor)
            if (result.isSuccess) {
                onSuccess()
                loadDonors()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun updateDonor(donor: Donor, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = bloodRepository.updateDonor(donor)
            if (result.isSuccess) {
                onSuccess()
                loadDonors()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun setDonorActiveStatus(donorId: String, isActive: Boolean, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = bloodRepository.setDonorActiveStatus(donorId, isActive)
            if (result.isSuccess) {
                onSuccess()
                loadDonors()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
