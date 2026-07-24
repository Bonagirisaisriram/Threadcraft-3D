package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.data.model.CostumePart
import com.example.data.model.GarmentPartType
import com.example.ui.theme.AtelierSurface
import com.example.ui.theme.AtelierSurfaceVariant
import com.example.ui.theme.GoldAccent
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Custom3DCanvas(
    costumeParts: Map<GarmentPartType, CostumePart>,
    selectedPartType: GarmentPartType?,
    onSelectPart: (GarmentPartType) -> Unit,
    isAutoSpinning: Boolean = false,
    zoomScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    var yawDegrees by remember { mutableFloatStateOf(20f) }
    var pitchDegrees by remember { mutableFloatStateOf(10f) }

    val autoSpinYaw = remember { Animatable(0f) }

    LaunchedEffect(isAutoSpinning) {
        if (isAutoSpinning) {
            autoSpinYaw.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(12000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            autoSpinYaw.stop()
        }
    }

    val currentYaw = if (isAutoSpinning) autoSpinYaw.value else yawDegrees

    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF13101E),
                        Color(0xFF1B172B),
                        Color(0xFF0C0A13)
                    )
                )
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    yawDegrees = (yawDegrees + dragAmount.x * 0.5f) % 360f
                    pitchDegrees = (pitchDegrees - dragAmount.y * 0.3f).coerceIn(-30f, 30f)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f + 40f

            val radYaw = Math.toRadians(currentYaw.toDouble()).toFloat()
            val radPitch = Math.toRadians(pitchDegrees.toDouble()).toFloat()

            // Draw Studio Lighting Platform / Pedestal
            drawPedestal(centerX, centerY + 240f * zoomScale, radYaw, zoomScale)

            // Draw Background Studio Light Rays
            drawLightGrid(centerX, centerY)

            // Draw Mannequin Base Body (Torso & Legs silhouette)
            drawMannequinBase(centerX, centerY, radYaw, radPitch, zoomScale)

            // Order of drawing layers based on 3D depth and layer index
            val cloakPart = costumeParts[GarmentPartType.CLOAK]
            val bottomPart = costumeParts[GarmentPartType.BOTTOM]
            val topPart = costumeParts[GarmentPartType.TOP]
            val beltPart = costumeParts[GarmentPartType.BELT]
            val footwearPart = costumeParts[GarmentPartType.FOOTWEAR]
            val headwearPart = costumeParts[GarmentPartType.HEADWEAR]

            // 1. Draw Back Cloak/Cape if facing forward or front cloak if facing back
            if (cloakPart != null) {
                drawCloakLayer(
                    centerX = centerX,
                    centerY = centerY,
                    yaw = radYaw,
                    pitch = radPitch,
                    zoom = zoomScale,
                    part = cloakPart,
                    isSelected = selectedPartType == GarmentPartType.CLOAK
                )
            }

            // 2. Draw Footwear / Boots
            if (footwearPart != null) {
                drawFootwearLayer(
                    centerX = centerX,
                    centerY = centerY,
                    yaw = radYaw,
                    zoom = zoomScale,
                    part = footwearPart,
                    isSelected = selectedPartType == GarmentPartType.FOOTWEAR
                )
            }

            // 3. Draw Bottoms / Skirt / Pants
            if (bottomPart != null) {
                drawBottomLayer(
                    centerX = centerX,
                    centerY = centerY,
                    yaw = radYaw,
                    pitch = radPitch,
                    zoom = zoomScale,
                    part = bottomPart,
                    isSelected = selectedPartType == GarmentPartType.BOTTOM
                )
            }

            // 4. Draw Top / Jacket / Corset
            if (topPart != null) {
                drawTopLayer(
                    centerX = centerX,
                    centerY = centerY,
                    yaw = radYaw,
                    pitch = radPitch,
                    zoom = zoomScale,
                    part = topPart,
                    isSelected = selectedPartType == GarmentPartType.TOP
                )
            }

            // 5. Draw Belt / Harness
            if (beltPart != null) {
                drawBeltLayer(
                    centerX = centerX,
                    centerY = centerY,
                    yaw = radYaw,
                    zoom = zoomScale,
                    part = beltPart,
                    isSelected = selectedPartType == GarmentPartType.BELT
                )
            }

            // 6. Draw Headwear / Crown
            if (headwearPart != null) {
                drawHeadwearLayer(
                    centerX = centerX,
                    centerY = centerY,
                    yaw = radYaw,
                    zoom = zoomScale,
                    part = headwearPart,
                    isSelected = selectedPartType == GarmentPartType.HEADWEAR
                )
            }
        }
    }
}

// --- 3D Projection Helpers & Render Pass Functions ---

private fun DrawScope.drawPedestal(cx: Float, cy: Float, yaw: Float, zoom: Float) {
    val radiusX = 140f * zoom
    val radiusY = 35f * zoom

    // Elliptical pedestal floor
    drawOval(
        color = Color(0x33FFD700),
        topLeft = Offset(cx - radiusX, cy - radiusY),
        size = Size(radiusX * 2, radiusY * 2)
    )

    drawOval(
        color = GoldAccent,
        topLeft = Offset(cx - radiusX, cy - radiusY),
        size = Size(radiusX * 2, radiusY * 2),
        style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
    )
}

private fun DrawScope.drawLightGrid(cx: Float, cy: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x22C29BFF), Color.Transparent),
            center = Offset(cx, cy - 80f),
            radius = 350f
        ),
        radius = 350f,
        center = Offset(cx, cy - 80f)
    )
}

private fun DrawScope.drawMannequinBase(cx: Float, cy: Float, yaw: Float, pitch: Float, zoom: Float) {
    val cosY = cos(yaw)

    // Wood/Chrome Mannequin Stand
    drawLine(
        color = Color(0xFF4A4553),
        start = Offset(cx, cy + 120f * zoom),
        end = Offset(cx, cy + 240f * zoom),
        strokeWidth = 6f * zoom
    )

    // Neck & Head Form
    drawOval(
        color = Color(0xFF2C273B),
        topLeft = Offset(cx - 20f * zoom, cy - 200f * zoom),
        size = Size(40f * zoom, 50f * zoom)
    )
}

private fun DrawScope.drawTopLayer(
    centerX: Float,
    centerY: Float,
    yaw: Float,
    pitch: Float,
    zoom: Float,
    part: CostumePart,
    isSelected: Boolean
) {
    val colorPrimary = Color(part.primaryColor)
    val colorSecondary = Color(part.secondaryColor)
    val cosY = cos(yaw)
    val sinY = sin(yaw)

    val width = (80f + cosY * 15f) * zoom
    val height = 110f * zoom
    val topY = centerY - 150f * zoom

    val path = Path().apply {
        moveTo(centerX - width, topY + 20f * zoom) // Left shoulder
        lineTo(centerX + width, topY + 20f * zoom) // Right shoulder
        lineTo(centerX + width * 0.75f, topY + height) // Right waist
        lineTo(centerX - width * 0.75f, topY + height) // Left waist
        close()
    }

    // Base Fabric Fill
    drawPath(path = path, color = colorPrimary)

    // Apply Fabric Texture & Lighting Shimmer
    drawPath(
        path = path,
        brush = Brush.horizontalGradient(
            colors = listOf(
                colorPrimary,
                colorPrimary.copy(alpha = 0.8f + part.fabric.sheen * 0.2f),
                colorSecondary.copy(alpha = 0.4f * part.fabric.sheen),
                colorPrimary
            )
        )
    )

    // Draw Pattern Details
    if (part.patternName != "Solid") {
        drawPatternOverlay(path, part, centerX, topY, width, height)
    }

    // Collar / Lapel Trim
    val collarPath = Path().apply {
        moveTo(centerX - 25f * zoom, topY + 20f * zoom)
        lineTo(centerX, topY + 60f * zoom)
        lineTo(centerX + 25f * zoom, topY + 20f * zoom)
    }
    drawPath(collarPath, color = colorSecondary, style = Stroke(width = 4f * zoom))

    // Selection Halo
    if (isSelected) {
        drawPath(path, color = GoldAccent, style = Stroke(width = 3.5f * zoom))
    }
}

private fun DrawScope.drawBottomLayer(
    centerX: Float,
    centerY: Float,
    yaw: Float,
    pitch: Float,
    zoom: Float,
    part: CostumePart,
    isSelected: Boolean
) {
    val colorPrimary = Color(part.primaryColor)
    val colorSecondary = Color(part.secondaryColor)
    val topY = centerY - 40f * zoom
    val widthTop = 60f * zoom
    val widthBottom = 95f * zoom
    val height = 130f * zoom

    val path = Path().apply {
        moveTo(centerX - widthTop, topY)
        lineTo(centerX + widthTop, topY)
        lineTo(centerX + widthBottom, topY + height)
        lineTo(centerX - widthBottom, topY + height)
        close()
    }

    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(colorPrimary, colorPrimary.copy(alpha = 0.85f), colorSecondary.copy(alpha = 0.6f))
        )
    )

    // Cloth Folds / Creases Lines
    val foldCount = 5
    for (i in 0..foldCount) {
        val fraction = i.toFloat() / foldCount
        val xStart = centerX - widthTop + (widthTop * 2 * fraction)
        val xEnd = centerX - widthBottom + (widthBottom * 2 * fraction)
        drawLine(
            color = Color.Black.copy(alpha = 0.25f * part.fabric.bump),
            start = Offset(xStart, topY),
            end = Offset(xEnd, topY + height),
            strokeWidth = 2f * zoom
        )
    }

    if (isSelected) {
        drawPath(path, color = GoldAccent, style = Stroke(width = 3.5f * zoom))
    }
}

private fun DrawScope.drawCloakLayer(
    centerX: Float,
    centerY: Float,
    yaw: Float,
    pitch: Float,
    zoom: Float,
    part: CostumePart,
    isSelected: Boolean
) {
    val colorPrimary = Color(part.primaryColor)
    val colorSecondary = Color(part.secondaryColor)
    val topY = centerY - 160f * zoom
    val width = 120f * zoom
    val height = 260f * zoom

    val path = Path().apply {
        moveTo(centerX - 35f * zoom, topY)
        quadraticTo(centerX - width, topY + height * 0.5f, centerX - width * 1.1f, topY + height)
        lineTo(centerX + width * 1.1f, topY + height)
        quadraticTo(centerX + width, topY + height * 0.5f, centerX + 35f * zoom, topY)
        close()
    }

    drawPath(
        path = path,
        brush = Brush.radialGradient(
            colors = listOf(colorPrimary, colorPrimary.copy(alpha = 0.9f), colorSecondary),
            center = Offset(centerX, topY + 80f),
            radius = 280f * zoom
        )
    )

    // Trim border
    drawPath(path, color = colorSecondary, style = Stroke(width = 3f * zoom))

    if (isSelected) {
        drawPath(path, color = GoldAccent, style = Stroke(width = 4f * zoom))
    }
}

private fun DrawScope.drawBeltLayer(
    centerX: Float,
    centerY: Float,
    yaw: Float,
    zoom: Float,
    part: CostumePart,
    isSelected: Boolean
) {
    val colorPrimary = Color(part.primaryColor)
    val colorSecondary = Color(part.secondaryColor)
    val topY = centerY - 45f * zoom
    val beltWidth = 65f * zoom

    val beltRect = Size(beltWidth * 2, 22f * zoom)
    val topLeft = Offset(centerX - beltWidth, topY)

    drawRect(
        color = colorPrimary,
        topLeft = topLeft,
        size = beltRect
    )

    // Center Ornate Buckle
    drawRoundRect(
        color = colorSecondary,
        topLeft = Offset(centerX - 16f * zoom, topY - 4f * zoom),
        size = Size(32f * zoom, 30f * zoom),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )

    if (isSelected) {
        drawRect(
            color = GoldAccent,
            topLeft = topLeft,
            size = beltRect,
            style = Stroke(width = 3f * zoom)
        )
    }
}

private fun DrawScope.drawFootwearLayer(
    centerX: Float,
    centerY: Float,
    yaw: Float,
    zoom: Float,
    part: CostumePart,
    isSelected: Boolean
) {
    val colorPrimary = Color(part.primaryColor)
    val topY = centerY + 90f * zoom

    // Left Boot
    drawRoundRect(
        color = colorPrimary,
        topLeft = Offset(centerX - 45f * zoom, topY),
        size = Size(28f * zoom, 80f * zoom),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
    )

    // Right Boot
    drawRoundRect(
        color = colorPrimary,
        topLeft = Offset(centerX + 17f * zoom, topY),
        size = Size(28f * zoom, 80f * zoom),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
    )

    if (isSelected) {
        drawRoundRect(
            color = GoldAccent,
            topLeft = Offset(centerX - 47f * zoom, topY - 2f),
            size = Size(32f * zoom, 84f * zoom),
            style = Stroke(width = 3f * zoom)
        )
        drawRoundRect(
            color = GoldAccent,
            topLeft = Offset(centerX + 15f * zoom, topY - 2f),
            size = Size(32f * zoom, 84f * zoom),
            style = Stroke(width = 3f * zoom)
        )
    }
}

private fun DrawScope.drawHeadwearLayer(
    centerX: Float,
    centerY: Float,
    yaw: Float,
    zoom: Float,
    part: CostumePart,
    isSelected: Boolean
) {
    val colorSecondary = Color(part.secondaryColor)
    val topY = centerY - 215f * zoom

    // Crown / Mask
    val crownPath = Path().apply {
        moveTo(centerX - 28f * zoom, topY + 15f * zoom)
        lineTo(centerX - 20f * zoom, topY - 15f * zoom)
        lineTo(centerX - 8f * zoom, topY + 5f * zoom)
        lineTo(centerX, topY - 25f * zoom) // Center peak
        lineTo(centerX + 8f * zoom, topY + 5f * zoom)
        lineTo(centerX + 20f * zoom, topY - 15f * zoom)
        lineTo(centerX + 28f * zoom, topY + 15f * zoom)
        close()
    }

    drawPath(crownPath, color = colorSecondary)

    if (isSelected) {
        drawPath(crownPath, color = GoldAccent, style = Stroke(width = 3f * zoom))
    }
}

private fun DrawScope.drawPatternOverlay(
    path: Path,
    part: CostumePart,
    cx: Float,
    topY: Float,
    w: Float,
    h: Float
) {
    when (part.patternName) {
        "Cyber Grid" -> {
            val step = 20f * part.patternScale
            var x = cx - w
            while (x <= cx + w) {
                drawLine(
                    color = Color(part.secondaryColor).copy(alpha = 0.4f),
                    start = Offset(x, topY),
                    end = Offset(x, topY + h),
                    strokeWidth = 1.5f
                )
                x += step
            }
        }
        "Damask Floral", "Gold Embroidery" -> {
            drawCircle(
                color = Color(part.secondaryColor).copy(alpha = 0.35f),
                radius = 18f * part.patternScale,
                center = Offset(cx, topY + h * 0.4f),
                style = Stroke(width = 2f)
            )
        }
    }
}
