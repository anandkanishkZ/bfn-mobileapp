package com.natrajtechnology.bfn.presentation.screens.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natrajtechnology.bfn.data.model.BloodRequest
import com.natrajtechnology.bfn.presentation.viewmodel.AuthViewModel
import com.natrajtechnology.bfn.presentation.viewmodel.BloodRequestViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import com.natrajtechnology.bfn.presentation.screens.request.RequestBloodScreen

@Composable
fun RequestsScreen(
    requests: List<BloodRequest>,
    isLoading: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    authViewModel: AuthViewModel,
    bloodRequestViewModel: BloodRequestViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    var editingRequest by remember { mutableStateOf<BloodRequest?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
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
                            contentDescription = "Blood Requests",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "All Blood Requests",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
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
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(requests) { request ->
                        Card(
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when (request.urgency.uppercase()) {
                                    "HIGH" -> Color(0xFFD32F2F)
                                    "MEDIUM" -> Color(0xFFFFA000)
                                    else -> Color(0xFF43A047)
                                }
                            )
                        ) {
                            val textColor = when (request.urgency.uppercase()) {
                                "HIGH" -> Color.White
                                "MEDIUM" -> Color.Black
                                else -> Color.White
                            }
                            val secondaryTextColor = when (request.urgency.uppercase()) {
                                "HIGH" -> Color.White.copy(alpha = 0.85f)
                                "MEDIUM" -> Color.Black.copy(alpha = 0.85f)
                                else -> Color.White.copy(alpha = 0.85f)
                            }
                            Column(Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        request.patientName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = textColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        request.bloodType,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor,
                                        fontSize = 18.sp
                                    )
                                    if (currentUser != null && request.requestedBy == currentUser!!.id) {
                                        IconButton(onClick = {
                                            editingRequest = request
                                            showEditDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit Request", tint = textColor)
                                        }
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                                Text("Units Needed: ${request.unitsNeeded}", fontWeight = FontWeight.Medium, color = textColor)
                                Text(
                                    "Urgency: ${request.urgency}",
                                    color = when (request.urgency.uppercase()) {
                                        "HIGH" -> Color.White
                                        "MEDIUM" -> Color.Black
                                        else -> Color.White
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Hospital: ${request.hospital}", color = secondaryTextColor)
                                Text("City: ${request.city}", color = secondaryTextColor)
                                Text("Contact: ${request.contactPerson} (${request.contactPhone})", color = secondaryTextColor)
                            }
                        }
                    }
                    // Add extra space at the bottom
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
        if (showEditDialog && editingRequest != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                confirmButton = {},
                dismissButton = {},
                text = {
                    RequestBloodScreen(
                        onSubmit = { updatedRequest ->
                            bloodRequestViewModel.updateRequest(
                                updatedRequest.copy(id = editingRequest!!.id, requestedBy = editingRequest!!.requestedBy),
                                onSuccess = { showEditDialog = false },
                                onError = { showEditDialog = false }
                            )
                        },
                        onBack = { showEditDialog = false },
                        initialRequest = editingRequest
                    )
                }
            )
        }
    }
}
