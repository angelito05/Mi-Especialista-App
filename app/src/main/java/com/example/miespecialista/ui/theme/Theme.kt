package com.example.miespecialista.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AzulProfundo,
    onPrimary = Blanco,
    primaryContainer = TurquesaClaro,
    onPrimaryContainer = AzulProfundoOscuro,
    secondary = Turquesa,
    onSecondary = Blanco,
    secondaryContainer = TurquesaClaro,
    onSecondaryContainer = AzulProfundo,
    tertiary = Ambar,
    onTertiary = Blanco,
    background = Blanco,
    onBackground = AzulProfundo,
    surface = Blanco,
    onSurface = AzulProfundo,
    surfaceVariant = GrisSuave,
    onSurfaceVariant = GrisTexto,
    outline = Turquesa
)

private val DarkColorScheme = darkColorScheme(
    primary = Turquesa,
    onPrimary = AzulProfundoOscuro,
    primaryContainer = AzulProfundo,
    onPrimaryContainer = Blanco,
    secondary = Turquesa,
    onSecondary = AzulProfundoOscuro,
    tertiary = Ambar,
    onTertiary = AzulProfundoOscuro,
    background = AzulProfundoOscuro,
    onBackground = Blanco,
    surface = AzulProfundo,
    onSurface = Blanco,
    surfaceVariant = GrisOscuro,
    onSurfaceVariant = GrisSuave,
    outline = Turquesa
)

@Composable
fun MiEspecialistaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
