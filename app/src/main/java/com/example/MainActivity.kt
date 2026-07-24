package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GarmentPartType
import com.example.ui.components.AiFashionAssistant
import com.example.ui.components.AiScannerScreen
import com.example.ui.components.Custom3DCanvas
import com.example.ui.components.FabricPreviewStudio
import com.example.ui.components.SavedCostumesScreen
import com.example.ui.components.TrendingDesignsFeed
import com.example.ui.theme.AtelierDarkBackground
import com.example.ui.theme.AtelierSurface
import com.example.ui.theme.AtelierSurfaceVariant
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.ThreadCraftTheme
import com.example.viewmodel.CostumeCustomizerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThreadCraftTheme {
                ThreadCraftMainApp()
            }
        }
    }
}

enum class NavigationScreen(val label: String, val icon: @Composable () -> Unit) {
    STUDIO_3D("3D Studio", { Icon(Icons.Default.ViewInAr, contentDescription = "3D Studio") }),
    AI_MATCHER("AI Scanner", { Icon(Icons.Default.Psychology, contentDescription = "AI Scanner") }),
    TRENDING("Trending", { Icon(Icons.Default.TrendingUp, contentDescription = "Trending") }),
    AI_COPILOT("AI Copilot", { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Copilot") }),
    SAVED("Wardrobe", { Icon(Icons.Default.Bookmark, contentDescription = "Wardrobe") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadCraftMainApp(
    viewModel: CostumeCustomizerViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf(NavigationScreen.STUDIO_3D) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var saveTitleInput by remember { mutableStateOf("") }

    val costumeParts by viewModel.costumeParts.collectAsStateWithLifecycle()
    val selectedPartType by viewModel.selectedPartType.collectAsStateWithLifecycle()
    val isAutoSpinning by viewModel.isAutoSpinning.collectAsStateWithLifecycle()
    val zoomScale by viewModel.zoomScale.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val matchResult by viewModel.matchResult.collectAsStateWithLifecycle()
    val savedCostumes by viewModel.savedCostumes.collectAsStateWithLifecycle()

    val currentPart = costumeParts[selectedPartType] ?: costumeParts.values.first()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ThreadCraft 3D",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    if (currentScreen == NavigationScreen.STUDIO_3D) {
                        IconButton(onClick = { showSaveDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save Design",
                                tint = GoldAccent
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AtelierSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = AtelierSurface,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationScreen.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = screen.icon,
                        label = { Text(screen.label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = GoldAccent,
                            indicatorColor = GoldAccent,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AtelierDarkBackground)
        ) {
            when (currentScreen) {
                NavigationScreen.STUDIO_3D -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // 3D Canvas Preview Container
                        Box(
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxWidth()
                        ) {
                            Custom3DCanvas(
                                costumeParts = costumeParts,
                                selectedPartType = selectedPartType,
                                onSelectPart = { viewModel.setSelectedPart(it) },
                                isAutoSpinning = isAutoSpinning,
                                zoomScale = zoomScale,
                                modifier = Modifier.fillMaxSize()
                            )

                            // 3D Overlay Toolbar
                            Card(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = AtelierSurface.copy(alpha = 0.85f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { viewModel.toggleAutoSpin() }) {
                                        Icon(
                                            imageVector = Icons.Default.RotateRight,
                                            contentDescription = "Auto Spin",
                                            tint = if (isAutoSpinning) GoldAccent else Color.White
                                        )
                                    }

                                    IconButton(onClick = { viewModel.setZoom(zoomScale + 0.15f) }) {
                                        Icon(
                                            imageVector = Icons.Default.ZoomIn,
                                            contentDescription = "Zoom In",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(onClick = { viewModel.setZoom(zoomScale - 0.15f) }) {
                                        Icon(
                                            imageVector = Icons.Default.ZoomOut,
                                            contentDescription = "Zoom Out",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            // Garment Part Selector Chips Overlay
                            LazyRow(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(GarmentPartType.entries) { partType ->
                                    val isSelected = selectedPartType == partType
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.setSelectedPart(partType) },
                                        label = { Text(partType.displayName, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = GoldAccent,
                                            selectedLabelColor = Color.Black,
                                            containerColor = AtelierSurfaceVariant,
                                            labelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        // Real-Time Fabric Control Studio
                        FabricPreviewStudio(
                            selectedPartType = selectedPartType,
                            currentPart = currentPart,
                            onUpdatePart = { viewModel.updateCurrentPart(it) },
                            modifier = Modifier.weight(0.9f)
                        )
                    }
                }

                NavigationScreen.AI_MATCHER -> {
                    AiScannerScreen(
                        matchResult = matchResult,
                        isAnalyzing = isAnalyzing,
                        onAnalyzeImage = { viewModel.analyzeCostumeImage(it) },
                        onLoad3DRecommendation = { rec ->
                            viewModel.loadCustomCostume(rec)
                            currentScreen = NavigationScreen.STUDIO_3D
                        }
                    )
                }

                NavigationScreen.TRENDING -> {
                    TrendingDesignsFeed(
                        trendingList = viewModel.getTrendingDesigns(),
                        onCustomizeIn3D = { costume ->
                            viewModel.loadCustomCostume(costume)
                            currentScreen = NavigationScreen.STUDIO_3D
                        }
                    )
                }

                NavigationScreen.AI_COPILOT -> {
                    AiFashionAssistant()
                }

                NavigationScreen.SAVED -> {
                    SavedCostumesScreen(
                        savedCostumes = savedCostumes,
                        onDeleteCostume = { viewModel.deleteSavedCostume(it) }
                    )
                }
            }
        }
    }

    // Save Costume Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save 3D Costume Creation", fontWeight = FontWeight.Bold, color = Color.White) },
            text = {
                Column {
                    Text(
                        "Enter a name for your custom costume creation to save in local database wardrobe:",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = saveTitleInput,
                        onValueChange = { saveTitleInput = it },
                        placeholder = { Text("e.g. Royal Imperial Velvet Gown") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldAccent
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveCurrentCostume(saveTitleInput)
                        saveTitleInput = ""
                        showSaveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Color.Black)
                ) {
                    Text("Save to Wardrobe", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = AtelierSurface
        )
    }
}
