package com.example.pokedex.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.domain.model.PokemonStat

/**
 * Barre de statistique animée pour un Pokémon.
 * Affiche le nom abrégé, la valeur numérique et une barre de progression colorée.
 * L'animation se déclenche au premier affichage.
 *
 * @param stat La statistique à afficher
 * @param maxStat La valeur maximale pour le calcul du ratio (défaut: 255)
 */
@Composable
fun StatBar(
    stat: PokemonStat,
    maxStat: Int = 255,
    modifier: Modifier = Modifier
) {
    var targetProgress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "stat_progress"
    )

    LaunchedEffect(stat.baseStat) {
        targetProgress = (stat.baseStat.toFloat() / maxStat).coerceIn(0f, 1f)
    }

    val barColor = when {
        stat.baseStat < 50 -> Color(0xFFE94560)   // Rouge — faible
        stat.baseStat < 90 -> Color(0xFFF7D02C)   // Jaune — moyen
        stat.baseStat < 120 -> Color(0xFF7AC74C)  // Vert — bon
        else -> Color(0xFF00C9A7)                  // Cyan — excellent
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Nom de la stat (largeur fixe pour alignement)
        Text(
            text = stat.displayName,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.width(64.dp)
        )

        // Valeur numérique
        Text(
            text = stat.baseStat.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(36.dp)
        )

        // Barre de progression animée
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}
