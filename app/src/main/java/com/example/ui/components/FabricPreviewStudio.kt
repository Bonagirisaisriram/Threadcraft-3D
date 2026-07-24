package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FabricPresets
import com.example.data.model.CostumePart
import com.example.data.model.FabricMaterial
import com.example.data.model.GarmentPartType
import com.example.ui.theme.AtelierSurface
import com.example.ui.theme.AtelierSurfaceVariant
import com.example.ui.theme.GoldAccent

@Composable
fun FabricPreviewStudio(
    selectedPartType: GarmentPartType,
    currentPart: CostumePart,
    onUpdatePart: (CostumePart) -> Unit,
    modifier: Modifier = Modifier
) {
    val fabricPresets = FabricPresets.allFabrics

    val presetColors = listOf(
        0xFF4A0E17 to "Imperial Crimson",
        0xFFFFD700 to "Venetian Gold",
        0xFF8E24AA to "Mulberry Purple",
        0xFF00E5FF to "Cyber Cyan",
        0xFF12101F to "Obsidian Black",
        0xFF3E2723 to "Tuscan Brown",
        0xFF1A237E to "Indigo Blue",
        0xFFECEFF1 to "Celestial Silver"
    )

    val patternOptions = listOf(
        "Solid",
        "Damask Floral",
        "Cyber Grid",
        "Gold Embroidery",
        "Steampunk Gears"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = AtelierSurface)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Fabric Studio",
                        tint = GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Real-Time Fabric Studio (${selectedPartType.displayName})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "$${String.format("%.2f", currentPart.fabric.pricePerYard)}/yd",
                    style = MaterialTheme.typography.labelLarge,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Select Fabric Material Type
            Text(
                text = "1. Select Fabric Material",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fabricPresets) { fabric ->
                    val isSelected = currentPart.fabric.id == fabric.id
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            onUpdatePart(
                                currentPart.copy(
                                    fabric = fabric,
                                    primaryColor = fabric.defaultColor
                                )
                            )
                        },
                        label = { Text(fabric.name, fontSize = 12.sp) },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = Color.White,
                            containerColor = AtelierSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Primary Color Palette Selection
            Text(
                text = "2. Color Palette",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(presetColors) { (colorHex, name) ->
                    val isSelected = currentPart.primaryColor == colorHex
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(colorHex))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) GoldAccent else Color.White.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .clickable {
                                onUpdatePart(currentPart.copy(primaryColor = colorHex))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = name,
                                tint = if (colorHex == 0xFFFFD700 || colorHex == 0xFFECEFF1) Color.Black else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Pattern Overlay Selection
            Text(
                text = "3. Pattern Overlay",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(patternOptions) { pattern ->
                    val isSelected = currentPart.patternName == pattern
                    AssistChip(
                        onClick = { onUpdatePart(currentPart.copy(patternName = pattern)) },
                        label = { Text(pattern, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Style,
                                contentDescription = null,
                                tint = if (isSelected) GoldAccent else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else AtelierSurfaceVariant,
                            labelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Sheen / Glossiness & Bump Sliders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sheen Slider
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Fabric Sheen / Gloss",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Text(
                            text = "${(currentPart.sheenMultiplier * currentPart.fabric.sheen * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent
                        )
                    }

                    Slider(
                        value = currentPart.sheenMultiplier,
                        onValueChange = { onUpdatePart(currentPart.copy(sheenMultiplier = it)) },
                        valueRange = 0.1f..2.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldAccent,
                            activeTrackColor = GoldAccent
                        )
                    )
                }

                // Bump / Texture Slider
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Texture Grain",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Text(
                            text = "${(currentPart.bumpMultiplier * currentPart.fabric.bump * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldAccent
                        )
                    }

                    Slider(
                        value = currentPart.bumpMultiplier,
                        onValueChange = { onUpdatePart(currentPart.copy(bumpMultiplier = it)) },
                        valueRange = 0.1f..2.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldAccent,
                            activeTrackColor = GoldAccent
                        )
                    )
                }
            }
        }
    }
}
