package com.natrajtechnology.bfn.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.natrajtechnology.bfn.data.model.User
import com.natrajtechnology.bfn.data.network.CloudinaryService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cloudinaryService: CloudinaryService
) {
    
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun saveUserProfile(user: User): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(user.id)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    // Ensure the user has the correct ID
                    Result.success(user.copy(id = userId))
                } else {
                    Result.failure(Exception("Failed to parse user data"))
                }
            } else {
                // Create a default user profile if it doesn't exist
                val defaultUser = User(id = userId)
                Result.success(defaultUser)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(user.id)
                .update(user.toMap())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun uploadProfilePhoto(userId: String, imageUri: Uri): Result<String> {
        return try {
            println("AuthRepository: Starting photo upload for user $userId")
            
            // First try unsigned preset upload
            val unsignedResult = cloudinaryService.uploadImageWithUnsignedPreset(imageUri, userId)
            
            if (unsignedResult.isSuccess) {
                println("AuthRepository: Unsigned upload successful")
                return unsignedResult
            } else {
                println("AuthRepository: Unsigned upload failed, trying signed upload")
                println("AuthRepository: Unsigned error: ${unsignedResult.exceptionOrNull()?.message}")
                
                // Fallback to signed upload
                val signedResult = cloudinaryService.uploadProfileImage(imageUri, userId)
                
                if (signedResult.isSuccess) {
                    println("AuthRepository: Signed upload successful")
                    return signedResult
                } else {
                    println("AuthRepository: Signed upload also failed: ${signedResult.exceptionOrNull()?.message}")
                    return signedResult
                }
            }
        } catch (e: Exception) {
            println("AuthRepository: Exception during upload: ${e.message}")
            Result.failure(e)
        }
    }
    
    fun signOut() {
        auth.signOut()
    }
}

// Extension function to convert User to Map for Firestore updates
private fun User.toMap(): Map<String, Any> {
    return mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "bloodType" to bloodType,
        "dateOfBirth" to dateOfBirth,
        "address" to address,
        "city" to city,
        "district" to district,
        "province" to province,
        "isVerified" to isVerified,
        "isActive" to isActive,
        "profilePicture" to profilePicture,
        "lastDonationDate" to lastDonationDate,
        "totalDonations" to totalDonations,
        "emergencyContact" to emergencyContact,
        "medicalConditions" to medicalConditions,
        "preferredLanguage" to preferredLanguage,
        "updatedAt" to System.currentTimeMillis()
    )
}
