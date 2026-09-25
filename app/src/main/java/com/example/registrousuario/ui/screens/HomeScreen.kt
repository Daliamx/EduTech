package com.example.registrousuario.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrousuario.ui.components.EduTechBottomNavBar
import com.example.registrousuario.ui.components.NavTab
import com.example.registrousuario.ui.theme.BackgroundGray
import com.example.registrousuario.ui.theme.BlueAccent
import com.example.registrousuario.ui.theme.GreenAccent
import com.example.registrousuario.ui.theme.OrangeAccent
import com.example.registrousuario.ui.theme.PinkAccent
import com.example.registrousuario.ui.theme.PurpleDark
import com.example.registrousuario.ui.theme.PurplePrimary
import com.example.registrousuario.ui.theme.PurpleSecondary
import com.example.registrousuario.ui.theme.SurfaceWhite
import com.example.registrousuario.ui.theme.TextDark
import com.example.registrousuario.ui.theme.TextMuted
import com.example.registrousuario.ui.theme.YellowAccent
import java.text.Normalizer

data class SubjectItem(
    val name: String,
    val emoji: String,
    val color: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    userName: String,
    onSubjectClick: (String) -> Unit,
    onAddFavorite: (String) -> Unit,
    onTabSelected: (NavTab) -> Unit
) {
    val allSubjects = remember {
        listOf(
            SubjectItem("Matemáticas", "➗", PurplePrimary),
            SubjectItem("Ciencias", "🔬", GreenAccent),
            SubjectItem("Lectura", "📚", BlueAccent),
            SubjectItem("Arte", "🎨", PinkAccent),
            SubjectItem("Historia", "🌎", OrangeAccent),
            SubjectItem("Música", "🎵", YellowAccent)
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    fun normalize(text: String): String {
        val unaccented = Normalizer.normalize(text, Normalizer.Form.NFD)
        return unaccented.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "").lowercase()
    }

    val filteredSubjects = remember(searchQuery) {
        val queryNorm = normalize(searchQuery)
        allSubjects.filter { normalize(it.name).contains(queryNorm) }
    }

    Scaffold(
        bottomBar = {
            EduTechBottomNavBar(
                selectedTab = NavTab.HOME,
                onTabSelected = onTabSelected
            )
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(PurplePrimary, PurpleDark)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = if (userName.isNotEmpty()) "¡Hola, $userName! 👋" else "¡Buenas tardes!",
                        color = SurfaceWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "¿Qué aventura comenzamos hoy?",
                        color = PurpleSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar materia...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = TextMuted
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    text = "📚 Materias",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (filteredSubjects.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron materias",
                            color = TextMuted,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredSubjects, key = { it.name }) { subject ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .combinedClickable(
                                        onClick = { onSubjectClick(subject.name) },
                                        onLongClick = { onAddFavorite(subject.name) }
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = subject.color)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = subject.emoji,
                                        fontSize = 32.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = subject.name,
                                        color = SurfaceWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}