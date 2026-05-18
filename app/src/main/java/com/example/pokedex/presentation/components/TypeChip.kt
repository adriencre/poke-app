package com.example.pokedex.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.presentation.theme.PokemonTypeColors

/**
 * Chip coloré affichant le type d'un Pokémon.
 * La couleur de fond correspond à la couleur officielle du type.
 */
@Composable
fun TypeChip(
    type: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = type.replaceFirstChar { it.uppercase() },
        color = PokemonTypeColors.getOnTypeColor(type),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PokemonTypeColors.getColor(type))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}
