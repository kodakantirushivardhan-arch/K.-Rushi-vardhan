package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.localization.SparkStrings
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    currentLanguage: AppLanguage,
    isGold: Boolean,
    strings: SparkStrings,
    onOpenLanguage: () -> Unit,
    onOpenFilters: () -> Unit,
    onOpenGold: () -> Unit,
    onOpenSafety: () -> Unit,
    onBoost: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Profile Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo / Avatar
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(SparkCrimson, SparkSunset))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "R",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp
                    )

                    // Small camera edit icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SparkGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit photo",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Rishi, 25",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Profile",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "📍 Hyderabad, Telangana • Software Engineer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Profile Completion Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SparkEmerald.copy(alpha = 0.12f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SparkEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Profile 100% Completed & Verified",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SparkEmerald
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Spark Gold Status Banner
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isGold) Color(0xFFFEF3C7) else MaterialTheme.colorScheme.surface
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(SparkGold, SparkSunset)),
                width = 2.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onOpenGold() }
                .testTag("spark_gold_banner_profile")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(SparkGold, SparkSunset))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Spark Gold",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isGold) strings.premiumActive else strings.sparkGold,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isGold) Color(0xFF78350F) else MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (isGold) "Unlimited likes & all admirers unlocked" else strings.goldTagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isGold) Color(0xFF92400E) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onOpenGold,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isGold) Color(0xFFD97706) else SparkCrimson
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (isGold) "Active" else "Upgrade",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings & Options Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                // Change Language
                ProfileSettingRow(
                    icon = Icons.Default.Translate,
                    title = strings.changeLanguage,
                    subtitle = currentLanguage.nativeName,
                    onClick = onOpenLanguage,
                    testTag = "setting_language"
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Slate100)

                // Discovery Filters
                ProfileSettingRow(
                    icon = Icons.Default.Tune,
                    title = strings.filterTitle,
                    subtitle = "Distance, Age, Indian Cultural Interests",
                    onClick = onOpenFilters,
                    testTag = "setting_filters"
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Slate100)

                // Safety Center
                ProfileSettingRow(
                    icon = Icons.Default.Shield,
                    title = strings.safetyCenter,
                    subtitle = "Guidelines, verification, Indian helplines",
                    onClick = onOpenSafety,
                    testTag = "setting_safety"
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Slate100)

                // Boost Profile
                ProfileSettingRow(
                    icon = Icons.Default.Bolt,
                    title = "Profile Boost 🚀",
                    subtitle = "Be the top profile in your city for 30 minutes",
                    onClick = onBoost,
                    testTag = "setting_boost"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Footer
        Text(
            text = "Spark v1.0.0 • Made with ❤️ for India",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Telugu • हिन्दी • தமிழ் • മലയാളം • English",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileSettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SparkCrimson,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(20.dp)
        )
    }
}
