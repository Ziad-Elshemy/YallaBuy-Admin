package eg.gov.iti.yallabuyadmin.ui.navigation


import androidx.compose.ui.graphics.painter.Painter

data class NavigationItem(
    val title: String,
    val icon: Painter,
    val route: String
)
