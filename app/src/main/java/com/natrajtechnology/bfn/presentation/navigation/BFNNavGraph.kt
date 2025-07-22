package com.natrajtechnology.bfn.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.natrajtechnology.bfn.presentation.screens.auth.ForgotPasswordScreen
import com.natrajtechnology.bfn.presentation.screens.auth.LoginScreen
import com.natrajtechnology.bfn.presentation.screens.auth.SignUpScreen
import com.natrajtechnology.bfn.presentation.screens.home.HomeScreen
import com.natrajtechnology.bfn.presentation.screens.request.RequestBloodScreen
import com.natrajtechnology.bfn.presentation.screens.splash.BrandedSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.natrajtechnology.bfn.presentation.viewmodel.BloodRequestViewModel
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import com.natrajtechnology.bfn.presentation.screens.donors.DonorsScreen
import com.natrajtechnology.bfn.presentation.viewmodel.DonorViewModel
import com.natrajtechnology.bfn.data.model.Donor
import com.natrajtechnology.bfn.presentation.viewmodel.AuthViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.collectAsState
import com.natrajtechnology.bfn.presentation.screens.requests.RequestsScreen

@Composable
fun BFNNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            BrandedSplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Auth Flow
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        // Clear back stack to prevent going back to login
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        // Clear back stack to prevent going back to signup
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToCreateRequest = {
                    navController.navigate(Screen.CreateRequest.route)
                },
                onNavigateToBloodRequests = {
                    navController.navigate(Screen.BloodRequests.route)
                }
            )
        }
        
        // Placeholder implementations for missing screens
        composable(Screen.Profile.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
            
            com.natrajtechnology.bfn.presentation.screens.profile.ProfileScreen(
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onActivateDonor = {
                    // Navigate to donors tab which will show the "Become a Donor" button
                    navController.navigate("donors")
                }
            )
        }
        
        composable(Screen.BloodRequests.route) {
            val bloodRequestViewModel: BloodRequestViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            val requests by bloodRequestViewModel.requests.collectAsState()
            val isLoading by bloodRequestViewModel.isLoading.collectAsState()
            val error by bloodRequestViewModel.error.collectAsState()
            LaunchedEffect(Unit) { bloodRequestViewModel.loadRecentRequests(100) }
            com.natrajtechnology.bfn.presentation.screens.requests.RequestsScreen(
                requests = requests,
                isLoading = isLoading,
                error = error,
                onRefresh = { bloodRequestViewModel.loadRecentRequests(100) },
                authViewModel = authViewModel,
                bloodRequestViewModel = bloodRequestViewModel
            )
        }
        
        composable(Screen.CreateRequest.route) {
            val viewModel: BloodRequestViewModel = hiltViewModel()
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            var showSuccess by remember { mutableStateOf(false) }
            var showError by remember { mutableStateOf<String?>(null) }
            RequestBloodScreen(
                onSubmit = { request ->
                    // Validation
                    val missing = mutableListOf<String>()
                    if (request.patientName.isBlank()) missing.add("Patient Name")
                    if (request.bloodType.isBlank()) missing.add("Blood Type")
                    if (request.unitsNeeded <= 0) missing.add("Units Needed")
                    if (request.urgency.isBlank()) missing.add("Urgency")
                    if (request.hospital.isBlank()) missing.add("Hospital")
                    if (request.city.isBlank()) missing.add("City")
                    if (request.contactPerson.isBlank()) missing.add("Contact Person")
                    if (request.contactPhone.isBlank()) missing.add("Contact Phone")
                    if (missing.isNotEmpty()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please fill: ${'$'}{missing.joinToString(", ")}")
                        }
                        return@RequestBloodScreen
                    }
                    viewModel.createRequest(request,
                        onSuccess = {
                            showSuccess = true
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Request submitted successfully!")
                            }
                            navController.popBackStack()
                        },
                        onError = { err ->
                            showError = err
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Failed: ${'$'}err")
                            }
                        }
                    )
                },
                onBack = { navController.popBackStack() }
            )
            // Notification (success/error)
            if (showSuccess) {
                // TODO: Integrate with Android notification manager if needed
            }
            if (showError != null) {
                // TODO: Integrate with Android notification manager if needed
            }
        }
        
        composable(Screen.DonationHistory.route) {
            PlaceholderScreen("Donation History Screen")
        }
        
        composable(Screen.Settings.route) {
            PlaceholderScreen("Settings Screen")
        }
        
        composable(Screen.About.route) {
            PlaceholderScreen("About Screen")
        }
        
        composable("donors") {
            val donorViewModel: DonorViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            val donors by donorViewModel.donors.collectAsState()
            val isLoading by donorViewModel.isLoading.collectAsState()
            val error by donorViewModel.error.collectAsState()
            val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
            val isRegistered = donors.any { donor -> donor.userId == (currentUser?.id ?: "") }
            LaunchedEffect(Unit) { donorViewModel.loadDonors() }
            DonorsScreen(
                donors = donors,
                isLoading = isLoading,
                error = error,
                isRegistered = isRegistered,
                currentUserId = currentUser?.id ?: "",
                currentUser = currentUser,
                onRegister = { donor ->
                    val donorWithUser = donor.copy(userId = currentUser?.id ?: "", name = currentUser?.firstName ?: donor.name, bloodType = currentUser?.bloodType ?: donor.bloodType, phoneNumber = currentUser?.phoneNumber ?: donor.phoneNumber, city = currentUser?.city ?: donor.city)
                    donorViewModel.registerDonor(donorWithUser, onSuccess = {}, onError = {})
                },
                onUpdateDonor = { donor ->
                    donorViewModel.updateDonor(donor, onSuccess = {}, onError = {})
                },
                onDeactivateDonor = { donor ->
                    donorViewModel.setDonorActiveStatus(donor.id, donor.active, onSuccess = {}, onError = {})
                },
                onRefresh = { donorViewModel.loadDonors() }
            )
        }
        
        composable("requests") {
            val bloodRequestViewModel: BloodRequestViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            val requests by bloodRequestViewModel.requests.collectAsState()
            val isLoading by bloodRequestViewModel.isLoading.collectAsState()
            val error by bloodRequestViewModel.error.collectAsState()
            LaunchedEffect(Unit) { bloodRequestViewModel.loadRecentRequests(100) }
            RequestsScreen(
                requests = requests,
                isLoading = isLoading,
                error = error,
                onRefresh = { bloodRequestViewModel.loadRecentRequests(100) },
                authViewModel = authViewModel,
                bloodRequestViewModel = bloodRequestViewModel
            )
        }
        
        composable("profile") {
            val viewModel: AuthViewModel = hiltViewModel()
            val donorViewModel: DonorViewModel = hiltViewModel()
            val donors by donorViewModel.donors.collectAsState()
            val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
            val userDonor = donors.find { it.userId == (currentUser?.id ?: "") }
            val isDonor = userDonor != null
            val isActive = userDonor?.active ?: false
            com.natrajtechnology.bfn.presentation.screens.profile.ProfileScreen(
                onSignOut = {
                    viewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                viewModel = viewModel,
                isDonor = isDonor && isActive,
                onActivateDonor = {
                    navController.navigate("donors")
                },
                onDeactivateDonor = {
                    userDonor?.let {
                        donorViewModel.setDonorActiveStatus(it.id, false, onSuccess = {}, onError = {})
                    }
                }
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(screenName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$screenName - Coming Soon!")
    }
}
