package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.SparkCrimson
import com.example.ui.theme.SparkGold
import com.example.ui.theme.SparkSunset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SparkTopBar(
    currentLanguage: AppLanguage,
    isGold: Boolean,
    onLanguageClick: () -> Unit,
    onFilterClick: () -> Unit,
    onGoldClick: () -> Unit,
    onSafetyClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("app_logo_title")
            ) {
                // Spark glowing flame icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(SparkCrimson, SparkSunset, SparkGold)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Spark Flame Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Spark",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        brush = Brush.horizontalGradient(
                            listOf(SparkCrimson, SparkSunset)
                        )
                    )
                )
            }
        },
        actions = {
            // Language Switcher Chip
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onLanguageClick() }
                    .testTag("language_selector_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.ENGLISH -> "EN"
                            AppLanguage.TELUGU -> "తెలుగు"
                            AppLanguage.HINDI -> "हिन्दी"
                            AppLanguage.TAMIL -> "தமிழ்"
                            AppLanguage.MALAYALAM -> "മലയാളം"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Spark Gold button
            IconButton(
                onClick = onGoldClick,
                modifier = Modifier.testTag("gold_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Spark Gold",
                        tint = if (isGold) SparkGold else Color(0xFFD97706)
                    )
                }
            }

            // Filter button
            IconButton(
                onClick = onFilterClick,
                modifier = Modifier.testTag("filter_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter Discovery",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Safety Shield button
            IconButton(
                onClick = onSafetyClick,
                modifier = Modifier.testTag("safety_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Safety Center",
                    tint = SparkCrimson
                )
            }
        }
    )
}
