package com.example.miespecialista.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miespecialista.R
import com.example.miespecialista.data.model.ServiceJob
import com.example.miespecialista.data.model.SpecialistData
import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.model.WalletTransaction
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
    BUSQUEDA(
        title = "Búsqueda",
        filledIcon = Icons.Filled.Search,
        outlinedIcon = Icons.Outlined.Search
    ),
    SEGUIMIENTO(
        title = "Seguimiento",
        filledIcon = Icons.Filled.Timeline,
        outlinedIcon = Icons.Outlined.Timeline
    ),
    MENSAJES(
        title = "Mensajes",
        filledIcon = Icons.Filled.Chat,
        outlinedIcon = Icons.Outlined.Chat
    ),
    BILLETERA(
        title = "Billetera",
        filledIcon = Icons.Filled.AccountBalanceWallet,
        outlinedIcon = Icons.Outlined.AccountBalanceWallet
    ),
    SOPORTE(
        title = "Soporte",
        filledIcon = Icons.Filled.SupportAgent,
        outlinedIcon = Icons.Outlined.SupportAgent
    )
}

@Composable
fun MainAppScreen(
    user: UserAccount,
    onLogout: () -> Unit,
    onUpdateUser: (UserAccount) -> Unit,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    val selectedSpecialist = mainViewModel.selectedSpecialistForDetail

    if (selectedSpecialist != null) {
        // Muestra la pantalla completa de Perfil del Especialista
        SpecialistProfileScreen(
            specialist = selectedSpecialist,
            onBack = { mainViewModel.selectSpecialist(null) },
            onRequestService = {
                mainViewModel.selectSpecialist(null)
                mainViewModel.selectTab(BottomTab.SEGUIMIENTO)
            },
            onSendMessage = {
                mainViewModel.selectSpecialist(null)
                mainViewModel.selectTab(BottomTab.MENSAJES)
            }
        )
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = Blanco,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(1.dp, GrisSuave, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    BottomTab.entries.forEach { tab ->
                        val isSelected = mainViewModel.currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { mainViewModel.selectTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) AzulProfundo else GrisTexto,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AzulProfundo else GrisTexto,
                                    fontSize = 11.sp
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
                    .background(Blanco)
            ) {
                when (mainViewModel.currentTab) {
                    BottomTab.INICIO -> InicioTabScreen(
                        user = user,
                        viewModel = mainViewModel
                    )
                    BottomTab.BUSQUEDA -> BusquedaTabScreen(
                        viewModel = mainViewModel
                    )
                    BottomTab.SEGUIMIENTO -> SeguimientoTabScreen()
                    BottomTab.MENSAJES -> MensajeriaTabScreen(viewModel = mainViewModel)
                    BottomTab.BILLETERA -> BilleteraTabScreen(viewModel = mainViewModel)
                    BottomTab.SOPORTE -> SoporteTabScreen(
                        user = user,
                        onLogout = onLogout,
                        onUpdateUser = onUpdateUser,
                        viewModel = mainViewModel
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------
// 1. PANTALLA INICIO
// ----------------------------------------------------------------------------
@Composable
fun InicioTabScreen(
    user: UserAccount,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Logo Oficial de la Marca en el Header
        Image(
            painter = painterResource(id = R.drawable.code_generated_image__1_),
            contentDescription = "Logo Mi Especialista",
            modifier = Modifier
                .height(38.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Saludo personalizado
        Text(
            text = "¡Hola, ${user.fullName}! 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "¿Qué servicio técnico u oficio necesitas hoy?",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Barra de Búsqueda
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text("Buscar electricista, plomero, pintor...") },
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

        Spacer(modifier = Modifier.height(20.dp))

        // Categorías rápidas de oficios
        Text(
            text = "Categorías Principales",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryItem(symbol = "⚡", label = "Electricidad")
            CategoryItem(symbol = "🪚", label = "Carpintería")
            CategoryItem(symbol = "🎨", label = "Pintura")
            CategoryItem(symbol = "🚰", label = "Plomería")
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Bloque promocional
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AzulProfundo)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "15% OFF en tu primera reparación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Blanco
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aplica con especialistas verificados contratados hoy",
                        style = MaterialTheme.typography.bodySmall,
                        color = Blanco.copy(alpha = 0.85f)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Ambar)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "PROMO15",
                        color = AzulProfundo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjetas de especialistas destacados
        Text(
            text = "Especialistas Destacados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(12.dp))

        viewModel.specialists.take(3).forEach { specialist ->
            SpecialistCardItem(
                specialist = specialist,
                onSelect = { viewModel.selectSpecialist(specialist) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
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
fun SpecialistCardItem(
    specialist: SpecialistData,
    onSelect: () -> Unit
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
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Turquesa.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(specialist.avatarSymbol, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = specialist.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AzulProfundo
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Verificado",
                        tint = Turquesa,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = specialist.specialty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Turquesa,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${specialist.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Ambar,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "📍 ${specialist.distance}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
            }

            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Ver perfil", color = Blanco, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ----------------------------------------------------------------------------
// 2. PANTALLA BÚSQUEDA Y FILTROS
// ----------------------------------------------------------------------------
@Composable
fun BusquedaTabScreen(
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Directorio de Especialistas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "Filtra por cercanía, evaluación o tarifa",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Cercanía", "Calificación", "Tarifa").forEach { filter ->
                val isSelected = viewModel.selectedFilter == filter
                Card(
                    modifier = Modifier
                        .clickable { viewModel.updateFilter(filter) }
                        .border(1.dp, if (isSelected) Turquesa else GrisSuave, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Turquesa.copy(alpha = 0.15f) else Blanco
                    )
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) AzulProfundo else GrisTexto,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            viewModel.specialists.forEach { specialist ->
                SpecialistCardItem(
                    specialist = specialist,
                    onSelect = { viewModel.selectSpecialist(specialist) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ----------------------------------------------------------------------------
// 3. PANTALLA PERFIL DEL ESPECIALISTA (DETALLE COMPLETO)
// ----------------------------------------------------------------------------
@Composable
fun SpecialistProfileScreen(
    specialist: SpecialistData,
    onBack: () -> Unit,
    onRequestService: () -> Unit,
    onSendMessage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Botón regresar
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = AzulProfundo
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Header Avatar + Nombre + Insignia
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Turquesa.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(specialist.avatarSymbol, fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = specialist.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AzulProfundo
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = "Insignia de verificación",
                    tint = Turquesa,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = specialist.specialty,
                style = MaterialTheme.typography.titleSmall,
                color = Turquesa,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${specialist.rating} • ${specialist.distance}",
                style = MaterialTheme.typography.bodyMedium,
                color = Ambar,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Descripción profesional
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = GrisSuave.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sobre el especialista:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AzulProfundo
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = specialist.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Galería de trabajos realizados
        Text(
            text = "Galería de Proyectos Realizados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("🏠 Trabajo en casa", "🔧 Reparación", "⚡ Instalación").forEach { work ->
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, GrisSuave, RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = GrisSuave)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = work,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulProfundo,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Botones amplios para acción
        Button(
            onClick = onRequestService,
            colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Solicitar servicio",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Blanco
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onSendMessage,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Enviar mensaje",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AzulProfundo
            )
        }
    }
}

// ----------------------------------------------------------------------------
// 4. PANTALLA MENSAJERÍA (CHAT INTERACTIVO MVVM)
// ----------------------------------------------------------------------------
@Composable
fun MensajeriaTabScreen(
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header del chat
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = GrisSuave.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Turquesa.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👷‍♂️", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Roberto Gómez",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AzulProfundo
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Turquesa)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "En línea",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista de globos de texto
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            viewModel.chatMessages.forEach { (isFromMe, text) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = if (isFromMe) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (isFromMe) 12.dp else 2.dp,
                            bottomEnd = if (isFromMe) 2.dp else 12.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFromMe) AzulProfundo else GrisSuave
                        ),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(
                            text = text,
                            color = if (isFromMe) Blanco else AzulProfundo,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de entrada de mensaje
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.AttachFile,
                    contentDescription = "Adjuntar imagen",
                    tint = AzulProfundo
                )
            }

            OutlinedTextField(
                value = viewModel.chatInputText,
                onValueChange = { viewModel.updateChatInputText(it) },
                placeholder = { Text("Escribe un mensaje...") },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = { viewModel.sendChatMessage() }
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AzulProfundo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Blanco,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------
// 5. PANTALLA SEGUIMIENTO Y TIMELINE DE SERVICIO
// ----------------------------------------------------------------------------
@Composable
fun SeguimientoTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Seguimiento del Servicio",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "Estado en tiempo real de tu solicitud",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mapa Simulado y Hora estimada
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GrisSuave, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = GrisSuave.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ubicación",
                        tint = Turquesa
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Dirección: Av. Insurgentes Sur 120, Coyoacán",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = AzulProfundo
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Turquesa.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Map,
                            contentDescription = "Mapa simulado",
                            tint = AzulProfundo,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "Mapa de rastreo en vivo",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = AzulProfundo
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "⏱️ Llegada estimada: 10:25 AM",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = AzulProfundo
                    )
                    Text(
                        text = "Costo estimado: $25.00",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Turquesa
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Timeline Vertical de Estado
        Text(
            text = "Línea de Tiempo del Servicio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(12.dp))

        TimelineStepItem(
            stepNumber = "1",
            title = "Servicio Solicitado y Confirmado",
            subtitle = "Tu solicitud fue aceptada por Roberto Gómez",
            isCompleted = true,
            isCurrent = false
        )
        TimelineStepItem(
            stepNumber = "2",
            title = "Especialista en camino",
            subtitle = "El técnico se dirige a la ubicación acordada",
            isCompleted = false,
            isCurrent = true
        )
        TimelineStepItem(
            stepNumber = "3",
            title = "Trabajo en proceso",
            subtitle = "Inspección y ejecución de la reparación",
            isCompleted = false,
            isCurrent = false
        )
        TimelineStepItem(
            stepNumber = "4",
            title = "Servicio Finalizado y Pago",
            subtitle = "Liberación del pago con garantía",
            isCompleted = false,
            isCurrent = false
        )
    }
}

@Composable
fun TimelineStepItem(
    stepNumber: String,
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> Turquesa
                        isCurrent -> AzulProfundo
                        else -> GrisSuave
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isCompleted) "✓" else stepNumber,
                color = if (isCompleted || isCurrent) Blanco else GrisTexto,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent || isCompleted) AzulProfundo else GrisTexto
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
        }
    }
}

// ----------------------------------------------------------------------------
// 6. PANTALLA BILLETERA DIGITAL
// ----------------------------------------------------------------------------
@Composable
fun BilleteraTabScreen(
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Billetera Digital",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )
        Text(
            text = "Gestiona tu saldo y pagos seguros",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Card de Saldo Disponible
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AzulProfundo)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Saldo Disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blanco.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$${String.format("%.2f", viewModel.walletBalance)} USD",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Blanco
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Turquesa),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar", tint = Blanco)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar Fondos", color = Blanco, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Retirar", color = Blanco, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reglas de comisión
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = GrisSuave.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = "Protección",
                    tint = Turquesa
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Transacciones protegidas con seguro de protección y comisión del 5%.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AzulProfundo
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Historial de Movimientos
        Text(
            text = "Historial de Movimientos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo
        )

        Spacer(modifier = Modifier.height(10.dp))

        viewModel.transactions.forEach { tx ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, GrisSuave, RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Blanco)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = tx.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AzulProfundo
                        )
                        Text(
                            text = tx.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }
                    Text(
                        text = tx.amount,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (tx.isNegative) AzulProfundo else Turquesa
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------
// 7. PANTALLA PERFIL Y EDITAR PERFIL (FUNCIONAL CON MVVM)
// ----------------------------------------------------------------------------
@Composable
fun SoporteTabScreen(
    user: UserAccount,
    onLogout: () -> Unit,
    onUpdateUser: (UserAccount) -> Unit,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (viewModel.isEditingProfile) {
            EditProfileView(
                user = user,
                viewModel = viewModel,
                onUpdateUser = onUpdateUser
            )
        } else {
            ProfileDetailsView(
                user = user,
                viewModel = viewModel,
                onLogout = onLogout
            )
        }
    }
}

@Composable
fun ProfileDetailsView(
    user: UserAccount,
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulProfundo,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Notificación de éxito al guardar
        AnimatedVisibility(visible = viewModel.profileSuccessMessage != null) {
            viewModel.profileSuccessMessage?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Turquesa.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = it,
                        color = AzulProfundo,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "📞 ${user.phone} • 📍 ${user.address}",
            style = MaterialTheme.typography.bodySmall,
            color = AzulProfundo,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Opciones de configuración
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GrisSuave, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ProfileMenuRow(
                    icon = Icons.Filled.Person,
                    title = "Editar Información Personal",
                    onClick = { viewModel.startEditingProfile(user) }
                )
                ProfileMenuRow(
                    icon = Icons.Filled.HomeWork,
                    title = "Mis Direcciones Guardadas",
                    onClick = { }
                )
                ProfileMenuRow(
                    icon = Icons.Filled.Notifications,
                    title = "Notificaciones y Alertas",
                    onClick = { }
                )
                ProfileMenuRow(
                    icon = Icons.Filled.Payment,
                    title = "Métodos de Pago y Facturación",
                    onClick = { }
                )
                ProfileMenuRow(
                    icon = Icons.Filled.Lock,
                    title = "Seguridad y Contraseña",
                    onClick = { }
                )
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
fun EditProfileView(
    user: UserAccount,
    viewModel: MainViewModel,
    onUpdateUser: (UserAccount) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GrisSuave, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editar Información Personal",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AzulProfundo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre Completo
            OutlinedTextField(
                value = viewModel.editFullName,
                onValueChange = { viewModel.updateEditFullName(it) },
                label = { Text("Nombre Completo") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo Electrónico
            OutlinedTextField(
                value = viewModel.editEmail,
                onValueChange = { viewModel.updateEditEmail(it) },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Teléfono
            OutlinedTextField(
                value = viewModel.editPhone,
                onValueChange = { viewModel.updateEditPhone(it) },
                label = { Text("Teléfono de Contacto") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dirección
            OutlinedTextField(
                value = viewModel.editAddress,
                onValueChange = { viewModel.updateEditAddress(it) },
                label = { Text("Dirección Principal") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = viewModel.profileErrorMessage != null) {
                viewModel.profileErrorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.saveProfileChanges(user, onUpdateUser)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar Cambios", color = Blanco, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.cancelEditingProfile() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar", color = AzulProfundo)
            }
        }
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
            onLogout = {},
            onUpdateUser = {}
        )
    }
}
