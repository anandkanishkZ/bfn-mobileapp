package com.natrajtechnology.bfn.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natrajtechnology.bfn.data.model.User
import com.natrajtechnology.bfn.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = authRepository.signInWithEmailAndPassword(email, password)
            
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    loadUserProfile(user.uid)
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    fun signUp(email: String, password: String, firstName: String, lastName: String, phoneNumber: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = authRepository.createUserWithEmailAndPassword(email, password)
            
            if (result.isSuccess) {
                val firebaseUser = result.getOrNull()
                if (firebaseUser != null) {
                    val user = User(
                        id = firebaseUser.uid,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        phoneNumber = phoneNumber
                    )
                    
                    val saveResult = authRepository.saveUserProfile(user)
                    if (saveResult.isSuccess) {
                        _currentUser.value = user
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = saveResult.exceptionOrNull()?.message
                        )
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _uiState.value = _uiState.value.copy(isAuthenticated = false)
    }
    
    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = authRepository.sendPasswordResetEmail(email)
            
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    message = "Password reset email sent successfully"
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    fun updateProfile(user: User) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = authRepository.updateUserProfile(user)
            
            if (result.isSuccess) {
                _currentUser.value = user
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Profile updated successfully"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    fun uploadProfilePhoto(imageUri: Uri, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                println("AuthViewModel: Starting photo upload")
                val currentUserId = authRepository.getCurrentUser()?.uid
                if (currentUserId == null) {
                    println("AuthViewModel: No current user found")
                    onComplete(false)
                    return@launch
                }
                
                println("AuthViewModel: Current user ID: $currentUserId")
                val result = authRepository.uploadProfilePhoto(currentUserId, imageUri)
                
                if (result.isSuccess) {
                    val photoUrl = result.getOrNull()
                    if (photoUrl != null) {
                        println("AuthViewModel: Photo upload successful, URL: $photoUrl")
                        // Update user profile with new photo URL
                        _currentUser.value?.let { user ->
                            val updatedUser = user.copy(profilePicture = photoUrl)
                            val updateResult = authRepository.updateUserProfile(updatedUser)
                            if (updateResult.isSuccess) {
                                println("AuthViewModel: Profile updated successfully")
                                _currentUser.value = updatedUser
                                onComplete(true)
                            } else {
                                println("AuthViewModel: Failed to update profile: ${updateResult.exceptionOrNull()?.message}")
                                onComplete(false)
                            }
                        } ?: run {
                            println("AuthViewModel: No current user to update")
                            onComplete(false)
                        }
                    } else {
                        println("AuthViewModel: Photo URL is null")
                        onComplete(false)
                    }
                } else {
                    println("AuthViewModel: Photo upload failed: ${result.exceptionOrNull()?.message}")
                    onComplete(false)
                }
            } catch (e: Exception) {
                println("AuthViewModel: Exception during photo upload: ${e.message}")
                onComplete(false)
            }
        }
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val firebaseUser = authRepository.getCurrentUser()
            if (firebaseUser != null) {
                loadUserProfile(firebaseUser.uid)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = false
                )
            }
        }
    }
    
    private suspend fun loadUserProfile(userId: String) {
        try {
            val result = authRepository.getUserProfile(userId)
            
            if (result.isSuccess) {
                val user = result.getOrNull()
                _currentUser.value = user
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load user profile"
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val message: String? = null
)
