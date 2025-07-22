package com.natrajtechnology.bfn.presentation.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.natrajtechnology.bfn.R
import com.natrajtechnology.bfn.presentation.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
    isDonor: Boolean = false,
    onActivateDonor: () -> Unit = {},
    onDeactivateDonor: () -> Unit = {}
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var isUploadingPhoto by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    val backPressInterval = 2000L // 2 seconds

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploadingPhoto = true
            viewModel.uploadProfilePhoto(uri) { success ->
                isUploadingPhoto = false
                if (success) {
                    Toast.makeText(context, "Profile photo updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun handleDoubleBackPress() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < backPressInterval) {
            // Double press detected: logout and go to Login
            onSignOut()
        } else {
            lastBackPressTime = currentTime
            Toast.makeText(context, "Press back again to logout", Toast.LENGTH_SHORT).show()
        }
    }

    if (showEditDialog && currentUser != null) {
        EditProfileDialog(
            user = currentUser!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedUser ->
                viewModel.updateProfile(updatedUser)
                showEditDialog = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Back button at the top left
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { handleDoubleBackPress() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Profile picture, name, and edit button overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentUser?.profilePicture?.isNotEmpty() == true) {
                            AsyncImage(
                                model = currentUser?.profilePicture,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                fallback = painterResource(id = R.drawable.logo_transparent)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.logo_transparent),
                                contentDescription = "Default Profile Picture",
                                modifier = Modifier.size(100.dp).clip(CircleShape)
                            )
                        }
                        
                        // Camera overlay icon
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isUploadingPhoto) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "${currentUser?.firstName ?: "User"} ${currentUser?.lastName ?: ""}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = currentUser?.email ?: "",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                // Edit button overlayed at top right
                IconButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .padding(top = 4.dp, end = 4.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(20.dp)) {
                    ProfileInfoRow(label = "Phone", value = currentUser?.phoneNumber ?: "-")
                    ProfileInfoRow(label = "Blood Type", value = currentUser?.bloodType ?: "-")
                    ProfileInfoRow(label = "City", value = currentUser?.city ?: "-")
                    ProfileInfoRow(label = "District", value = currentUser?.district ?: "-")
                    ProfileInfoRow(label = "Province", value = currentUser?.province ?: "-")
                    ProfileInfoRow(label = "Date of Birth", value = currentUser?.dateOfBirth ?: "-")
                    ProfileInfoRow(label = "Last Donation", value = currentUser?.lastDonationDate ?: "-")
                    ProfileInfoRow(label = "Total Donations", value = currentUser?.totalDonations?.toString() ?: "0")
                }
            }
            Spacer(Modifier.height(16.dp))
            // Active/Deactivate Donor button with dedicated icon
            val isActiveDonor = isDonor // isDonor now means active donor
            if (isActiveDonor) {
                Button(
                    onClick = { showDeactivateDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ToggleOff, // Use ToggleOff for deactivation
                        contentDescription = "Deactivate Donor",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Deactivate Donor")
                }
                Spacer(Modifier.height(16.dp))
            } else {
                Button(
                    onClick = onActivateDonor,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ToggleOn, // Use ToggleOn for activation
                        contentDescription = "Become a Donor",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Become a Donor")
                }
                Spacer(Modifier.height(16.dp))
                // Sign Out button below Become a Donor
                Button(
                    onClick = { onSignOut() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Logout", color = Color.White)
                }
            }
            // Deactivate/Activate confirmation dialog
            if (showDeactivateDialog) {
                AlertDialog(
                    onDismissRequest = { showDeactivateDialog = false },
                    title = { Text("Deactivate Donor") },
                    text = { Text("Are you sure you want to deactivate your donor registration?") },
                    confirmButton = {
                        Button(onClick = {
                            onDeactivateDonor()
                            showDeactivateDialog = false
                        }) { Text("Deactivate") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeactivateDialog = false }) { Text("Cancel") }
                    }
                )
            }
            Spacer(Modifier.height(32.dp))
            // Logout option
            Button(
                onClick = { onSignOut() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text("Logout", color = Color.White)
            }
            // Error message
            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    user: com.natrajtechnology.bfn.data.model.User,
    onDismiss: () -> Unit,
    onSave: (com.natrajtechnology.bfn.data.model.User) -> Unit
) {
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var phone by remember { mutableStateOf(user.phoneNumber) }
    var city by remember { mutableStateOf(user.city) }
    var district by remember { mutableStateOf(user.district) }
    var province by remember { mutableStateOf(user.province) }
    var bloodType by remember { mutableStateOf(user.bloodType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSave(user.copy(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phone,
                    city = city,
                    district = district,
                    province = province,
                    bloodType = bloodType
                ))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("District") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Province") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bloodType,
                    onValueChange = { bloodType = it },
                    label = { Text("Blood Type") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(value, fontWeight = FontWeight.Medium)
    }
}
