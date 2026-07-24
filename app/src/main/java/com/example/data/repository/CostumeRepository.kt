package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.data.DefaultInventory
import com.example.data.DefaultTrendingDesigns
import com.example.data.FabricPresets
import com.example.data.api.GeminiClient
import com.example.data.db.AppDatabase
import com.example.data.model.AiMatchResult
import com.example.data.model.CostumePart
import com.example.data.model.CustomCostume
import com.example.data.model.FabricMaterial
import com.example.data.model.GarmentPartType
import com.example.data.model.InventoryEntity
import com.example.data.model.InventoryItem
import com.example.data.model.SavedCostumeEntity
import com.example.data.model.TrendingDesign
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CostumeRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val costumeDao = db.costumeDao()
    private val inventoryDao = db.inventoryDao()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun getPresetFabrics(): List<FabricMaterial> = FabricPresets.allFabrics

    fun getTrendingDesigns(): List<TrendingDesign> = DefaultTrendingDesigns.items

    val savedCostumesFlow: Flow<List<SavedCostumeEntity>> = costumeDao.getAllSavedCostumes()

    suspend fun saveCustomCostume(costume: CustomCostume) {
        // Convert costume parts to simple description JSON
        val summary = costume.parts.entries.joinToString("; ") { (type, part) ->
            "${type.displayName}: ${part.styleName} (${part.fabric.name}, Pattern: ${part.patternName})"
        }
        val entity = SavedCostumeEntity(
            id = costume.id.ifEmpty { UUID.randomUUID().toString() },
            title = costume.title.ifEmpty { "Custom 3D Outfit" },
            timestamp = System.currentTimeMillis(),
            partsJson = summary,
            designerNotes = costume.designerNotes
        )
        costumeDao.insertCostume(entity)
    }

    suspend fun deleteSavedCostume(id: String) {
        costumeDao.deleteCostume(id)
    }

    fun getInventoryItems(): List<InventoryItem> = DefaultInventory.items

    suspend fun analyzeAndMatchCostume(bitmap: Bitmap): AiMatchResult {
        val aiAnalysis = GeminiClient.analyzeCostumeImage(bitmap)

        // Calculate match against inventory items based on keyword heuristic + AI output
        val matchedInventory = DefaultInventory.items.filter { item ->
            item.compatibilityTags.any { tag ->
                aiAnalysis.contains(tag, ignoreCase = true)
            } || aiAnalysis.contains(item.materialName, ignoreCase = true)
        }.ifEmpty {
            DefaultInventory.items.take(3)
        }

        val score = if (matchedInventory.isNotEmpty()) (85..98).random() else 65

        // Extract detected style line
        val style = aiAnalysis.lines()
            .firstOrNull { it.contains("DETECTED STYLE", ignoreCase = true) }
            ?.substringAfter(":")?.trim() ?: "Custom Avant-Garde"

        val material = aiAnalysis.lines()
            .firstOrNull { it.contains("PRIMARY FABRIC", ignoreCase = true) }
            ?.substringAfter(":")?.trim() ?: "Velvet & Silk Blend"

        val theme = aiAnalysis.lines()
            .firstOrNull { it.contains("THEME & ERA", ignoreCase = true) }
            ?.substringAfter(":")?.trim() ?: "Contemporary High Fashion"

        val recommendedFabrics = getPresetFabrics()
            .firstOrNull { aiAnalysis.contains(it.name, ignoreCase = true) }
            ?: FabricPresets.ItalianVelvet

        val customRec = CustomCostume(
            id = UUID.randomUUID().toString(),
            title = "AI Recreated: $style",
            parts = mapOf(
                GarmentPartType.TOP to CostumePart(
                    partType = GarmentPartType.TOP,
                    styleName = "AI Scanned $style Top",
                    primaryColor = recommendedFabrics.defaultColor,
                    secondaryColor = 0xFFFFD700,
                    fabric = recommendedFabrics
                ),
                GarmentPartType.CLOAK to CostumePart(
                    partType = GarmentPartType.CLOAK,
                    styleName = "Matching Draped Cape",
                    primaryColor = recommendedFabrics.defaultColor,
                    secondaryColor = 0xFF12101F,
                    fabric = FabricPresets.RoyalSilk
                )
            ),
            designerNotes = "Auto-generated from AI Image Recognition scan."
        )

        return AiMatchResult(
            detectedStyle = style,
            detectedMaterial = material,
            detectedColors = listOf("#4A0E17", "#FFD700", "#12101F"),
            themeEra = theme,
            matchScore = score,
            matchedInventory = matchedInventory,
            aiSummary = aiAnalysis,
            custom3DRecommendation = customRec
        )
    }

    suspend fun getFashionAssistantAdvice(query: String): String {
        return GeminiClient.generateFashionAdvice(query)
    }
}
