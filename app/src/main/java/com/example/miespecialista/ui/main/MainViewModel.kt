package com.example.miespecialista.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miespecialista.data.model.ServiceJob
import com.example.miespecialista.data.model.SpecialistData
import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.model.WalletTransaction
import com.example.miespecialista.data.repository.SpecialistRepository
import kotlinx.coroutines.launch

// ViewModel para gestionar la lógica de presentación y filtrado (MVVM)
class MainViewModel(
    private val specialistRepository: SpecialistRepository = SpecialistRepository()
) : ViewModel() {

    var currentTab by mutableStateOf(BottomTab.INICIO)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var selectedFilter by mutableStateOf("Todos")
        private set

    var selectedSpecialistForDetail by mutableStateOf<SpecialistData?>(null)
        private set

    // Subpantallas dentro de Perfil (Billetera, Soporte, Editar)
    var profileSubScreen by mutableStateOf<ProfileSubScreen?>(null)
        private set

    // Estado para edición de perfil
    var editFullName by mutableStateOf("")
        private set
    var editEmail by mutableStateOf("")
        private set
    var editPhone by mutableStateOf("")
        private set
    var editAddress by mutableStateOf("")
        private set

    var profileErrorMessage by mutableStateOf<String?>(null)
        private set
    var profileSuccessMessage by mutableStateOf<String?>(null)
        private set

    // Estado del Chat
    var chatInputText by mutableStateOf("")
        private set
    var chatMessages by mutableStateOf(
        listOf(
            Pair(false, "¡Hola! Buenas tardes, ¿en qué puedo ayudarte con tu proyecto u oficio?"),
            Pair(true, "Hola Roberto, necesito reparar un cortocircuito en el tablero principal."),
            Pair(false, "Claro que sí, puedo ir mañana a las 10:30 AM para revisar y darte el presupuesto exacto.")
        )
    )
        private set

    // Billetera y transacciones
    var walletBalance by mutableDoubleStateOf(120.50)
        private set

    var apiSpecialistsList by mutableStateOf<List<SpecialistData>>(specialistRepository.getLocalSpecialists())
        private set

    init {
        fetchRemoteSpecialists()
    }

    fun fetchRemoteSpecialists() {
        viewModelScope.launch {
            apiSpecialistsList = specialistRepository.getSpecialists(searchQuery.ifBlank { null })
        }
    }

    val specialists: List<SpecialistData>
        get() = apiSpecialistsList.ifEmpty { specialistRepository.getLocalSpecialists() }

    // Lógica del filtro de búsqueda de especialistas funcional
    val filteredSpecialists: List<SpecialistData>
        get() {
            var list = specialists

            // 1. Filtrado por texto en nombre o especialidad
            if (searchQuery.isNotBlank()) {
                val query = searchQuery.trim().lowercase()
                list = list.filter {
                    it.name.lowercase().contains(query) ||
                    it.specialty.lowercase().contains(query)
                }
            }

            // 2. Filtrado y ordenamiento según el chip seleccionado
            return when (selectedFilter) {
                "Cercanía" -> list.sortedBy {
                    it.distance.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
                }
                "Calificación" -> list.sortedByDescending {
                    it.rating.substringBefore(" ").toDoubleOrNull() ?: 0.0
                }
                "Tarifa" -> list.sortedBy {
                    it.hourlyPrice.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
                }
                else -> list
            }
        }

    val services: List<ServiceJob>
        get() = specialistRepository.getServices()

    val transactions: List<WalletTransaction>
        get() = specialistRepository.getTransactions()

    fun selectTab(tab: BottomTab) {
        currentTab = tab
        selectedSpecialistForDetail = null
        profileSubScreen = null
        profileSuccessMessage = null
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        fetchRemoteSpecialists()
    }

    fun updateFilter(filter: String) {
        selectedFilter = filter
    }

    fun selectSpecialist(specialist: SpecialistData?) {
        selectedSpecialistForDetail = specialist
    }

    fun openProfileSubScreen(subScreen: ProfileSubScreen) {
        profileSubScreen = subScreen
    }

    fun closeProfileSubScreen() {
        profileSubScreen = null
    }

    fun startEditingProfile(user: UserAccount) {
        editFullName = user.fullName
        editEmail = user.email
        editPhone = user.phone
        editAddress = user.address
        profileErrorMessage = null
        profileSuccessMessage = null
        profileSubScreen = ProfileSubScreen.EDIT_PROFILE
    }

    fun updateEditFullName(name: String) {
        editFullName = name
        profileErrorMessage = null
    }

    fun updateEditEmail(email: String) {
        editEmail = email
        profileErrorMessage = null
    }

    fun updateEditPhone(phone: String) {
        editPhone = phone
        profileErrorMessage = null
    }

    fun updateEditAddress(address: String) {
        editAddress = address
        profileErrorMessage = null
    }

    fun cancelEditingProfile() {
        profileSubScreen = null
        profileErrorMessage = null
    }

    fun saveProfileChanges(
        currentUser: UserAccount,
        onUpdateUser: (UserAccount) -> Unit
    ) {
        when {
            editFullName.isBlank() -> {
                profileErrorMessage = "El nombre no puede estar vacío."
            }
            editEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.trim()).matches() -> {
                profileErrorMessage = "Ingresa un correo electrónico válido."
            }
            editPhone.isBlank() -> {
                profileErrorMessage = "Ingresa un número de teléfono válido."
            }
            editAddress.isBlank() -> {
                profileErrorMessage = "Ingresa tu dirección principal."
            }
            else -> {
                profileErrorMessage = null
                val updated = currentUser.copy(
                    fullName = editFullName.trim(),
                    email = editEmail.trim(),
                    phone = editPhone.trim(),
                    address = editAddress.trim()
                )
                onUpdateUser(updated)
                profileSubScreen = null
                profileSuccessMessage = "¡Perfil actualizado exitosamente!"
            }
        }
    }

    fun updateChatInputText(text: String) {
        chatInputText = text
    }

    fun sendChatMessage() {
        if (chatInputText.isNotBlank()) {
            chatMessages = chatMessages + Pair(true, chatInputText.trim())
            chatInputText = ""
        }
    }
}

enum class ProfileSubScreen {
    EDIT_PROFILE,
    WALLET,
    SUPPORT
}
