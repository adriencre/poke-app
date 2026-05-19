package com.example.pokedex.presentation.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.runtime.key
import androidx.compose.runtime.LaunchedEffect
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.util.UiState
import com.example.pokedex.presentation.components.PokemonRadarChart
import com.example.pokedex.presentation.components.StatBar
import com.example.pokedex.presentation.components.TypeChip
import com.example.pokedex.presentation.theme.PokemonTypeColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PokemonDetailScreen(
    onBackClick: () -> Unit,
    onPokemonClick: (Int) -> Unit = {},
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    val favoriteScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "fav_scale"
    )
    val favoriteColor by animateColorAsState(
        targetValue = if (isFavorite) Color(0xFFE94560) else MaterialTheme.colorScheme.onSurface,
        label = "fav_color"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (val s = detailState) {
                        is UiState.Success -> Text(s.data.displayName, fontWeight = FontWeight.Bold)
                        else -> Text("Détail")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    if (detailState is UiState.Success) {
                        val cryUrl = (detailState as UiState.Success<PokemonDetail>).data.cryUrl
                        if (cryUrl != null) {
                            val context = LocalContext.current
                            IconButton(onClick = { playPokemonCry(context, cryUrl) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Jouer le cri")
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (detailState is UiState.Success) {
                FloatingActionButton(
                    onClick = { viewModel.toggleFavorite() },
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.scale(favoriteScale)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                        tint = favoriteColor, modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        when (val state = detailState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(48.dp), color = MaterialTheme.colorScheme.primary)
                }
            }
            is UiState.Error -> {
                Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("😢", fontSize = 48.sp)
                        Text(state.message, fontSize = 14.sp, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
                        Button(onClick = onBackClick) { Icon(Icons.Default.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.size(8.dp)); Text("Retour") }
                    }
                }
            }
            is UiState.Success -> DetailContent(detail = state.data, onPokemonClick = onPokemonClick, modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailContent(detail: PokemonDetail, onPokemonClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val primaryTypeColor = if (detail.types.isNotEmpty()) PokemonTypeColors.getColor(detail.types.first()) else MaterialTheme.colorScheme.primary

    var isShiny by remember { mutableStateOf(false) }
    var triggerSparkles by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(
            Modifier.fillMaxWidth().height(280.dp).background(Brush.verticalGradient(listOf(primaryTypeColor.copy(alpha = 0.3f), primaryTypeColor.copy(alpha = 0.05f)))),
            contentAlignment = Alignment.Center
        ) {
            Text(detail.formattedNumber, fontSize = 120.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
            
            if (isShiny && triggerSparkles > 0) {
                key(triggerSparkles) {
                    ShinySparkles(Modifier.align(Alignment.Center))
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(if (isShiny && detail.shinyImageUrl != null) detail.shinyImageUrl else detail.imageUrl).crossfade(true).build(),
                contentDescription = detail.displayName, modifier = Modifier.size(220.dp)
            )
            
            if (detail.shinyImageUrl != null) {
                IconButton(
                    onClick = { 
                        isShiny = !isShiny 
                        if (isShiny) {
                            triggerSparkles++
                        }
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Shiny",
                        tint = if (isShiny) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(detail.displayName, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text(detail.formattedNumber, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                detail.types.forEach { TypeChip(type = it) }
            }

            if (detail.flavorTexts.isNotEmpty()) {
                var selectedTextIndex by remember { mutableStateOf(0) }
                var expanded by remember { mutableStateOf(false) }
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        
                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text(
                                    text = detail.flavorTexts[selectedTextIndex].displayVersion,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryTypeColor
                                )
                                Icon(Icons.Default.ArrowDropDown, null, tint = primaryTypeColor)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                detail.flavorTexts.forEachIndexed { index, flavorText ->
                                    DropdownMenuItem(
                                        text = { Text(flavorText.displayVersion) },
                                        onClick = {
                                            selectedTextIndex = index
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "“${detail.flavorTexts[selectedTextIndex].text}”",
                            fontSize = 15.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
                Row(Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    InfoItem("Taille", detail.heightInMeters)
                    Box(Modifier.width(1.dp).height(40.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)))
                    InfoItem("Poids", detail.weightInKg)
                }
            }

            var showRadarChart by remember { mutableStateOf(true) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Statistiques de base", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Barres", fontSize = 12.sp, color = if (showRadarChart) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary)
                    Switch(
                        checked = showRadarChart,
                        onCheckedChange = { showRadarChart = it },
                        modifier = Modifier.padding(horizontal = 8.dp).scale(0.8f)
                    )
                    Text("Radar", fontSize = 12.sp, color = if (showRadarChart) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                if (showRadarChart) {
                    PokemonRadarChart(stats = detail.stats)
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        detail.stats.forEach { stat ->
                            StatBar(stat = stat)
                        }
                    }
                }
            }

            Text("Faiblesses et Résistances", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    detail.weaknessesAndResistances.forEach { (type, multiplier) ->
                        val color = when {
                            multiplier > 1f -> Color(0xFFE57373) // Rouge pour faiblesse
                            multiplier < 1f -> Color(0xFF81C784) // Vert pour résistance
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                TypeChip(type = type)
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "x${if (multiplier % 1f == 0f) multiplier.toInt() else multiplier}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = color
                                )
                            }
                        }
                    }
                }
            }

            if (detail.abilities.isNotEmpty()) {
                Text("Talents", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        detail.abilities.forEach { ability ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = ability.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                if (ability.isHidden) {
                                    Spacer(Modifier.width(8.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Secret",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (detail.moves.isNotEmpty()) {
                var movesExpanded by remember { mutableStateOf(false) }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Techniques de combat", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    TextButton(onClick = { movesExpanded = !movesExpanded }) {
                        Text(if (movesExpanded) "Masquer" else "Afficher (${detail.moves.size})", color = primaryTypeColor)
                    }
                }
                
                if (movesExpanded) {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(Modifier.padding(8.dp)) {
                            detail.moves.forEach { move ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1.5f)) {
                                        Text(move.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Niv. ${move.levelLearnedAt}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                            Spacer(Modifier.width(8.dp))
                                            TypeChip(type = move.type)
                                        }
                                    }
                                    
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "ATQ: ${move.power ?: '-'}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "PRÉ: ${move.accuracy ?: '-'}%",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                            }
                        }
                    }
                }
            }

            if (detail.evolutions.isNotEmpty()) {
                Text("Évolutions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(detail.evolutions.size) { index ->
                            val evolution = detail.evolutions[index]
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { onPokemonClick(evolution.id) }
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current).data(evolution.imageUrl).crossfade(true).build(),
                                    contentDescription = evolution.name,
                                    modifier = Modifier.size(80.dp)
                                )
                                Text(evolution.name.replaceFirstChar { it.uppercase() }, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                            if (index < detail.evolutions.size - 1) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Évolue vers", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), modifier = Modifier.padding(start = 16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

private fun playPokemonCry(context: android.content.Context, url: String) {
    try {
        MediaPlayer().apply {
            setDataSource(context, Uri.parse(url))
            prepareAsync()
            setOnPreparedListener { start() }
            setOnCompletionListener { release() }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
private fun ShinySparkles(modifier: Modifier = Modifier) {
    val sparkles = remember {
        (0 until 12).map { index ->
            val angle = (index * 30) * (Math.PI / 180f)
            val distance = kotlin.random.Random.nextInt(80, 140)
            val scale = 0.6f + kotlin.random.Random.nextFloat() * 0.7f
            Triple(angle, distance, scale)
        }
    }
    
    Box(modifier = modifier) {
        for (sparkle in sparkles) {
            SparkleItem(angle = sparkle.first, distance = sparkle.second.dp, scale = sparkle.third)
        }
    }
}

@Composable
private fun SparkleItem(angle: Double, distance: androidx.compose.ui.unit.Dp, scale: Float) {
    val animProgress = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 850,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
    }
    
    val alpha = 1f - animProgress.value
    val currentDistance = distance * animProgress.value
    val x = (Math.cos(angle) * currentDistance.value).dp
    val y = (Math.sin(angle) * currentDistance.value).dp
    
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        tint = Color(0xFFFFD700),
        modifier = Modifier
            .offset(x = x, y = y)
            .scale(scale * animProgress.value)
            .alpha(alpha)
            .size(24.dp)
    )
}
