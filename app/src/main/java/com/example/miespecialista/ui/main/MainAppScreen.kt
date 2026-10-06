package com.example.miespecialista.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miespecialista.ui.auth.UserAccount
import com.example.miespecialista.ui.theme.Ambar
import com.example.miespecialista.ui.theme.AzulProfundo
import com.example.miespecialista.ui.theme.Blanco
import com.example.miespecialista.ui.theme.GrisSuave
import com.example.miespecialista.ui.theme.GrisTexto
import com.example.miespecialista.ui.theme.MiEspecialistaTheme
import com.example.miespecialista.ui.theme.Turquesa

// Enum para la barra de navegación con iconos Rellenos (Filled) y Lineales (Outlined)
enum class BottomTab(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    INICIO(
        title = "Inicio",
        filledIcon = Icons.Filled.Home,
        outlinedIcon = Icons.Outlined.Home
    ),
    FAVORITOS(
        title = "Favoritos",
        filledIcon = Icons.Filled.Favorite,
        outlinedIcon = Icons.Outlined.FavoriteBorder
    ),
    SERVICIOS(
        title = "Mis Servicios",
        filledIcon = Icons.Filled.Handyman,
        outlinedIcon = Icons.Outlined.Handyman
    ),
    SOPORTE(
        title = "Soporte",
        filledIcon = Icons.Filled.HeadsetMic,
        outlinedIcon = Icons.Outlined.HeadsetMic
    ),
    PERFIL(
        title = "Perfil",
        filledIcon = Icons.Filled.Person,
        outlinedIcon = Icons.Outlined.Person
    )
}

@Composable
fun MainAppScreen(
    user: UserAccount,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(BottomTab.INICIO) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Blanco,
                tonalElevation = 8.dp,
                modifier = Modifier.border(1.dp, GrisSuave, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                BottomTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                                contentDescription = tab.title,
                                tint = if (isSelected) AzulProfundo else GrisTexto,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AzulProfundo else GrisTexto
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Turquesa.copy(alpha = 0.25f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentTab) {
                BottomTab.INICIO -> InicioTabScreen(user = user)
                BottomTab.FAVORITOS -> FavoritosTabScreen()
                BottomTab.SERVICIOS -> ServiciosTabScreen()
                BottomTab.SOPORTE -> SoporteTabScreen()
                BottomTab.PERFIL -> PerfilTabScreen(user = user, onLogout = onLogout)
            }
        }
    }
}

@Composable
fun InicioTabScreen(user: UserAccount) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Hero Card de Bienvenida para contratar técnicos y especialistas
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AzulProfundo)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "¡Hola, ${user.fullName}! 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Blanco
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "¿Qué técnico o especialista del hogar u oficina necesitas hoy?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blanco.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Barra de Búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar electricista, plomero, pintor, carpintero...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar",
                    tint = AzulProfundo
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Turquesa,
                unfocusedBorderColor = GrisSuave,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Categorías rápidas de oficios
        Text(
            text = "Oficios y Especialidades",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem(symbol = "⚡", label = "Electricidad")
            CategoryItem(symbol = "🪚", label = "Carpintería")
            CategoryItem(symbol = "🎨", label = "Pintura")
            CategoryItem(symbol = "🚰", label = "Plomería")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem(symbol = "🔑", label = "Cerrajería")
            CategoryItem(symbol = "❄️", label = "Aire Acond.")
            CategoryItem(symbol = "🚗", label = "Mecánica")
            CategoryItem(symbol = "🏡", label = "Jardinería")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Especialistas recomendados
        Text(
            text = "Especialistas Destacados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(12.dp))

        SpecialistCard(
            avatarSymbol = "👷‍♂️",
            name = "Roberto Gómez",
            specialty = "Electricista Certificado",
            rating = "4.9 (120 trabajos)",
            price = "$25 / hora"
        )
        Spacer(modifier = Modifier.height(12.dp))
        SpecialistCard(
            avatarSymbol = "🪚",
            name = "Marcos Silva",
            specialty = "Carpintero & Mueblista",
            rating = "4.8 (98 trabajos)",
            price = "$30 / hora"
        )
        Spacer(modifier = Modifier.height(12.dp))
        SpecialistCard(
            avatarSymbol = "🎨",
            name = "Dora Martínez",
            specialty = "Pintura e Impermeabilización",
            rating = "5.0 (150 trabajos)",
            price = "$20 / hora"
        )
    }
}

@Composable
fun CategoryItem(symbol: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GrisSuave)
            .padding(10.dp)
            .width(72.dp)
    ) {
        Text(text = symbol, fontSize = 26.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo,
            textAlign = TextAlign.Center,
            fontSize = 11.sp
        )
    }
}

@Composable
fun SpecialistCard(
    avatarSymbol: String,
    name: String,
    specialty: String,
    rating: String,
    price: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GrisSuave, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Turquesa.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(avatarSymbol, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulProfundo
                )
                Text(
                    text = specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Turquesa,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Puntuación",
                        tint = Ambar,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$rating • $price",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Contratar", color = Blanco, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FavoritosTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Mis Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "Especialistas y técnicos guardados",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(20.dp))

        SpecialistCard(
            avatarSymbol = "🚰",
            name = "Luis Hernández",
            specialty = "Plomería & Fontanería",
            rating = "4.9 (85 trabajos)",
            price = "$28 / hora"
        )
        Spacer(modifier = Modifier.height(12.dp))
        SpecialistCard(
            avatarSymbol = "🪚",
            name = "Marcos Silva",
            specialty = "Carpintero & Mueblista",
            rating = "4.8 (98 trabajos)",
            price = "$30 / hora"
        )
    }
}

@Composable
fun ServiciosTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Mis Servicios Contratados",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "Historial de trabajos solicitados y visitas técnicas",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(20.dp))

        ServiceStatusCard(
            specialistName = "Roberto Gómez",
            serviceTitle = "Instalación de Iluminación y Tablero",
            category = "Electricidad",
            date = "Mañana, 10:30 AM",
            status = "Confirmado",
            statusColor = Turquesa
        )

        Spacer(modifier = Modifier.height(12.dp))

        ServiceStatusCard(
            specialistName = "Luis Hernández",
            serviceTitle = "Reparación de Fuga de Agua e Inodoro",
            category = "Plomería",
            date = "Ayer, 3:00 PM",
            status = "Completado",
            statusColor = AzulProfundo
        )
    }
}

@Composable
fun ServiceStatusCard(
    specialistName: String,
    serviceTitle: String,
    category: String,
    date: String,
    status: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GrisSuave, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = serviceTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulProfundo,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = status,
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Técnico especialista: $specialistName ($category)",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )
            Text(
                text = "📅 $date",
                style = MaterialTheme.typography.bodySmall,
                color = AzulProfundo,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SoporteTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Centro de Soporte",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "¿Tienes alguna duda sobre tus cotizaciones o contratos?",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(20.dp))

        SupportOptionItem(
            icon = Icons.Filled.QuestionAnswer,
            title = "Chat de Ayuda y Cotizaciones",
            subtitle = "Resuelve tus dudas sobre presupuestos y visitas"
        )
        Spacer(modifier = Modifier.height(10.dp))
        SupportOptionItem(
            icon = Icons.Filled.Shield,
            title = "Garantía de Servicio Mi Especialista",
            subtitle = "Protección y respaldo ante imprevistos o fallas"
        )
        Spacer(modifier = Modifier.height(10.dp))
        SupportOptionItem(
            icon = Icons.Filled.SupportAgent,
            title = "Llamada a Atención al Cliente",
            subtitle = "Soporte telefónico inmediato 24/7"
        )
    }
}

@Composable
fun SupportOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .border(1.dp, GrisSuave, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Turquesa.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = AzulProfundo,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulProfundo
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
            }
        }
    }
}

@Composable
fun PerfilTabScreen(
    user: UserAccount,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Turquesa),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Avatar",
                tint = Blanco,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = user.fullName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Opciones de configuración de la cuenta
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GrisSuave, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ProfileMenuRow(icon = Icons.Filled.Person, title = "Editar Datos de Contacto")
                ProfileMenuRow(icon = Icons.Filled.HomeWork, title = "Mis Direcciones Guardadas (Hogar/Oficina)")
                ProfileMenuRow(icon = Icons.Filled.Notifications, title = "Notificaciones de Visitas Técnicas")
                ProfileMenuRow(icon = Icons.Filled.Payment, title = "Métodos de Pago y Facturación")
                ProfileMenuRow(icon = Icons.Filled.Lock, title = "Seguridad y Contraseña")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                color = AzulProfundo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileMenuRow(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Turquesa,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = AzulProfundo,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = GrisTexto,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppScreenPreview() {
    MiEspecialistaTheme {
        MainAppScreen(
            user = UserAccount("Carlos Rodríguez", "carlos@cliente.com", "Password123!"),
            onLogout = {}
        )
    }
}
