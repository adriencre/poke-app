package com.example.pokedex.presentation.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.pokedex.domain.model.PokemonStat
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun PokemonRadarChart(
    stats: List<PokemonStat>,
    modifier: Modifier = Modifier,
    maxStat: Float = 255f // La stat maximale théorique d'un Pokémon
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = minOf(centerX, centerY) - 40.dp.toPx() // Espace pour le texte

        // Assurons-nous d'avoir 6 stats (même s'il en manque, on prend ce qu'il y a)
        val numStats = maxOf(6, stats.size)
        val angleStep = (2 * PI) / numStats

        // 1. Dessiner la grille (les "toiles d'araignée" hexagonales)
        val numGridLines = 5
        for (i in 1..numGridLines) {
            val currentRadius = radius * (i.toFloat() / numGridLines)
            val gridPath = Path()
            
            for (j in 0 until numStats) {
                // On commence à -PI/2 pour que la première pointe soit en haut
                val angle = j * angleStep - PI / 2
                val x = centerX + currentRadius * cos(angle).toFloat()
                val y = centerY + currentRadius * sin(angle).toFloat()
                
                if (j == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
            }
            gridPath.close()
            drawPath(
                path = gridPath,
                color = gridColor,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // 2. Dessiner les lignes reliant le centre aux sommets de la grille
        for (j in 0 until numStats) {
            val angle = j * angleStep - PI / 2
            val x = centerX + radius * cos(angle).toFloat()
            val y = centerY + radius * sin(angle).toFloat()
            drawLine(
                color = gridColor,
                start = Offset(centerX, centerY),
                end = Offset(x, y),
                strokeWidth = 1.dp.toPx()
            )
            
            // 3. Dessiner les labels (noms des stats)
            val statName = stats.getOrNull(j)?.name?.replace("-", " ")?.uppercase() ?: ""
            val textRadius = radius + 20.dp.toPx()
            val textX = centerX + textRadius * cos(angle).toFloat()
            val textY = centerY + textRadius * sin(angle).toFloat()
            
            // Utiliser le canvas natif pour dessiner le texte (Paint classique Android)
            drawContext.canvas.nativeCanvas.apply {
                val paint = Paint().apply {
                    color = android.graphics.Color.argb(
                        onSurfaceColor.alpha,
                        onSurfaceColor.red,
                        onSurfaceColor.green,
                        onSurfaceColor.blue
                    )
                    textSize = 12.dp.toPx()
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.DEFAULT_BOLD
                }
                // Ajuster légèrement Y pour le centrage vertical du texte
                drawText(statName, textX, textY + (paint.textSize / 3), paint)
            }
        }

        // 4. Dessiner la zone de statistiques du Pokémon
        if (stats.isNotEmpty()) {
            val statPath = Path()
            for (j in 0 until numStats) {
                val angle = j * angleStep - PI / 2
                val statValue = stats.getOrNull(j)?.baseStat?.toFloat() ?: 0f
                // Normaliser la stat par rapport au max
                val normalizedStat = (statValue / maxStat).coerceIn(0f, 1f)
                val statRadius = radius * normalizedStat
                
                val x = centerX + statRadius * cos(angle).toFloat()
                val y = centerY + statRadius * sin(angle).toFloat()
                
                if (j == 0) statPath.moveTo(x, y) else statPath.lineTo(x, y)
            }
            statPath.close()
            
            // Remplissage avec transparence
            drawPath(
                path = statPath,
                color = primaryColor.copy(alpha = 0.4f),
                style = Fill
            )
            // Bordure
            drawPath(
                path = statPath,
                color = primaryColor,
                style = Stroke(width = 3.dp.toPx())
            )
            
            // Points sur les sommets
            for (j in 0 until numStats) {
                val angle = j * angleStep - PI / 2
                val statValue = stats.getOrNull(j)?.baseStat?.toFloat() ?: 0f
                val normalizedStat = (statValue / maxStat).coerceIn(0f, 1f)
                val statRadius = radius * normalizedStat
                
                val x = centerX + statRadius * cos(angle).toFloat()
                val y = centerY + statRadius * sin(angle).toFloat()
                
                drawCircle(
                    color = primaryColor,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}
