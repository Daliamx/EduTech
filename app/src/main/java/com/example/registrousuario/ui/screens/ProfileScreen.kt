package com.example.registrousuario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrousuario.data.User
import com.example.registrousuario.ui.components.EduTechBottomNavBar
import com.example.registrousuario.ui.components.NavTab
import com.example.registrousuario.ui.theme.BackgroundGray
import com.example.registrousuario.ui.theme.PurpleDark
import com.example.registrousuario.ui.theme.PurplePrimary
import com.example.registrousuario.ui.theme.PurpleSecondary
import com.example.registrousuario.ui.theme.RedAccent
import com.example.registrousuario.ui.theme.SurfaceWhite

@Composable
fun ProfileScreen(
    user: User?,
    onSaveProfile: (nombre: String, edad: Int, correo: String) -> Unit,
    onLogout: () -> Unit,
    onTabSelected: (NavTab) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var edadStr by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var edadError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user) {
        user?.let {
            nombre = it.nombre
            edadStr = it.edad.toString()
            correo = it.correo
        }
    }

    Scaffold(
        bottomBar = {
            EduTechBottomNavBar(
                selectedTab = NavTab.PROFILE,
                onTabSelected = onTabSelected
            )
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PurplePrimary, PurpleDark)
                        )
                    )
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SurfaceWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", fontSize = 40.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (nombre.isNotEmpty()) nombre else "Mi Perfil",
                        color = SurfaceWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Consulta y edita tus datos",
                        color = PurpleSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            nombreError = null
                        },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        isError = nombreError != null,
                        supportingText = nombreError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = edadStr,
                        onValueChange = {
                            edadStr = it
                            edadError = null
                        },
                        label = { Text("Edad") },
                        isError = edadError != null,
                        supportingText = edadError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            correoError = null
                        },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        isError = correoError != null,
                        supportingText = correoError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            var esValido = true
                            if (nombre.trim().isEmpty()) {
                                nombreError = "Escribe tu nombre"
                                esValido = false
                            }
                            val edad = edadStr.toIntOrNull()
                            if (edad == null || edad <= 0) {
                                edadError = "Ingresa una edad válida"
                                esValido = false
                            }
                            if (correo.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
                                correoError = "Ingresa un correo electrónico válido"
                                esValido = false
                            }

                            if (esValido && edad != null) {
                                onSaveProfile(nombre.trim(), edad, correo.trim())
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) {
                        Text(
                            text = "Guardar cambios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            color = RedAccent,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}