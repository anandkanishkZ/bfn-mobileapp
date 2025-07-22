package com.natrajtechnology.bfn.presentation.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natrajtechnology.bfn.data.model.BloodRequest
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestBloodScreen(
    onSubmit: (BloodRequest) -> Unit,
    onBack: () -> Unit,
    initialRequest: BloodRequest? = null
) {
    var patientName by remember { mutableStateOf(initialRequest?.patientName ?: "") }
    var gender by remember { mutableStateOf(initialRequest?.gender ?: "") }
    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Other")
    var bloodType by remember { mutableStateOf(initialRequest?.bloodType ?: "") }
    var bloodTypeExpanded by remember { mutableStateOf(false) }
    val bloodTypeOptions = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var urgency by remember { mutableStateOf(initialRequest?.urgency ?: "") }
    var urgencyExpanded by remember { mutableStateOf(false) }
    val urgencyOptions = listOf("HIGH", "MEDIUM", "LOW")
    var unitsNeeded by remember { mutableStateOf(if (initialRequest != null && initialRequest.unitsNeeded > 0) initialRequest.unitsNeeded.toString() else "") }
    var hospital by remember { mutableStateOf(initialRequest?.hospital ?: "") }
    var city by remember { mutableStateOf(initialRequest?.city ?: "") }
    var contactPerson by remember { mutableStateOf(initialRequest?.contactPerson ?: "") }
    var contactPhone by remember { mutableStateOf(initialRequest?.contactPhone ?: "") }
    var error by remember { mutableStateOf<String?>(null) }
    var case by remember { mutableStateOf(initialRequest?.case ?: "") }

    // Add a SnackbarHost for notifications
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        // Use scrollState for verticalScroll on a Column for guaranteed scrollability
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
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
                            contentDescription = "Request Blood",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Request Blood",
                            fontSize = 24.sp,
                            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF43A047),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = { Text("Patient Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Gender Dropdown
            val genderOptions = listOf("Male", "Female", "Other")
            var genderExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = case,
                onValueChange = { case = it },
                label = { Text("Case") },
                modifier = Modifier.fillMaxWidth()
            )

            // Blood Type Dropdown
            val bloodTypeOptions = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
            var bloodTypeExpanded by remember { mutableStateOf(false) }
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

            OutlinedTextField(
                value = unitsNeeded,
                onValueChange = { unitsNeeded = it.filter { c -> c.isDigit() } },
                label = { Text("Units Needed") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Urgency Dropdown
            val urgencyOptions = listOf("High", "Medium", "Low")
            var urgencyExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = urgencyExpanded,
                onExpandedChange = { urgencyExpanded = !urgencyExpanded }
            ) {
                OutlinedTextField(
                    value = urgency,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Urgency") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = urgencyExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = urgencyExpanded,
                    onDismissRequest = { urgencyExpanded = false }
                ) {
                    urgencyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                urgency = option
                                urgencyExpanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = hospital,
                onValueChange = { hospital = it },
                label = { Text("Hospital") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contactPerson,
                onValueChange = { contactPerson = it },
                label = { Text("Contact Person") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contactPhone,
                onValueChange = { contactPhone = it.filter { c -> c.isDigit() } },
                label = { Text("Contact Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onBack) { Text("Cancel") }
                Button(onClick = {
                    onSubmit(
                        BloodRequest(
                            id = System.currentTimeMillis().toString(),
                            patientName = patientName,
                            gender = gender, // Save gender
                            case = case,     // Save case
                            bloodType = bloodType,
                            unitsNeeded = unitsNeeded.toIntOrNull() ?: 1,
                            urgency = urgency.uppercase(),
                            hospital = hospital,
                            city = city,
                            contactPerson = contactPerson,
                            contactPhone = contactPhone
                        )
                    )
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Submit")
                    Spacer(Modifier.width(4.dp))
                    Text("Submit Request")
                }
            }
            Spacer(Modifier.height(70.dp))
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
