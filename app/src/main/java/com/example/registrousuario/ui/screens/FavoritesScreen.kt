package com.example.registrousuario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrousuario.data.FavoriteSubject
import com.example.registrousuario.ui.components.EduTechBottomNavBar
import com.example.registrousuario.ui.components.NavTab
import com.example.registrousuario.ui.theme.BackgroundGray
import com.example.registrousuario.ui.theme.BlueAccent
import com.example.registrousuario.ui.theme.PurpleDark
import com.example.registrousuario.ui.theme.PurplePrimary
import com.example.registrousuario.ui.theme.PurpleSecondary
import com.example.registrousuario.ui.theme.RedAccent
import com.example.registrousuario.ui.theme.SurfaceWhite
import com.example.registrousuario.ui.theme.TextDark
import com.example.registrousuario.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoritesList: List<FavoriteSubject>,
    onAddFavorite: (nombre: String, descripcion: String, emoji: String) -> Unit,
    onUpdateFavorite: (FavoriteSubject) -> Unit,
    onDeleteFavorite: (FavoriteSubject) -> Unit,
    onTabSelected: (NavTab) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var favoriteToEdit by remember { mutableStateOf<FavoriteSubject?>(null) }
    var favoriteToDelete by remember { mutableStateOf<FavoriteSubject?>(null) }

    Scaffold(
        bottomBar = {
            EduTechBottomNavBar(
                selectedTab = NavTab.FAVORITES,
                onTabSelected = onTabSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PurplePrimary,
                contentColor = SurfaceWhite
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar materia favorita")
            }
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
                        text = "⭐ Materias Favoritas",
                        color = SurfaceWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Organiza y consulta tus materias preferidas",
                        color = PurpleSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            if (favoritesList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📚", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes materias favoritas aún",
                            color = TextDark,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Presiona el botón '+' para agregar una nueva materia a tus favoritos.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritesList, key = { it.id }) { favorite ->
                        FavoriteItemCard(
                            favorite = favorite,
                            onEdit = { favoriteToEdit = favorite },
                            onDelete = { favoriteToDelete = favorite }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        FavoriteFormDialog(
            title = "Agregar Materia Favorita",
            initialName = "",
            initialDescription = "",
            initialEmoji = "⭐",
            confirmText = "Agregar",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, desc, emoji ->
                onAddFavorite(name, desc, emoji)
                showAddDialog = false
            }
        )
    }

    favoriteToEdit?.let { favorite ->
        FavoriteFormDialog(
            title = "Editar Materia Favorita",
            initialName = favorite.nombre,
            initialDescription = favorite.descripcion,
            initialEmoji = favorite.emoji,
            confirmText = "Guardar",
            onDismiss = { favoriteToEdit = null },
            onConfirm = { name, desc, emoji ->
                onUpdateFavorite(favorite.copy(nombre = name, descripcion = desc, emoji = emoji))
                favoriteToEdit = null
            }
        )
    }

    favoriteToDelete?.let { favorite ->
        AlertDialog(
            onDismissRequest = { favoriteToDelete = null },
            title = { Text("Eliminar materia") },
            text = { Text("¿Estás seguro de eliminar '${favorite.nombre}' de tus favoritos?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteFavorite(favorite)
                        favoriteToDelete = null
                    }
                ) {
                    Text("Eliminar", color = RedAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { favoriteToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun FavoriteItemCard(
    favorite: FavoriteSubject,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                    .clip(CircleShape)
                    .background(PurpleSecondary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = favorite.emoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = favorite.nombre,
                    color = TextDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (favorite.descripcion.isBlank()) "Sin notas o descripción" else favorite.descripcion,
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = BlueAccent
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = RedAccent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteFormDialog(
    title: String,
    initialName: String,
    initialDescription: String,
    initialEmoji: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, emoji: String) -> Unit
) {
    val predefinedSubjects = remember {
        listOf(
            "Matemáticas",
            "Ciencias",
            "Lectura",
            "Arte",
            "Historia",
            "Música",
            "Geografía",
            "Computación",
            "Idiomas",
            "Educación Física"
        )
    }

    val emojiMap = remember {
        mapOf(
            "Matemáticas" to "➗",
            "Ciencias" to "🔬",
            "Lectura" to "📚",
            "Arte" to "🎨",
            "Historia" to "🌎",
            "Música" to "🎵",
            "Geografía" to "🌎",
            "Computación" to "💻",
            "Idiomas" to "🧠",
            "Educación Física" to "🚀"
        )
    }

    val emojisAvailable = remember {
        listOf("⭐", "➗", "🔬", "📚", "🎨", "🌎", "🎵", "💻", "🧠", "📐", "🧪", "🚀")
    }

    var selectedName by remember {
        mutableStateOf(if (initialName.isNotEmpty()) initialName else predefinedSubjects[0])
    }
    var description by remember { mutableStateOf(initialDescription) }
    var selectedEmoji by remember { mutableStateOf(initialEmoji) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selecciona una materia") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        predefinedSubjects.forEach { subject ->
                            DropdownMenuItem(
                                text = { Text(subject) },
                                onClick = {
                                    selectedName = subject
                                    isDropdownExpanded = false
                                    emojiMap[subject]?.let { suggested ->
                                        selectedEmoji = suggested
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Notas o descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Selecciona un ícono:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    emojisAvailable.forEach { emoji ->
                        val isSelected = emoji == selectedEmoji
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PurpleSecondary else SurfaceWhite)
                                .clickable { selectedEmoji = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 22.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedName.isNotBlank()) {
                        onConfirm(selectedName, description, selectedEmoji)
                    }
                }
            ) {
                Text(confirmText, fontWeight = FontWeight.Bold, color = PurplePrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}