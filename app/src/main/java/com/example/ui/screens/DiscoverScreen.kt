package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.SparkStrings
import com.example.model.UserProfile
import com.example.ui.components.DiscoveryControlBar
import com.example.ui.components.ProfileCard
import com.example.ui.theme.SparkCrimson

@Composable
fun DiscoverScreen(
    deck: List<UserProfile>,
    cardIndex: Int,
    strings: SparkStrings,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeUp: () -> Unit,
    onRewind: () -> Unit,
    onBoost: () -> Unit,
    onOpenFilters: () -> Unit,
    onResetFilters: () -> Unit,
    onInspectProfile: (UserProfile) -> Unit
) {
    val remainingCards = if (cardIndex < deck.size) deck.subList(cardIndex, deck.size) else emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("discover_screen")
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (remainingCards.isNotEmpty()) {
                // Background card (peeking)
                if (remainingCards.size > 1) {
                    val nextUser = remainingCards[1]
                    ProfileCard(
                        user = nextUser,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, bottom = 0.dp),
                        onSwipedLeft = {},
                        onSwipedRight = {},
                        onSwipedUp = {},
                        onInfoClick = { onInspectProfile(nextUser) }
                    )
                }

                // Foreground active card
                val currentUser = remainingCards[0]
                ProfileCard(
                    user = currentUser,
                    modifier = Modifier.fillMaxSize(),
                    onSwipedLeft = onSwipeLeft,
                    onSwipedRight = onSwipeRight,
                    onSwipedUp = onSwipeUp,
                    onInfoClick = { onInspectProfile(currentUser) }
                )
            } else {
                // Empty state when all profiles have been swiped
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .testTag("empty_deck_view")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(SparkCrimson.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = SparkCrimson,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = strings.noMoreProfiles,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = strings.resetFiltersTip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onOpenFilters,
                            colors = ButtonDefaults.buttonColors(containerColor = SparkCrimson),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("open_filters_from_empty_button")
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Adjust Filters", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = onResetFilters,
                            modifier = Modifier.testTag("reset_deck_button")
                        ) {
                            Text("Reset All Swipes", color = SparkCrimson)
                        }
                    }
                }
            }
        }

        // Floating Control Bar
        DiscoveryControlBar(
            onRewind = onRewind,
            onPass = onSwipeLeft,
            onSuperSpark = onSwipeUp,
            onLike = onSwipeRight,
            onBoost = onBoost
        )
    }
}
