package com.natrajtechnology.bfn.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.natrajtechnology.bfn.data.model.BloodRequest
import com.natrajtechnology.bfn.data.model.DonationRecord
import com.natrajtechnology.bfn.data.model.Donor
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BloodRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    // Blood Requests
    suspend fun createBloodRequest(request: BloodRequest): Result<String> {
        return try {
            val docRef = firestore.collection("blood_requests").document()
            val requestWithId = request.copy(id = docRef.id)
            docRef.set(requestWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getBloodRequests(limit: Int = 20): Result<List<BloodRequest>> {
        return try {
            val snapshot = firestore.collection("blood_requests")
                .whereEqualTo("status", "ACTIVE")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            val requests = snapshot.documents.mapNotNull { doc ->
                doc.toObject(BloodRequest::class.java)
            }
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getBloodRequestsByType(bloodType: String, limit: Int = 20): Result<List<BloodRequest>> {
        return try {
            val snapshot = firestore.collection("blood_requests")
                .whereEqualTo("bloodType", bloodType)
                .whereEqualTo("status", "ACTIVE")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            val requests = snapshot.documents.mapNotNull { doc ->
                doc.toObject(BloodRequest::class.java)
            }
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getBloodRequestsByLocation(city: String, limit: Int = 20): Result<List<BloodRequest>> {
        return try {
            val snapshot = firestore.collection("blood_requests")
                .whereEqualTo("city", city)
                .whereEqualTo("status", "ACTIVE")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            val requests = snapshot.documents.mapNotNull { doc ->
                doc.toObject(BloodRequest::class.java)
            }
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateBloodRequestStatus(requestId: String, status: String): Result<Unit> {
        return try {
            firestore.collection("blood_requests")
                .document(requestId)
                .update(
                    mapOf(
                        "status" to status,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateBloodRequest(request: BloodRequest): Result<Unit> {
        return try {
            firestore.collection("blood_requests")
                .document(request.id)
                .set(request.copy(updatedAt = System.currentTimeMillis()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Donation Records
    suspend fun createDonationRecord(record: DonationRecord): Result<String> {
        return try {
            val docRef = firestore.collection("donations").document()
            val recordWithId = record.copy(id = docRef.id)
            docRef.set(recordWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getDonationHistory(donorId: String): Result<List<DonationRecord>> {
        return try {
            val snapshot = firestore.collection("donations")
                .whereEqualTo("donorId", donorId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val donations = snapshot.documents.mapNotNull { doc ->
                doc.toObject(DonationRecord::class.java)
            }
            Result.success(donations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getDonationStats(donorId: String): Result<Map<String, Int>> {
        return try {
            val snapshot = firestore.collection("donations")
                .whereEqualTo("donorId", donorId)
                .whereEqualTo("status", "COMPLETED")
                .get()
                .await()
            
            val totalDonations = snapshot.documents.size
            val totalUnits = snapshot.documents.sumOf { doc ->
                doc.getLong("units")?.toInt() ?: 0
            }
            
            Result.success(
                mapOf(
                    "totalDonations" to totalDonations,
                    "totalUnits" to totalUnits
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Donors
    suspend fun registerDonor(donor: Donor): Result<String> {
        return try {
            val docRef = firestore.collection("donors").document()
            val donorWithId = donor.copy(id = docRef.id)
            docRef.set(donorWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDonors(limit: Int = 50): Result<List<Donor>> {
        return try {
            val snapshot = firestore.collection("donors")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            val donors = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Donor::class.java)
            }
            Result.success(donors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Search functionality
    suspend fun searchDonorsByBloodType(bloodType: String, city: String? = null): Result<List<String>> {
        return try {
            var query = firestore.collection("users")
                .whereEqualTo("bloodType", bloodType)
                .whereEqualTo("isActive", true)
            
            if (city != null) {
                query = query.whereEqualTo("city", city)
            }
            
            val snapshot = query.get().await()
            val donorIds = snapshot.documents.map { it.id }
            
            Result.success(donorIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateDonor(donor: Donor): Result<Unit> {
        return try {
            if (donor.id.isEmpty()) {
                return Result.failure(Exception("Donor ID cannot be empty"))
            }
            firestore.collection("donors").document(donor.id)
                .set(donor.copy(updatedAt = System.currentTimeMillis()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setDonorActiveStatus(donorId: String, isActive: Boolean): Result<Unit> {
        return try {
            firestore.collection("donors").document(donorId)
                .update("active", isActive, "updatedAt", System.currentTimeMillis())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
