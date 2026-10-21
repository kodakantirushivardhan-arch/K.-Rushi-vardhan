package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.SparkStrings
import com.example.model.FilterState
import com.example.ui.theme.SparkCrimson
import com.example.ui.theme.SparkSunset

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    currentFilter: FilterState,
    strings: SparkStrings,
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit,
    onReset: () -> Unit
) {
    var maxDistance by remember { mutableFloatStateOf(currentFilter.maxDistanceKm.toFloat()) }
    var selectedCity by remember { mutableStateOf(currentFilter.selectedCity) }
    var selectedInterests by remember { mutableStateOf(currentFilter.selectedInterests) }
    var verifiedOnly by remember { mutableStateOf(currentFilter.verifiedOnly) }

    val cities = listOf(
        "All Cities",
        "Hyderabad",
        "Mumbai",
        "Bengaluru",
        "Chennai",
        "Delhi NCR",
        "Kochi",
        "Pune",
        "Kolkata"
    )

    val allInterests = listOf(
        "Cricket" to "🏏",
        "Biryani" to "🍲",
        "Filter Coffee" to "☕",
        "Chai" to "☕",
        "Trekking" to "🏔️",
        "Badminton" to "🏸",
        "Bollywood" to "🎬",
        "Carnatic Music" to "🎶",
        "Kathakali" to "🎭",
        "Street Food" to "🍛",
        "Startups" to "🚀"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("filter_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.filterTitle,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("filter_close_button")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Distance Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.distanceFilter,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "${maxDistance.toInt()} km",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SparkCrimson
                    )
                )
            }

            Slider(
                value = maxDistance,
                onValueChange = { maxDistance = it },
                valueRange = 5f..100f,
                steps = 19,
                colors = SliderDefaults.colors(
                    thumbColor = SparkCrimson,
                    activeTrackColor = SparkCrimson
                ),
                modifier = Modifier.testTag("distance_slider")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // City Selector
            Text(
                text = strings.cityFilter,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                cities.forEach { city ->
                    val isSelected = selectedCity == city
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCity = city },
                        label = { Text(city) },
                        leadingIcon = if (isSelected) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SparkCrimson.copy(alpha = 0.15f),
                            selectedLabelColor = SparkCrimson
                        ),
                        modifier = Modifier.testTag("city_chip_$city")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Cultural Interests
            Text(
                text = strings.interestsFilter,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                allInterests.forEach { (interest, emoji) ->
                    val isSelected = interest in selectedInterests
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedInterests = if (isSelected) {
                                selectedInterests - interest
                            } else {
                                selectedInterests + interest
                            }
                        },
                        label = { Text("$emoji $interest") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SparkSunset.copy(alpha = 0.18f),
                            selectedLabelColor = SparkSunset
                        ),
                        modifier = Modifier.testTag("interest_chip_$interest")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Verified Only Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.verifiedOnly,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Show profiles with blue check verification",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = verifiedOnly,
                    onCheckedChange = { verifiedOnly = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = SparkCrimson, checkedTrackColor = SparkCrimson.copy(alpha = 0.5f)),
                    modifier = Modifier.testTag("verified_only_switch")
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Buttons (Apply & Reset)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        maxDistance = 50f
                        selectedCity = "All Cities"
                        selectedInterests = emptySet()
                        verifiedOnly = false
                        onReset()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("reset_filters_button")
                ) {
                    Text(strings.resetFilters)
                }

                Button(
                    onClick = {
                        onApply(
                            FilterState(
                                maxDistanceKm = maxDistance.toInt(),
                                selectedCity = selectedCity,
                                selectedInterests = selectedInterests,
                                verifiedOnly = verifiedOnly
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SparkCrimson),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("apply_filters_button")
                ) {
                    Text(strings.applyFilters, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
