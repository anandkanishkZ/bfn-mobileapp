package com.natrajtechnology.bfn.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Surface
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Overview : BottomNavItem("home", Icons.Default.Home, "Overview")
    object Requests : BottomNavItem("requests", Icons.Default.Menu, "Requests")
    object RequestBlood : BottomNavItem("create_request", Icons.Default.Add, "Request Blood")
    object Donors : BottomNavItem("donors", Icons.Default.Person, "Donors")
    object Profile : BottomNavItem("profile", Icons.Default.AccountCircle, "Profile")
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Overview,
        BottomNavItem.Requests,
        BottomNavItem.RequestBlood,
        BottomNavItem.Donors,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Professional color scheme for blood donation app
    val activeColor = Color(0xFFDC143C) // Crimson red - represents blood
    val inactiveColor = Color(0xFF757575) // Medium gray
    val requestBloodActiveColor = Color(0xFFFF1744) // Bright red for emergency feel
    val backgroundColor = Color(0xFFFAFAFA) // Light gray background
    
    NavigationBar(
        modifier = modifier,
        containerColor = backgroundColor,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconColor = when {
                item == BottomNavItem.RequestBlood && isSelected -> requestBloodActiveColor
                isSelected -> activeColor
                else -> inactiveColor
            }
            
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (item == BottomNavItem.RequestBlood) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) requestBloodActiveColor.copy(alpha = 0.1f) else Color.White,
                            tonalElevation = if (isSelected) 2.dp else 1.dp
                        ) {
                            androidx.compose.material3.Icon(
                                item.icon,
                                contentDescription = item.label,
                                tint = iconColor,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        androidx.compose.material3.Icon(
                            item.icon, 
                            contentDescription = item.label,
                            tint = iconColor
                        )
                    }
                },
                label = if (item == BottomNavItem.RequestBlood) null else { 
                    { 
                        Text(
                            item.label,
                            color = if (isSelected) activeColor else inactiveColor
                        ) 
                    } 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Transparent, // We handle icon colors manually
                    unselectedIconColor = Color.Transparent, // We handle icon colors manually
                    selectedTextColor = Color.Transparent, // We handle text colors manually
                    unselectedTextColor = Color.Transparent, // We handle text colors manually
                    indicatorColor = Color.Transparent // Remove the background indicator
                )
            )
        }
    }
}
