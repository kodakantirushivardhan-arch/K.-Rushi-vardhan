package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.localization.SparkStrings
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun MatchCelebrationDialog(
    matchedUser: UserProfile,
    strings: SparkStrings,
    onSendMessage: (prefilledText: String) -> Unit,
    onKeepSwiping: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Dialog(
        onDismissRequest = onKeepSwiping,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0F172A).copy(alpha = 0.95f),
                            Color(0xFF4C0519).copy(alpha = 0.98f),
                            Color(0xFF0F172A).copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(24.dp)
                .testTag("match_celebration_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Sparks floating banner
                Text(
                    text = strings.itsASpark,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp,
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFFDE047), Color(0xFFF97316), Color(0xFFFB7185))
                        )
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = String.format(strings.mutualMatchDesc, matchedUser.name),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Overlapping Avatars
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Current user avatar (left)
                    Box(
                        modifier = Modifier
                            .offset(x = (-42).dp)
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(3.dp, SparkGold, CircleShape)
                            .background(SparkCrimson),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "You",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    // Matched user avatar (right)
                    Box(
                        modifier = Modifier
                            .offset(x = 42.dp)
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFF38BDF8), CircleShape)
                            .background(Color(matchedUser.avatarBgColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (matchedUser.photoUrls.isNotEmpty()) {
                            AsyncImage(
                                model = matchedUser.photoUrls.first(),
                                contentDescription = matchedUser.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = matchedUser.name.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        }
                    }

                    // Floating heart between avatars
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(SparkCrimson, SparkSunset))
                            )
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Match",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Action 1: Send Chai Icebreaker
                Button(
                    onClick = {
                        onSendMessage("Namaste ${matchedUser.name}! Sending you a warm cup of Masala Chai to celebrate our Spark ☕✨")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SparkSunset
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("send_chai_button")
                ) {
                    Text(
                        text = strings.sendChai,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action 2: Say Hello
                OutlinedButton(
                    onClick = {
                        onSendMessage("Hey ${matchedUser.name}! ${matchedUser.nativeGreeting} Great to match with you!")
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder().copy(
                        brush = Brush.horizontalGradient(listOf(SparkGold, SparkCrimson))
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("say_hi_button")
                ) {
                    Text(
                        text = strings.sayHi,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action 3: Keep Swiping
                TextButton(
                    onClick = onKeepSwiping,
                    modifier = Modifier.testTag("keep_swiping_button")
                ) {
                    Text(
                        text = strings.keepSwiping,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
