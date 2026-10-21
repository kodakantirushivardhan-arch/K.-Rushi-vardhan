package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun DiscoveryControlBar(
    modifier: Modifier = Modifier,
    onRewind: () -> Unit,
    onPass: () -> Unit,
    onSuperSpark: () -> Unit,
    onLike: () -> Unit,
    onBoost: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rewind Button
        ActionButton(
            size = 48,
            icon = Icons.Default.Replay,
            iconColor = Color(0xFFD97706),
            backgroundColor = Color.White,
            contentDesc = "Rewind Swipe",
            testTag = "rewind_button",
            onClick = onRewind
        )

        // Pass Button
        ActionButton(
            size = 58,
            icon = Icons.Default.Close,
            iconColor = SparkRuby,
            backgroundColor = Color.White,
            contentDesc = "Pass Profile",
            testTag = "pass_button",
            onClick = onPass
        )

        // Super Spark Button
        ActionButton(
            size = 48,
            icon = Icons.Default.Star,
            iconColor = SparkCyan,
            backgroundColor = Color.White,
            contentDesc = "Super Spark",
            testTag = "super_spark_button",
            onClick = onSuperSpark
        )

        // Like Button
        ActionButton(
            size = 64,
            icon = Icons.Default.Favorite,
            iconColor = Color.White,
            backgroundBrush = Brush.linearGradient(listOf(SparkCrimson, SparkSunset)),
            contentDesc = "Like Profile",
            testTag = "like_button",
            onClick = onLike
        )

        // Boost Button
        ActionButton(
            size = 48,
            icon = Icons.Default.Bolt,
            iconColor = Color(0xFF8B5CF6),
            backgroundColor = Color.White,
            contentDesc = "Boost Profile",
            testTag = "boost_button",
            onClick = onBoost
        )
    }
}

@Composable
private fun ActionButton(
    size: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    backgroundColor: Color = Color.White,
    backgroundBrush: Brush? = null,
    contentDesc: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(size.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .testTag(testTag),
        shape = CircleShape,
        color = if (backgroundBrush != null) Color.Transparent else backgroundColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (backgroundBrush != null) Modifier.background(backgroundBrush)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = iconColor,
                modifier = Modifier.size((size * 0.52f).dp)
            )
        }
    }
}
