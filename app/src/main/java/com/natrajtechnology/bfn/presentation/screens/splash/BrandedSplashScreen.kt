package com.natrajtechnology.bfn.presentation.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.natrajtechnology.bfn.R
import com.natrajtechnology.bfn.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun BrandedSplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var startAnimation by remember { mutableStateOf(false) }

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "logoAlpha"
    )

    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        label = "titleAlpha"
    )

    val taglineAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 600),
        label = "taglineAlpha"
    )

    val progressAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 900),
        label = "progressAlpha"
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(2500)
        if (uiState.isAuthenticated && currentUser != null) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    val bloodRed = Color(0xFFD32F2F) // Rich, vibrant blood red
    val bloodRedDark = Color(0xFFB71C1C) // Deeper red for accents

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🧼 Minimal logo
            Image(
                painter = painterResource(id = R.drawable.logo_transparent),
                contentDescription = "Blood For Nepal Logo",
                modifier = Modifier
                    .size(100.dp)
                    .alpha(logoAlpha),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Blood For Nepal",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = bloodRed,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "रक्तदान - जीवनदान",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = bloodRed.copy(alpha = 0.9f),
                modifier = Modifier.alpha(taglineAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Save Lives, Donate Blood",
                fontSize = 14.sp,
                color = bloodRedDark,
                modifier = Modifier.alpha(taglineAlpha)
            )

            Spacer(modifier = Modifier.height(40.dp))

            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .alpha(progressAlpha),
                strokeWidth = 2.dp,
                color = bloodRed
            )
        }

        // Footer
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .alpha(titleAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Version 1.0.0",
                fontSize = 12.sp,
                color = bloodRed.copy(alpha = 0.8f)
            )
            Text(
                text = "© 2025 Zwicky Technology",
                fontSize = 12.sp,
                color = bloodRed.copy(alpha = 0.7f)
            )
            Text(
                text = "Made by Anand KanishkZ",
                fontSize = 12.sp,
                color = bloodRed.copy(alpha = 0.7f)
            )
        }
    }
}
