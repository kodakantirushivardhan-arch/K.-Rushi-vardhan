package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    user: UserProfile,
    modifier: Modifier = Modifier,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit,
    onSwipedUp: () -> Unit,
    onInfoClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val rotation = (offsetX.value / 35f).coerceIn(-15f, 15f)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .rotate(rotation)
            .pointerInput(user.id) {
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            val dragDistanceX = offsetX.value
                            val dragDistanceY = offsetY.value

                            when {
                                dragDistanceX > 300f -> {
                                    offsetX.animateTo(1000f, spring())
                                    onSwipedRight()
                                }
                                dragDistanceX < -300f -> {
                                    offsetX.animateTo(-1000f, spring())
                                    onSwipedLeft()
                                }
                                dragDistanceY < -250f -> {
                                    offsetY.animateTo(-1000f, spring())
                                    onSwipedUp()
                                }
                                else -> {
                                    offsetX.animateTo(0f, spring())
                                    offsetY.animateTo(0f, spring())
                                }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                )
            }
            .testTag("profile_card_${user.id}")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image / Avatar
            if (user.photoUrls.isNotEmpty()) {
                AsyncImage(
                    model = user.photoUrls.first(),
                    contentDescription = user.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(user.avatarBgColor), Slate900)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(1),
                        fontSize = 120.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Dark gradient scrim for legible text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Dynamic Swipe Stamps (LIKE / NOPE / SPARK)
            if (offsetX.value > 60f) {
                val alpha = (offsetX.value / 250f).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.TopStart)
                        .rotate(-15f)
                        .border(4.dp, SparkEmerald.copy(alpha = alpha), RoundedCornerShape(12.dp))
                        .background(SparkEmerald.copy(alpha = 0.2f * alpha), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "LIKE",
                        color = SparkEmerald.copy(alpha = alpha),
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = 2.sp
                    )
                }
            } else if (offsetX.value < -60f) {
                val alpha = (abs(offsetX.value) / 250f).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.TopEnd)
                        .rotate(15f)
                        .border(4.dp, SparkRuby.copy(alpha = alpha), RoundedCornerShape(12.dp))
                        .background(SparkRuby.copy(alpha = 0.2f * alpha), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "NOPE",
                        color = SparkRuby.copy(alpha = alpha),
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = 2.sp
                    )
                }
            } else if (offsetY.value < -60f) {
                val alpha = (abs(offsetY.value) / 250f).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.TopCenter)
                        .border(4.dp, SparkGold.copy(alpha = alpha), RoundedCornerShape(12.dp))
                        .background(SparkGold.copy(alpha = 0.2f * alpha), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "SUPER SPARK",
                        color = SparkGold.copy(alpha = alpha),
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Bottom Profile Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Native Indian Greeting Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SparkGold.copy(alpha = 0.25f),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(SparkGold, SparkSunset))
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✨ ${user.nativeGreeting}",
                            color = Color(0xFFFEF08A),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Name & Age & Verified Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${user.name}, ${user.age}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    if (user.verified) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Profile",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier
                                .size(26.dp)
                                .padding(end = 6.dp)
                        )
                    }

                    // Info button to see details
                    IconButton(
                        onClick = onInfoClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .testTag("info_button_${user.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "View Profile Details",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Location & Distance
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = SparkSunset,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${user.city}, ${user.state} • ${user.distanceKm} km away",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Occupation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Occupation",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.occupation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                // Indian Cultural Interests Chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    user.interests.take(4).forEach { interest ->
                        val emoji = when (interest.lowercase()) {
                            "cricket" -> "🏏"
                            "chai" -> "☕"
                            "filter coffee" -> "☕"
                            "biryani" -> "🍲"
                            "trekking" -> "🏔️"
                            "badminton" -> "🏸"
                            "bollywood" -> "🎬"
                            "carnatic music" -> "🎶"
                            "kathakali" -> "🎭"
                            "street food" -> "🍛"
                            "startups" -> "🚀"
                            else -> "✨"
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.18f),
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                text = "$emoji $interest",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
