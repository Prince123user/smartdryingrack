package com.example.smartdryingrack.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdryingrack.core.designsystem.AppColors
import com.example.smartdryingrack.core.designsystem.Dimens
import com.example.smartdryingrack.core.designsystem.FuturisticToggle
import com.example.smartdryingrack.core.designsystem.GlassCard
import com.example.smartdryingrack.core.designsystem.LogoVariant
import com.example.smartdryingrack.core.designsystem.SmartDryingRackLogo
import kotlinx.coroutines.launch

data class OnboardingPage(
    val step: String,
    val title: String,
    val description: String,
    val type: Int
)

val onboardingPages = listOf(
    OnboardingPage("01/04", "Smart Drying", "Your clothes. Protected automatically.", 0),
    OnboardingPage("02/04", "Smart Weather", "Know when rain is coming before it reaches your clothes.", 1),
    OnboardingPage("03/04", "Automatic Protection", "Your rack retracts automatically when rain is detected.", 2),
    OnboardingPage("04/04", "Smart Scheduling", "Set your preferred schedule and let the system handle the rest.", 3)
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit, onConnect: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppColors.Background, AppColors.BackgroundAlt)))
            .padding(Dimens.ScreenPadding)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Smart Drying Rack",
                    color = AppColors.TextSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
                if (pagerState.currentPage < onboardingPages.size - 1) {
                    Text(
                        "Skip",
                        color = AppColors.Primary,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.clickable {
                            scope.launch { pagerState.animateScrollToPage(onboardingPages.size - 1) }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val p = onboardingPages[page]
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        p.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = AppColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        p.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(28.dp))

                    when (p.type) {
                        0 -> OnboardingIllustration1()
                        1 -> OnboardingIllustration2()
                        2 -> OnboardingIllustration3()
                        else -> OnboardingIllustration4()
                    }
                }
            }

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    Modifier.padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    onboardingPages.indices.forEach { index ->
                        val active = pagerState.currentPage == index
                        Box(
                            Modifier
                                .size(if (active) 24.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (active) AppColors.Primary else AppColors.Border)
                        )
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        OutlinedButton(
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Back", color = AppColors.TextPrimary)
                        }
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }

                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        Button(
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                        ) {
                            Text("Continue ›", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.End) {
                            Button(
                                onClick = onConnect,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                            ) {
                                Text("Get Started ›", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "I'll do this later",
                                color = AppColors.TextSecondary,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.clickable(onClick = onFinish)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingIllustration1() {
    Box(
        Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        SmartDryingRackLogo(size = 150.dp, variant = LogoVariant.SmartDryingRackLogo, glow = true)
    }
}

@Composable
private fun OnboardingIllustration2() {
    GlassCard(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("31°", style = MaterialTheme.typography.headlineLarge, color = AppColors.TextPrimary, fontWeight = FontWeight.Bold)
                Text("Partly Cloudy", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
            }
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.Warning.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("Rain chance 60% at 4 PM", color = AppColors.Warning, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Humidity 68%", color = AppColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
            Text("Wind 12 km/h", color = AppColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun OnboardingIllustration3() {
    GlassCard(Modifier.fillMaxWidth(), accent = AppColors.Danger) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = AppColors.Danger, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Rain detected", style = MaterialTheme.typography.titleMedium, color = AppColors.Danger, fontWeight = FontWeight.Bold)
                Text("Retracting...", style = MaterialTheme.typography.bodyMedium, color = AppColors.TextSecondary)
            }
        }
    }
}

@Composable
private fun OnboardingIllustration4() {
    GlassCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Rack Out", style = MaterialTheme.typography.labelSmall, color = AppColors.TextSecondary)
                Text("8:00 AM", style = MaterialTheme.typography.titleMedium, color = AppColors.Primary, fontWeight = FontWeight.Bold)
            }
            Column {
                Text("Rack In", style = MaterialTheme.typography.labelSmall, color = AppColors.TextSecondary)
                Text("5:00 PM", style = MaterialTheme.typography.titleMedium, color = AppColors.Primary, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEachIndexed { index, day ->
                val active = index < 5
                Box(
                    Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (active) AppColors.Primary else AppColors.CardAlt)
                        .border(1.dp, if (active) AppColors.Primary else AppColors.Border, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day, color = if (active) Color.White else AppColors.TextSecondary, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        FuturisticToggle("Weather Protection", checked = true) {}
    }
}
