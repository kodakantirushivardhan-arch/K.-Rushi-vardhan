package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.localization.SparkStrings
import com.example.model.ChatMessage
import com.example.model.Match
import com.example.model.MessageStatus
import com.example.ui.components.ReportBlockDialog
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    match: Match,
    messages: List<ChatMessage>,
    strings: SparkStrings,
    isRecordingVoice: Boolean,
    recordingTimerSec: Int,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onStartVoiceRecording: () -> Unit,
    onStopVoiceRecording: (send: Boolean) -> Unit,
    onReportUser: (reason: String) -> Unit,
    onBlockUser: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var showSafetyDialog by remember { mutableStateOf(false) }
    var playingAudioId by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("chat_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(match.user.avatarBgColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (match.user.photoUrls.isNotEmpty()) {
                                AsyncImage(
                                    model = match.user.photoUrls.first(),
                                    contentDescription = match.user.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = match.user.name.take(1),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = match.user.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                )
                                if (match.user.verified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Active now • ${match.user.city}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SparkEmerald,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    // Safety Shield (Report / Block)
                    IconButton(
                        onClick = { showSafetyDialog = true },
                        modifier = Modifier.testTag("chat_safety_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Safety Options",
                            tint = SparkCrimson
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                // Safety Banner inside chat
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SparkSunset,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Messages are end-to-end simulated & protected by Spark Safety.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                items(messages) { message ->
                    val isMe = message.senderId == "current_user"
                    MessageBubble(
                        message = message,
                        isMe = isMe,
                        isPlayingAudio = playingAudioId == message.id,
                        onTogglePlayAudio = {
                            playingAudioId = if (playingAudioId == message.id) null else message.id
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Indian Icebreakers Quick Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 6.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val icebreakers = listOf(
                        strings.icebreakerChai,
                        strings.icebreakerBiryani,
                        strings.icebreakerCricket,
                        strings.icebreakerWeekend
                    )
                    items(icebreakers) { chip ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SparkCrimson.copy(alpha = 0.1f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    onSendMessage(chip)
                                }
                        ) {
                            Text(
                                text = chip,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = SparkCrimson,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))

            // Bottom Input Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isRecordingVoice) {
                    // Recording Mode
                    val infiniteTransition = rememberInfiniteTransition(label = "rec_pulse")
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 0.9f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "recScale"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(SparkRuby)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Recording: 0:${recordingTimerSec.toString().padStart(2, '0')}",
                                fontWeight = FontWeight.Bold,
                                color = SparkRuby,
                                fontSize = 15.sp
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Cancel
                            IconButton(
                                onClick = { onStopVoiceRecording(false) },
                                modifier = Modifier.testTag("cancel_voice_record")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Cancel", tint = Slate400)
                            }

                            // Send Voice Note
                            IconButton(
                                onClick = { onStopVoiceRecording(true) },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(SparkCrimson)
                                    .testTag("send_voice_record")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Voice", tint = Color.White)
                            }
                        }
                    }
                } else {
                    // Standard Text & Mic Input
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text(strings.typeMessage, fontSize = 14.sp) },
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SparkCrimson,
                                unfocusedBorderColor = Slate200
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (inputText.isNotBlank()) {
                            // Send Text Button
                            IconButton(
                                onClick = {
                                    val text = inputText.trim()
                                    inputText = ""
                                    onSendMessage(text)
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SparkCrimson)
                                    .testTag("send_message_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            // Voice Note Mic Button
                            IconButton(
                                onClick = onStartVoiceRecording,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SparkSunset)
                                    .testTag("voice_note_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Record Voice Note",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSafetyDialog) {
        ReportBlockDialog(
            user = match.user,
            onDismiss = { showSafetyDialog = false },
            onReport = { reason ->
                showSafetyDialog = false
                onReportUser(reason)
            },
            onBlock = {
                showSafetyDialog = false
                onBlockUser()
            }
        )
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    isMe: Boolean,
    isPlayingAudio: Boolean,
    onTogglePlayAudio: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isMe) 18.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 18.dp
            ),
            color = if (isMe) SparkCrimson else MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (message.isVoiceNote) {
                // Voice note playback bubble
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = onTogglePlayAudio,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isMe) Color.White.copy(alpha = 0.25f) else SparkSunset.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlayingAudio) "Pause" else "Play",
                            tint = if (isMe) Color.White else SparkSunset
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Audio waveform visualization
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val heights = listOf(14, 24, 18, 30, 22, 28, 16, 26, 12, 20)
                        heights.forEachIndexed { index, h ->
                            val active = isPlayingAudio && (index % 2 == 0)
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(if (active) (h + 6).dp else h.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (isMe) Color.White.copy(alpha = if (active) 1f else 0.6f)
                                        else SparkSunset.copy(alpha = if (active) 1f else 0.5f)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "0:${message.voiceDurationSec.toString().padStart(2, '0')}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }

        // Timestamp and read tick status
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp, end = 4.dp)
        ) {
            Text(
                text = message.timeFormatted,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )

            if (isMe) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = "Read",
                    tint = if (message.status == MessageStatus.READ) Color(0xFF38BDF8) else Slate400,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
