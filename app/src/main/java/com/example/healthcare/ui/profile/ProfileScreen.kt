package com.example.healthcare.ui.profile

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.healthcare.viewmodel.ProfileViewModel
import com.example.healthcare.viewmodel.ThemeViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- 1. AMBIL STATE TEMA SAAT INI ---
    val currentTheme by themeViewModel.themeState.collectAsState()

    val context = LocalContext.current

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    // --- EFEK TOAST PESAN ---
    LaunchedEffect(uiState.passwordChangeMessage) {
        uiState.passwordChangeMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (message.contains("Sukses")) showChangePasswordDialog = false
            viewModel.clearMessage()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            // --- SET BACKGROUND GLOBAL GRADIENT ---
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer, // Atas
                        MaterialTheme.colorScheme.primaryContainer, // Tengah
                        MaterialTheme.colorScheme.surface           // Bawah
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 18.dp)
        ) {
            // Header Section (Hanya Menampilkan Foto, Tidak Bisa Edit)
            ProfileHeader(
                displayName = uiState.displayName,
                photoUrl = uiState.photoUrl
            )

            // Profile Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Informasi Akun",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                InfoCard(Icons.Outlined.Person, "Username", uiState.displayName)
                Spacer(modifier = Modifier.height(12.dp))
                InfoCard(Icons.Outlined.Email, "Email", uiState.email)
                Spacer(modifier = Modifier.height(12.dp))
                InfoCard(Icons.Outlined.Lock, "Password", "********", isPassword = true)

                Spacer(modifier = Modifier.height(24.dp))

                // --- TOMBOL GANTI TEMA ---
                ThemeChangeButton(onClick = { showThemeDialog = true })

                Spacer(modifier = Modifier.height(16.dp))

                ChangePasswordButton(onClick = { showChangePasswordDialog = true })

                Spacer(modifier = Modifier.height(16.dp))

                LogoutButton(onClick = { showLogoutDialog = true })

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Loading Indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
    }

    // --- DIALOGS ---

    // 1. Dialog Ganti Password
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onConfirm = { old, new -> viewModel.changePassword(old, new) }
        )
    }

    // 2. Dialog Logout
    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = { showLogoutDialog = false; onLogoutClick() }
        )
    }

    // 3. Dialog Ganti Tema
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { mode ->
                themeViewModel.changeTheme(mode)
            }
        )
    }
}

// --- HEADER TRANSPARAN (TANPA EDIT FOTO) ---
@Composable
fun ProfileHeader(
    displayName: String,
    photoUrl: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile Avatar (Static)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                // Ikon edit (pensil) dan clickable sudah dihapus
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hello, $displayName",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// --- INFO CARD ---
@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isPassword: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// --- TOMBOL GANTI TEMA ---
@Composable
fun ThemeChangeButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Icon(Icons.Outlined.DarkMode, null, Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("Ganti Tema Aplikasi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ChangePasswordButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "scale")

    OutlinedButton(
        onClick = { isPressed = true; onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Icon(Icons.Default.Lock, null, Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("Ubah Password", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
    LaunchedEffect(isPressed) { if (isPressed) { kotlinx.coroutines.delay(100); isPressed = false } }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Button(
        onClick = { isPressed = true; onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.error)
    ) {
        Icon(Icons.Default.Logout, null, Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("Keluar dari Akun", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
    LaunchedEffect(isPressed) { if (isPressed) { kotlinx.coroutines.delay(100); isPressed = false } }
}

// --- DIALOG PILIHAN TEMA ---
@Composable
fun ThemeSelectionDialog(
    currentTheme: Int,
    onDismiss: () -> Unit,
    onThemeSelected: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Tema", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                ThemeOption(
                    label = "Ikuti Sistem (Default)",
                    isSelected = currentTheme == 0,
                    onClick = { onThemeSelected(0) }
                )
                ThemeOption(
                    label = "Mode Terang",
                    isSelected = currentTheme == 1,
                    onClick = { onThemeSelected(1) }
                )
                ThemeOption(
                    label = "Mode Gelap",
                    isSelected = currentTheme == 2,
                    onClick = { onThemeSelected(2) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tutup") }
        }
    )
}

@Composable
fun ThemeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

// --- DIALOG CHANGE PASSWORD & LOGOUT ---

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (oldPassword: String, newPassword: String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ubah Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Password Lama") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Password Baru") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Konfirmasi") },
                    visualTransformation = PasswordVisualTransformation()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (newPassword.length < 6) errorMessage = "Password min 6 karakter"
                else if (newPassword != confirmPassword) errorMessage = "Password tidak cocok"
                else onConfirm(oldPassword, newPassword)
            }) { Text("Simpan") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Batal") } }
    )
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Keluar?") },
        text = { Text("Yakin ingin keluar dari aplikasi?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Ya, Keluar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Batal") } }
    )
}