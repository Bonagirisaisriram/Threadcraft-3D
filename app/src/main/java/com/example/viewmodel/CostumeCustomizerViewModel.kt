package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FabricPresets
import com.example.data.model.AiMatchResult
import com.example.data.model.CostumePart
import com.example.data.model.CustomCostume
import com.example.data.model.GarmentPartType
import com.example.data.model.SavedCostumeEntity
import com.example.data.repository.CostumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class CostumeCustomizerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CostumeRepository(application)

    // Current 3D Costume Parts state
    private val _costumeParts = MutableStateFlow<Map<GarmentPartType, CostumePart>>(
        mapOf(
            GarmentPartType.TOP to CostumePart(
                partType = GarmentPartType.TOP,
                styleName = "Imperial Bodice",
                primaryColor = 0xFF4A0E17,
                secondaryColor = 0xFFFFD700,
                fabric = FabricPresets.ItalianVelvet,
                patternName = "Damask Floral"
            ),
            GarmentPartType.BOTTOM to CostumePart(
                partType = GarmentPartType.BOTTOM,
                styleName = "Pleated Skirt",
                primaryColor = 0xFF4A0E17,
                secondaryColor = 0xFFFFD700,
                fabric = FabricPresets.ItalianVelvet
            ),
            GarmentPartType.CLOAK to CostumePart(
                partType = GarmentPartType.CLOAK,
                styleName = "Regal Hooded Cloak",
                primaryColor = 0xFF31070D,
                secondaryColor = 0xFFFFD700,
                fabric = FabricPresets.ItalianVelvet,
                patternName = "Gold Embroidery"
            ),
            GarmentPartType.BELT to CostumePart(
                partType = GarmentPartType.BELT,
                styleName = "Gold Thread Cinch Belt",
                primaryColor = 0xFFFFD700,
                secondaryColor = 0xFF4A0E17,
                fabric = FabricPresets.GoldBrocade
            ),
            GarmentPartType.FOOTWEAR to CostumePart(
                partType = GarmentPartType.FOOTWEAR,
                styleName = "Royal Boots",
                primaryColor = 0xFF12101F,
                secondaryColor = 0xFFFFD700,
                fabric = FabricPresets.PremiumLeather
            )
        )
    )
    val costumeParts: StateFlow<Map<GarmentPartType, CostumePart>> = _costumeParts.asStateFlow()

    // Selected Part for Fabric Tweaking
    private val _selectedPartType = MutableStateFlow(GarmentPartType.TOP)
    val selectedPartType: StateFlow<GarmentPartType> = _selectedPartType.asStateFlow()

    // 3D Controls State
    private val _isAutoSpinning = MutableStateFlow(false)
    val isAutoSpinning: StateFlow<Boolean> = _isAutoSpinning.asStateFlow()

    private val _zoomScale = MutableStateFlow(1.0f)
    val zoomScale: StateFlow<Float> = _zoomScale.asStateFlow()

    // AI Scanner State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _matchResult = MutableStateFlow<AiMatchResult?>(null)
    val matchResult: StateFlow<AiMatchResult?> = _matchResult.asStateFlow()

    // Saved Costumes Flow from Room DB
    private val _savedCostumes = MutableStateFlow<List<SavedCostumeEntity>>(emptyList())
    val savedCostumes: StateFlow<List<SavedCostumeEntity>> = _savedCostumes.asStateFlow()

    init {
        viewModelScope.launch {
            repository.savedCostumesFlow.collectLatest { list ->
                _savedCostumes.value = list
            }
        }
    }

    fun setSelectedPart(partType: GarmentPartType) {
        _selectedPartType.value = partType
    }

    fun updateCurrentPart(updatedPart: CostumePart) {
        val currentMap = _costumeParts.value.toMutableMap()
        currentMap[updatedPart.partType] = updatedPart
        _costumeParts.value = currentMap
    }

    fun toggleAutoSpin() {
        _isAutoSpinning.value = !_isAutoSpinning.value
    }

    fun setZoom(scale: Float) {
        _zoomScale.value = scale.coerceIn(0.7f, 1.8f)
    }

    fun loadCustomCostume(costume: CustomCostume) {
        _costumeParts.value = costume.parts
    }

    fun saveCurrentCostume(title: String) {
        viewModelScope.launch {
            val costume = CustomCostume(
                id = UUID.randomUUID().toString(),
                title = title.ifEmpty { "ThreadCraft 3D Design" },
                parts = _costumeParts.value
            )
            repository.saveCustomCostume(costume)
        }
    }

    fun deleteSavedCostume(id: String) {
        viewModelScope.launch {
            repository.deleteSavedCostume(id)
        }
    }

    fun analyzeCostumeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            val result = repository.analyzeAndMatchCostume(bitmap)
            _matchResult.value = result
            _isAnalyzing.value = false
        }
    }

    fun getTrendingDesigns() = repository.getTrendingDesigns()
}
