package com.natrajtechnology.bfn.presentation.navigation

sealed class Screen(val route: String) {
    // Splash Screen
    object Splash : Screen("splash")
    
    // Auth Screens
    object Login : Screen("login")
    object SignUp : Screen("sign_up")
    object ForgotPassword : Screen("forgot_password")
    
    // Main App Screens
    object Home : Screen("home")
    object Profile : Screen("profile")
    object BloodRequests : Screen("blood_requests")
    object CreateRequest : Screen("create_request")
    object DonationHistory : Screen("donation_history")
    object Settings : Screen("settings")
    object About : Screen("about")
    
    // Detail Screens
    object RequestDetails : Screen("request_details/{requestId}") {
        fun createRoute(requestId: String) = "request_details/$requestId"
    }
    object DonorProfile : Screen("donor_profile/{donorId}") {
        fun createRoute(donorId: String) = "donor_profile/$donorId"
    }
}
