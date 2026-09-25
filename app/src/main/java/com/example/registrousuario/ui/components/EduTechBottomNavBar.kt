package com.example.registrousuario.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.registrousuario.ui.theme.PurplePrimary
import com.example.registrousuario.ui.theme.SurfaceWhite
import com.example.registrousuario.ui.theme.TextMuted

enum class NavTab(val title: String, val icon: ImageVector) {
    HOME("Principal", Icons.Default.Home),
    FAVORITES("Favoritos", Icons.Default.Star),
    PROFILE("Perfil", Icons.Default.Person)
}

@Composable
fun EduTechBottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceWhite
    ) {
        NavTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = { Text(tab.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PurplePrimary,
                    selectedTextColor = PurplePrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}