package com.natrajtechnology.bfn.data.network

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.natrajtechnology.bfn.utils.CloudinaryConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class CloudinaryService @Inject constructor(
    private val context: Context
) {
    
    init {
        initializeCloudinary()
    }
    
    private fun initializeCloudinary() {
        try {
            val config = hashMapOf(
                "cloud_name" to CloudinaryConfig.CLOUD_NAME,
                "api_key" to CloudinaryConfig.API_KEY,
                "api_secret" to CloudinaryConfig.API_SECRET,
                "secure" to true
            )
            MediaManager.init(context, config)
            println("Cloudinary initialized successfully with cloud_name: ${CloudinaryConfig.CLOUD_NAME}")
        } catch (e: Exception) {
            println("Cloudinary initialization failed: ${e.message}")
            e.printStackTrace()
        }
    }
    
    suspend fun uploadProfileImage(imageUri: Uri, userId: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            try {
                println("Starting signed upload for user: $userId")
                val publicId = "profile_pictures/${userId}_${System.currentTimeMillis()}"
                
                val options = hashMapOf(
                    "public_id" to publicId,
                    "folder" to CloudinaryConfig.PROFILE_PICTURES_FOLDER,
                    "transformation" to CloudinaryConfig.PROFILE_IMAGE_TRANSFORMATION,
                    "resource_type" to "image",
                    "overwrite" to true
                )
                
                val requestId = MediaManager.get().upload(imageUri)
                    .options(options)
                    .callback(object : UploadCallback {
                        override fun onStart(requestId: String) {
                            println("Signed upload started with request ID: $requestId")
                        }
                        
                        override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                            val progress = (bytes * 100 / totalBytes).toInt()
                            println("Signed upload progress: $progress%")
                        }
                        
                        override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                            println("Signed upload success! Result data: $resultData")
                            val secureUrl = resultData["secure_url"] as? String
                            if (secureUrl != null) {
                                println("Secure URL received: $secureUrl")
                                continuation.resume(Result.success(secureUrl))
                            } else {
                                println("No secure URL in result data")
                                continuation.resume(Result.failure(Exception("Failed to get image URL from signed upload")))
                            }
                        }
                        
                        override fun onError(requestId: String, error: ErrorInfo) {
                            println("Signed upload error: ${error.description}")
                            println("Error code: ${error.code}")
                            continuation.resume(Result.failure(Exception("Signed upload failed: ${error.description} (Code: ${error.code})")))
                        }
                        
                        override fun onReschedule(requestId: String, error: ErrorInfo) {
                            println("Signed upload rescheduled: ${error.description}")
                        }
                    })
                    .dispatch()
                
                continuation.invokeOnCancellation {
                    println("Signed upload cancelled")
                    MediaManager.get().cancelRequest(requestId)
                }
                
            } catch (e: Exception) {
                println("Exception during signed upload setup: ${e.message}")
                continuation.resume(Result.failure(e))
            }
        }
    }
    
    suspend fun uploadImageWithUnsignedPreset(imageUri: Uri, userId: String): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            try {
                println("Starting upload for user: $userId")
                println("Image URI: $imageUri")
                println("Upload preset: ${CloudinaryConfig.UPLOAD_PRESET}")
                
                val publicId = "profile_pictures/${userId}_${System.currentTimeMillis()}"
                
                val requestId = MediaManager.get().upload(imageUri)
                    .unsigned(CloudinaryConfig.UPLOAD_PRESET)
                    .option("public_id", publicId)
                    .option("folder", CloudinaryConfig.PROFILE_PICTURES_FOLDER)
                    .option("resource_type", "image")
                    .callback(object : UploadCallback {
                        override fun onStart(requestId: String) {
                            println("Upload started with request ID: $requestId")
                        }
                        
                        override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                            val progress = (bytes * 100 / totalBytes).toInt()
                            println("Upload progress: $progress%")
                        }
                        
                        override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                            println("Upload success! Result data: $resultData")
                            val secureUrl = resultData["secure_url"] as? String
                            if (secureUrl != null) {
                                println("Secure URL received: $secureUrl")
                                continuation.resume(Result.success(secureUrl))
                            } else {
                                println("No secure URL in result data")
                                continuation.resume(Result.failure(Exception("Failed to get image URL from response")))
                            }
                        }
                        
                        override fun onError(requestId: String, error: ErrorInfo) {
                            println("Upload error: ${error.description}")
                            println("Error code: ${error.code}")
                            continuation.resume(Result.failure(Exception("Upload failed: ${error.description} (Code: ${error.code})")))
                        }
                        
                        override fun onReschedule(requestId: String, error: ErrorInfo) {
                            println("Upload rescheduled: ${error.description}")
                        }
                    })
                    .dispatch()
                
                continuation.invokeOnCancellation {
                    println("Upload cancelled")
                    MediaManager.get().cancelRequest(requestId)
                }
                
            } catch (e: Exception) {
                println("Exception during upload setup: ${e.message}")
                continuation.resume(Result.failure(e))
            }
        }
    }
    
    /**
     * Test Cloudinary connection and configuration
     */
    fun testConnection(): Boolean {
        return try {
            val isInitialized = MediaManager.get() != null
            println("Cloudinary MediaManager initialized: $isInitialized")
            
            // Test basic configuration
            println("Cloud Name: ${CloudinaryConfig.CLOUD_NAME}")
            println("API Key: ${CloudinaryConfig.API_KEY}")
            println("Upload Preset: ${CloudinaryConfig.UPLOAD_PRESET}")
            
            isInitialized
        } catch (e: Exception) {
            println("Cloudinary test connection failed: ${e.message}")
            false
        }
    }

    /**
     * Get file path from URI for file uploads
     */
    private fun getFilePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }
}
