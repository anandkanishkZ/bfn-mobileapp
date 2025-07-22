package com.natrajtechnology.bfn.presentation.screens.donors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natrajtechnology.bfn.data.model.Donor
import androidx.compose.foundation.shape.RoundedCornerShape
import com.natrajtechnology.bfn.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorsScreen(
    donors: List<Donor>,
    isLoading: Boolean,
    error: String?,
    onRegister: (Donor) -> Unit,
    isRegistered: Boolean,
    onRefresh: () -> Unit,
    currentUserId: String = "",
    currentUser: User? = null,
    onUpdateDonor: (Donor) -> Unit = {},
    onDeactivateDonor: (Donor) -> Unit = {}
) {
    var showRegisterDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var lastDonationDate by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var editingDonor by remember { mutableStateOf<Donor?>(null) }
    var bloodTypeExpanded by remember { mutableStateOf(false) }
    var editBloodTypeExpanded by remember { mutableStateOf(false) }

    // Function to clear form fields
    fun clearFormFields() {
        name = ""
        bloodType = ""
        city = ""
        phone = ""
        lastDonationDate = ""
        errorMsg = null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 6.dp,
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Donors",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "All Donors",
                            fontSize = 24.sp,
                            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color(0xFF43A047), // Green
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Donors", fontSize = 24.sp, fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
                if (!isRegistered) {
                    Button(onClick = { 
                        // Pre-fill form with user information
                        currentUser?.let { user ->
                            name = "${user.firstName} ${user.lastName}".trim()
                            bloodType = user.bloodType
                            city = user.city
                            phone = user.phoneNumber
                            lastDonationDate = user.lastDonationDate
                        }
                        showRegisterDialog = true 
                    }) {
                        Text("Become a Donor")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(donors) { donor ->
                        val cardColor = if (donor.active)
                            Color(0xFFF0F9F5) // Light mint green background for active
                        else
                            Color(0xFFFAF5F5) // Light warm gray background for inactive
                        val borderColor = if (donor.active)
                            Color(0xFF22C55E) // Modern emerald green border for active
                        else
                            Color(0xFF94A3B8) // Subtle slate gray border for inactive
                        Card(
                            Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            border = BorderStroke(2.dp, borderColor)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        donor.name,
                                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                                        modifier = Modifier.weight(1f),
                                        color = if (donor.active) Color(0xFF065F46) else Color(0xFF6B7280) // Dark green for active, gray for inactive
                                    )
                                    if (donor.userId == currentUserId) {
                                        IconButton(onClick = {
                                            editingDonor = donor
                                            name = donor.name
                                            bloodType = donor.bloodType
                                            city = donor.city
                                            phone = donor.phoneNumber
                                            lastDonationDate = donor.lastDonationDate
                                            showEditDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = {
                                            editingDonor = donor
                                            showDeactivateDialog = true
                                        }) {
                                            Icon(
                                                imageVector = if (donor.active) Icons.Default.ToggleOff else Icons.Default.ToggleOn,
                                                contentDescription = if (donor.active) "Deactivate" else "Activate",
                                                tint = if (donor.active) Color(0xFF059669) else Color(0xFFDC2626) // Green for active toggle, red for inactive toggle
                                            )
                                        }
                                    }
                                }
                                Text("Blood Type: ${donor.bloodType}", color = MaterialTheme.colorScheme.onSurface)
                                Text("City: ${donor.city}", color = MaterialTheme.colorScheme.onSurface)
                                Text("Phone: ${donor.phoneNumber}", color = MaterialTheme.colorScheme.onSurface)
                                if (donor.lastDonationDate.isNotBlank())
                                    Text("Last Donation: ${donor.lastDonationDate}", color = MaterialTheme.colorScheme.onSurface)
                                if (!donor.active) {
                                    Text(
                                        "Inactive",
                                        color = Color(0xFF6B7280), // Professional gray for inactive status
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        // Register Dialog
        if (showRegisterDialog) {
            AlertDialog(
                onDismissRequest = { showRegisterDialog = false },
                title = { Text("Register as Donor") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                        
                        // Blood Type Dropdown
                        val bloodTypeOptions = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
                        ExposedDropdownMenuBox(
                            expanded = bloodTypeExpanded,
                            onExpandedChange = { bloodTypeExpanded = !bloodTypeExpanded }
                        ) {
                            OutlinedTextField(
                                value = bloodType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Blood Type") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodTypeExpanded) }
                            )
                            ExposedDropdownMenu(
                                expanded = bloodTypeExpanded,
                                onDismissRequest = { bloodTypeExpanded = false }
                            ) {
                                bloodTypeOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            bloodType = option
                                            bloodTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") })
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") })
                        OutlinedTextField(value = lastDonationDate, onValueChange = { lastDonationDate = it }, label = { Text("Last Donation Date (optional)") })
                        if (errorMsg != null) Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (name.isBlank() || bloodType.isBlank() || city.isBlank() || phone.isBlank()) {
                            errorMsg = "Please fill all required fields."
                        } else {
                            errorMsg = null
                            onRegister(
                                Donor(
                                    name = name,
                                    bloodType = bloodType,
                                    city = city,
                                    phoneNumber = phone,
                                    lastDonationDate = lastDonationDate
                                )
                            )
                            showRegisterDialog = false
                        }
                    }) { Text("Register") }
                },
                dismissButton = {
                    TextButton(onClick = { showRegisterDialog = false }) { Text("Cancel") }
                }
            )
        }
        // Edit Dialog
        if (showEditDialog && editingDonor != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Donor Info") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                        
                        // Blood Type Dropdown for Edit
                        val bloodTypeOptions = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
                        ExposedDropdownMenuBox(
                            expanded = editBloodTypeExpanded,
                            onExpandedChange = { editBloodTypeExpanded = !editBloodTypeExpanded }
                        ) {
                            OutlinedTextField(
                                value = bloodType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Blood Type") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = editBloodTypeExpanded) }
                            )
                            ExposedDropdownMenu(
                                expanded = editBloodTypeExpanded,
                                onDismissRequest = { editBloodTypeExpanded = false }
                            ) {
                                bloodTypeOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            bloodType = option
                                            editBloodTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") })
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") })
                        OutlinedTextField(value = lastDonationDate, onValueChange = { lastDonationDate = it }, label = { Text("Last Donation Date (optional)") })
                        if (errorMsg != null) Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (name.isBlank() || bloodType.isBlank() || city.isBlank() || phone.isBlank()) {
                            errorMsg = "Please fill all required fields."
                        } else {
                            errorMsg = null
                            onUpdateDonor(
                                editingDonor!!.copy(
                                    name = name,
                                    bloodType = bloodType,
                                    city = city,
                                    phoneNumber = phone,
                                    lastDonationDate = lastDonationDate
                                )
                            )
                            showEditDialog = false
                        }
                    }) { Text("Update") }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
                }
            )
        }
        // Deactivate Dialog
        if (showDeactivateDialog && editingDonor != null) {
            val isCurrentlyActive = editingDonor!!.active
            AlertDialog(
                onDismissRequest = { showDeactivateDialog = false },
                title = { Text(if (isCurrentlyActive) "Deactivate Donor" else "Activate Donor") },
                text = { Text(
                    if (isCurrentlyActive)
                        "Are you sure you want to deactivate your donor registration?"
                    else
                        "Do you want to activate your donor registration again?"
                ) },
                confirmButton = {
                    Button(onClick = {
                        onDeactivateDonor(editingDonor!!.copy(active = !isCurrentlyActive))
                        showDeactivateDialog = false
                    }) { Text(if (isCurrentlyActive) "Deactivate" else "Activate") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeactivateDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
