package com.example.data

import com.example.data.model.CostumePart
import com.example.data.model.FabricMaterial
import com.example.data.model.GarmentPartType
import com.example.data.model.InventoryItem
import com.example.data.model.TrendingDesign

object FabricPresets {
    val RoyalSilk = FabricMaterial(
        id = "silk_01",
        name = "Mulberry Silk",
        category = "Silk & Satin",
        description = "Smooth shimmering silk with soft specular reflection and rich drape.",
        defaultColor = 0xFF8E24AA, // Purple Silk
        sheen = 0.85f,
        bump = 0.1f,
        metallic = 0.2f,
        pricePerYard = 24.50
    )

    val ItalianVelvet = FabricMaterial(
        id = "velvet_01",
        name = "Florentine Velvet",
        category = "Velvet & Fleece",
        description = "Deep plush pile velvet with rich light absorption and soft highlights.",
        defaultColor = 0xFF4A0E17, // Crimson Velvet
        sheen = 0.40f,
        bump = 0.65f,
        metallic = 0.0f,
        pricePerYard = 32.00
    )

    val PremiumLeather = FabricMaterial(
        id = "leather_01",
        name = "Tuscan Leather",
        category = "Leather & Vinyl",
        description = "Full-grain supple leather with fine pore texture and subtle glossy sheen.",
        defaultColor = 0xFF3E2723, // Deep Brown Leather
        sheen = 0.50f,
        bump = 0.70f,
        metallic = 0.1f,
        pricePerYard = 45.00
    )

    val GoldBrocade = FabricMaterial(
        id = "brocade_01",
        name = "Venetian Gold Brocade",
        category = "Lace & Embroidery",
        description = "Intricate woven jacquard fabric with metallic gold thread motifs.",
        defaultColor = 0xFFFFD700, // Gold
        sheen = 0.90f,
        bump = 0.80f,
        metallic = 0.75f,
        pricePerYard = 58.00
    )

    val CyberNeonSatin = FabricMaterial(
        id = "cyber_satin_01",
        name = "Holographic Cyber Satin",
        category = "Metals & Cyber",
        description = "Iridescent fiber-optic woven satin with vibrant spectral shimmer.",
        defaultColor = 0xFF00E5FF, // Cyan Cyber
        sheen = 0.95f,
        bump = 0.20f,
        metallic = 0.85f,
        pricePerYard = 38.00
    )

    val DarkDenim = FabricMaterial(
        id = "denim_01",
        name = "Raw Indigo Denim",
        category = "Denim & Canvas",
        description = "Heavyweight twill weave cotton with durable textured finish.",
        defaultColor = 0xFF1A237E, // Indigo Blue
        sheen = 0.15f,
        bump = 0.55f,
        metallic = 0.0f,
        pricePerYard = 18.00
    )

    val CelestialLace = FabricMaterial(
        id = "lace_01",
        name = "Chantilly Star Lace",
        category = "Lace & Embroidery",
        description = "Delicate sheer patterned lace overlay with floral filigree.",
        defaultColor = 0xFFECEFF1, // Silver Lace
        sheen = 0.60f,
        bump = 0.85f,
        metallic = 0.30f,
        pricePerYard = 28.50
    )

    val allFabrics = listOf(
        RoyalSilk, ItalianVelvet, PremiumLeather, GoldBrocade, CyberNeonSatin, DarkDenim, CelestialLace
    )
}

object DefaultInventory {
    val items = listOf(
        InventoryItem(
            id = "inv_01",
            name = "Florentine Velvet Roll (Crimson)",
            category = "Fabric Roll",
            materialName = "Florentine Velvet",
            colorName = "Deep Crimson",
            colorHex = 0xFF4A0E17,
            stockYards = 120.0,
            pricePerYard = 32.00,
            compatibilityTags = listOf("Royal", "Gown", "Cloak", "Velvet", "Gothic")
        ),
        InventoryItem(
            id = "inv_02",
            name = "Venetian Gold Thread Brocade",
            category = "Fabric Roll",
            materialName = "Venetian Gold Brocade",
            colorName = "Imperial Gold",
            colorHex = 0xFFFFD700,
            stockYards = 45.5,
            pricePerYard = 58.00,
            compatibilityTags = listOf("Royal", "Gold", "Embroidery", "Steampunk", "Brocade")
        ),
        InventoryItem(
            id = "inv_03",
            name = "Fiber-Optic Cyber Satin (Cyan)",
            category = "Fabric Roll",
            materialName = "Holographic Cyber Satin",
            colorName = "Neon Cyan",
            colorHex = 0xFF00E5FF,
            stockYards = 85.0,
            pricePerYard = 38.00,
            compatibilityTags = listOf("Cyberpunk", "Futuristic", "Jacket", "Neon", "Satin")
        ),
        InventoryItem(
            id = "inv_04",
            name = "Tuscan Grain Leather Sheeting",
            category = "Fabric Roll",
            materialName = "Tuscan Leather",
            colorName = "Espresso Brown",
            colorHex = 0xFF3E2723,
            stockYards = 60.0,
            pricePerYard = 45.00,
            compatibilityTags = listOf("Leather", "Cosplay", "Armor", "Boots", "Belt")
        ),
        InventoryItem(
            id = "inv_05",
            name = "Solid Brass Ornate Steampunk Buckles",
            category = "Accessory",
            materialName = "Solid Brass",
            colorName = "Antique Brass",
            colorHex = 0xFFC5A059,
            stockYards = 350.0, // quantity
            pricePerYard = 12.50,
            compatibilityTags = listOf("Steampunk", "Buckle", "Belt", "Armor", "Metal")
        ),
        InventoryItem(
            id = "inv_06",
            name = "Mulberry Purple Silk Bolts",
            category = "Fabric Roll",
            materialName = "Mulberry Silk",
            colorName = "Royal Purple",
            colorHex = 0xFF8E24AA,
            stockYards = 95.0,
            pricePerYard = 24.50,
            compatibilityTags = listOf("Silk", "Cape", "Flowing", "Fantasy", "Cosplay")
        )
    )
}

object DefaultTrendingDesigns {
    val items = listOf(
        TrendingDesign(
            id = "trend_01",
            title = "Neon Vanguard Cyber Coat",
            designer = "NeoTokyo Atelier",
            category = "Cyberpunk",
            description = "High-collar tactical trench coat with integrated holographic cyber satin trim and fiber-optic lining.",
            likesCount = 1420,
            imageDrawableResName = "img_trending_cyberpunk",
            presetParts = mapOf(
                GarmentPartType.TOP to CostumePart(
                    partType = GarmentPartType.TOP,
                    styleName = "Cyber Tactical Coat",
                    primaryColor = 0xFF12101F,
                    secondaryColor = 0xFF00E5FF,
                    fabric = FabricPresets.CyberNeonSatin,
                    patternName = "Cyber Grid",
                    patternScale = 1.2f
                ),
                GarmentPartType.BOTTOM to CostumePart(
                    partType = GarmentPartType.BOTTOM,
                    styleName = "Padded Cargo Trousers",
                    primaryColor = 0xFF1A1A24,
                    secondaryColor = 0xFF00E5FF,
                    fabric = FabricPresets.DarkDenim,
                    patternName = "Solid"
                ),
                GarmentPartType.BELT to CostumePart(
                    partType = GarmentPartType.BELT,
                    styleName = "Harness & Utility Sash",
                    primaryColor = 0xFF00E5FF,
                    secondaryColor = 0xFF12101F,
                    fabric = FabricPresets.CyberNeonSatin,
                    patternName = "Solid"
                ),
                GarmentPartType.FOOTWEAR to CostumePart(
                    partType = GarmentPartType.FOOTWEAR,
                    styleName = "Neon Soles Cyber Boots",
                    primaryColor = 0xFF0A0A0F,
                    secondaryColor = 0xFF00E5FF,
                    fabric = FabricPresets.PremiumLeather,
                    patternName = "Solid"
                )
            )
        ),
        TrendingDesign(
            id = "trend_02",
            title = "Imperial Elizabethan Regal Cloak",
            designer = "Atelier Sovereign",
            category = "Fantasy Royal",
            description = "Heavy Florentine velvet cape lined with gold brocade embroidery and Chantilly star lace filigree.",
            likesCount = 2890,
            imageDrawableResName = "img_trending_royal",
            presetParts = mapOf(
                GarmentPartType.TOP to CostumePart(
                    partType = GarmentPartType.TOP,
                    styleName = "Regal Corset & Bodice",
                    primaryColor = 0xFF4A0E17,
                    secondaryColor = 0xFFFFD700,
                    fabric = FabricPresets.ItalianVelvet,
                    patternName = "Damask Floral",
                    patternScale = 1.0f
                ),
                GarmentPartType.CLOAK to CostumePart(
                    partType = GarmentPartType.CLOAK,
                    styleName = "Imperial Hooded Cape",
                    primaryColor = 0xFF31070D,
                    secondaryColor = 0xFFFFD700,
                    fabric = FabricPresets.ItalianVelvet,
                    patternName = "Gold Embroidery",
                    patternScale = 0.9f
                ),
                GarmentPartType.BOTTOM to CostumePart(
                    partType = GarmentPartType.BOTTOM,
                    styleName = "Full Velvet Ball Skirt",
                    primaryColor = 0xFF4A0E17,
                    secondaryColor = 0xFFFFD700,
                    fabric = FabricPresets.ItalianVelvet,
                    patternName = "Solid"
                ),
                GarmentPartType.BELT to CostumePart(
                    partType = GarmentPartType.BELT,
                    styleName = "Gold Thread Cinch Belt",
                    primaryColor = 0xFFFFD700,
                    secondaryColor = 0xFF4A0E17,
                    fabric = FabricPresets.GoldBrocade,
                    patternName = "Gold Embroidery"
                )
            )
        ),
        TrendingDesign(
            id = "trend_03",
            title = "Celestial Starlight Cosplay Robe",
            designer = "Luna Fantasy Studio",
            category = "Cosplay",
            description = "Flowing purple Mulberry silk costume with celestial lace trim and glowing gold brocade clasp.",
            likesCount = 1850,
            imageDrawableResName = "img_hero_fashion",
            presetParts = mapOf(
                GarmentPartType.TOP to CostumePart(
                    partType = GarmentPartType.TOP,
                    styleName = "Starlight Tunic",
                    primaryColor = 0xFF7B1FA2,
                    secondaryColor = 0xFFECEFF1,
                    fabric = FabricPresets.RoyalSilk,
                    patternName = "Celestial Lace"
                ),
                GarmentPartType.CLOAK to CostumePart(
                    partType = GarmentPartType.CLOAK,
                    styleName = "Flowing Silk Mantle",
                    primaryColor = 0xFF8E24AA,
                    secondaryColor = 0xFFFFD700,
                    fabric = FabricPresets.RoyalSilk,
                    patternName = "Solid"
                ),
                GarmentPartType.BOTTOM to CostumePart(
                    partType = GarmentPartType.BOTTOM,
                    styleName = "Pleated Silk Robe Bottom",
                    primaryColor = 0xFF4A148C,
                    secondaryColor = 0xFFECEFF1,
                    fabric = FabricPresets.RoyalSilk,
                    patternName = "Solid"
                )
            )
        )
    )
}
