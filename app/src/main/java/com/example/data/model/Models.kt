package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GarmentPartType(val displayName: String, val layerIndex: Int) {
    TOP("Jacket / Top", 1),
    BOTTOM("Pants / Skirt", 2),
    CLOAK("Cloak / Cape", 3),
    BELT("Belt / Sash", 4),
    FOOTWEAR("Boots / Footwear", 5),
    HEADWEAR("Crown / Mask", 6)
}

data class FabricMaterial(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val defaultColor: Long,
    val sheen: Float, // 0.0 (matte) to 1.0 (ultra shiny)
    val bump: Float,  // 0.0 (smooth) to 1.0 (rough/textured)
    val metallic: Float, // 0.0 (organic) to 1.0 (metal)
    val pricePerYard: Double
)

data class CostumePart(
    val partType: GarmentPartType,
    val styleName: String,
    val primaryColor: Long,
    val secondaryColor: Long,
    val fabric: FabricMaterial,
    val patternName: String = "Solid",
    val patternScale: Float = 1.0f,
    val sheenMultiplier: Float = 1.0f,
    val bumpMultiplier: Float = 1.0f
)

data class CustomCostume(
    val id: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val parts: Map<GarmentPartType, CostumePart>,
    val designerNotes: String = ""
)

data class InventoryItem(
    val id: String,
    val name: String,
    val category: String, // "Fabric Roll", "Pattern Cut", "Trimming", "Accessory"
    val materialName: String,
    val colorName: String,
    val colorHex: Long,
    val stockYards: Double,
    val pricePerYard: Double,
    val compatibilityTags: List<String>
)

data class TrendingDesign(
    val id: String,
    val title: String,
    val designer: String,
    val category: String,
    val description: String,
    val likesCount: Int,
    val imageDrawableResName: String,
    val presetParts: Map<GarmentPartType, CostumePart>
)

data class AiMatchResult(
    val detectedStyle: String,
    val detectedMaterial: String,
    val detectedColors: List<String>,
    val themeEra: String,
    val matchScore: Int, // 0 to 100
    val matchedInventory: List<InventoryItem>,
    val aiSummary: String,
    val custom3DRecommendation: CustomCostume? = null
)

@Entity(tableName = "saved_costumes")
data class SavedCostumeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val timestamp: Long,
    val partsJson: String,
    val designerNotes: String
)

@Entity(tableName = "inventory_items")
data class InventoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val materialName: String,
    val colorName: String,
    val colorHex: Long,
    val stockYards: Double,
    val pricePerYard: Double,
    val compatibilityTagsJson: String
)
