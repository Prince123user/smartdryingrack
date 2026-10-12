package com.example.smartdryingrack.core.designsystem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.min

enum class LogoVariant { SmartDryingRackLogo, SmartDryingRackIcon, SmartDryingRackMonochrome }

@Composable
fun SmartDryingRackLogo(
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    variant: LogoVariant = LogoVariant.SmartDryingRackLogo,
    glow: Boolean = true,
) {
    val wire = when (variant) {
        LogoVariant.SmartDryingRackMonochrome -> AppColors.TextPrimary
        else -> AppColors.Primary
    }
    val secondary = when (variant) {
        LogoVariant.SmartDryingRackLogo -> AppColors.Cyan
        LogoVariant.SmartDryingRackIcon -> AppColors.Primary
        LogoVariant.SmartDryingRackMonochrome -> AppColors.TextPrimary
    }
    val leaf = if (variant == LogoVariant.SmartDryingRackMonochrome) AppColors.TextPrimary else AppColors.Success

    val glowAlpha = if (glow && variant != LogoVariant.SmartDryingRackMonochrome) {
        animateFloatAsState(1f, label = "logoGlow").value
    } else 0f

    val density = LocalDensity.current
    val sizePx = with(density) { size.toPx() }

    Canvas(modifier.size(size)) {
        val w = sizePx
        val h = sizePx
        val cx = w / 2f
        val cy = h / 2f
        val s = minOf(w, h)

        if (glowAlpha > 0f) {
            drawGlow(wire, cx, cy, s * 0.48f, 0.18f)
            drawGlow(secondary, cx, cy, s * 0.35f, 0.12f)
        }

        // Rack body (wire-frame look)
        val rackLeft = cx - s * 0.18f
        val rackRight = cx + s * 0.18f
        val rackTop = cy - s * 0.12f
        val rackBottom = cy + s * 0.22f
        val thick = s * 0.018f

        drawLine(wire, Offset(rackLeft, rackTop), Offset(rackLeft, rackBottom), thick)
        drawLine(wire, Offset(rackRight, rackTop), Offset(rackRight, rackBottom), thick)
        drawLine(wire, Offset(rackLeft, rackTop), Offset(rackRight, rackTop), thick)
        drawLine(wire.copy(alpha = 0.75f), Offset(rackLeft, rackBottom), Offset(rackRight, rackBottom), thick * 0.95f)

        // Clothes rods
        val rodStroke = s * 0.012f
        val rod1 = cy - s * 0.02f
        val rod2 = cy + s * 0.06f
        val rod3 = cy + s * 0.14f
        drawLine(secondary, Offset(rackLeft + s * 0.02f, rod1), Offset(rackRight - s * 0.02f, rod1), rodStroke)
        drawLine(secondary, Offset(rackLeft + s * 0.02f, rod2), Offset(rackRight - s * 0.02f, rod2), rodStroke)
        drawLine(secondary, Offset(rackLeft + s * 0.02f, rod3), Offset(rackRight - s * 0.02f, rod3), rodStroke)

        // Wi‑Fi arcs
        val arcStroke = s * 0.016f
        val arcY = cy - s * 0.17f
        drawArcStroke(secondary, Rect(cx - s * 0.18f, arcY - s * 0.18f, cx + s * 0.18f, arcY + s * 0.18f), 205f, 120f, arcStroke * 0.9f)
        drawArcStroke(wire, Rect(cx - s * 0.26f, arcY - s * 0.26f, cx + s * 0.26f, arcY + s * 0.26f), 205f, 120f, arcStroke)
        drawArcStroke(leaf, Rect(cx - s * 0.34f, arcY - s * 0.34f, cx + s * 0.34f, arcY + s * 0.34f), 205f, 120f, arcStroke * 0.8f)
        drawCircle(wire, s * 0.02f, Offset(cx, arcY + s * 0.02f))

        // Leaf accent
        val leafCenter = Offset(rackRight + s * 0.02f, rackTop + s * 0.06f)
        drawLeaf(leaf, leafCenter, s * 0.09f)

        // Optional icon frame
        if (variant == LogoVariant.SmartDryingRackIcon) {
            val pad = s * 0.14f
            drawRoundRect(
                color = wire.copy(alpha = 0.11f),
                topLeft = Offset(cx - s / 2f + pad, cy - s / 2f + pad),
                size = Size(s - 2 * pad, s - 2 * pad),
                cornerRadius = CornerRadius(s * 0.18f, s * 0.18f)
            )
        }
    }
}

private fun DrawScope.drawLine(color: Color, start: Offset, end: Offset, stroke: Float) {
    drawLine(color = color, start = start, end = end, strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawArcStroke(color: Color, oval: Rect, start: Float, sweep: Float, stroke: Float) {
    drawArc(color = color, startAngle = start, sweepAngle = sweep, useCenter = false, topLeft = oval.topLeft, size = oval.size,
        style = Stroke(width = stroke, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawGlow(color: Color, cx: Float, cy: Float, r: Float, alpha: Float) {
    drawCircle(color.copy(alpha = alpha), center = Offset(cx, cy), radius = r)
}

private fun DrawScope.drawLeaf(color: Color, center: Offset, scale: Float) {
    val p = Path().apply {
        moveTo(center.x, center.y - scale * 1.1f)
        cubicTo(center.x + scale * 0.9f, center.y - scale * 0.6f, center.x + scale * 0.75f, center.y + scale * 0.45f, center.x, center.y + scale * 1.0f)
        cubicTo(center.x - scale * 0.75f, center.y + scale * 0.45f, center.x - scale * 0.9f, center.y - scale * 0.6f, center.x, center.y - scale * 1.1f)
        close()
    }
    drawPath(p, color)
    drawLine(color.copy(alpha = 0.65f), Offset(center.x, center.y - scale * 0.9f), Offset(center.x + scale * 0.25f, center.y + scale * 0.85f), strokeWidth = scale * 0.12f, cap = StrokeCap.Round)
}
