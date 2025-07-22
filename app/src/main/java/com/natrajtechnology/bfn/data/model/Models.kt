package com.natrajtechnology.bfn.data.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val bloodType: String = "",
    val dateOfBirth: String = "",
    val address: String = "",
    val city: String = "",
    val district: String = "",
    val province: String = "",
    val isVerified: Boolean = false,
    val isActive: Boolean = true,
    val profilePicture: String = "",
    val lastDonationDate: String = "",
    val totalDonations: Int = 0,
    val emergencyContact: String = "",
    val medicalConditions: String = "",
    val preferredLanguage: String = "en",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // Required no-argument constructor for Firestore
    constructor() : this(
        id = "",
        firstName = "",
        lastName = "",
        email = "",
        phoneNumber = "",
        bloodType = "",
        dateOfBirth = "",
        address = "",
        city = "",
        district = "",
        province = "",
        isVerified = false,
        isActive = true,
        profilePicture = "",
        lastDonationDate = "",
        totalDonations = 0,
        emergencyContact = "",
        medicalConditions = "",
        preferredLanguage = "en",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}

enum class BloodType(val displayName: String) {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-")
}

data class BloodRequest(
    val id: String = "",
    val patientName: String = "",
    val gender: String = "", // Added
    val case: String = "",   // Added
    val bloodType: String = "",
    val unitsNeeded: Int = 0,
    val urgency: String = "", // HIGH, MEDIUM, LOW
    val hospital: String = "",
    val contactPerson: String = "",
    val contactPhone: String = "",
    val requiredBy: String = "",
    val description: String = "",
    val requestedBy: String = "",
    val status: String = "ACTIVE", // ACTIVE, FULFILLED, CANCELLED
    val location: String = "",
    val city: String = "",
    val district: String = "",
    val province: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class DonationRecord(
    val id: String = "",
    val donorId: String = "",
    val recipientId: String = "",
    val bloodType: String = "",
    val units: Int = 0,
    val donationDate: String = "",
    val hospital: String = "",
    val notes: String = "",
    val status: String = "COMPLETED", // COMPLETED, CANCELLED
    val createdAt: Long = System.currentTimeMillis()
)

data class Donor(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val bloodType: String = "",
    val city: String = "",
    val phoneNumber: String = "",
    val lastDonationDate: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val active: Boolean = true // Added for activation/deactivation
)
