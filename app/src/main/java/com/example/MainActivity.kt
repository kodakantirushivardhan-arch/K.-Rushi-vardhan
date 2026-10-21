package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.SparkCrimson
import com.example.ui.theme.SparkTheme
import com.example.viewmodel.SparkViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SparkTheme {
                SparkApp()
            }
        }
    }
}

@Composable
fun SparkApp(viewModel: SparkViewModel = viewModel()) {
    val context = LocalContext.current

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val strings by viewModel.strings.collectAsStateWithLifecycle()

    val deck by viewModel.deck.collectAsStateWithLifecycle()
    val cardIndex by viewModel.cardIndex.collectAsStateWithLifecycle()
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val allChatMessages by viewModel.allChatMessages.collectAsStateWithLifecycle()

    val activeChat by viewModel.activeChat.collectAsStateWithLifecycle()
    val isRecordingVoice by viewModel.isRecordingVoice.collectAsStateWithLifecycle()
    val recordingTimerSec by viewModel.recordingTimerSec.collectAsStateWithLifecycle()

    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val isFilterSheetOpen by viewModel.isFilterSheetOpen.collectAsStateWithLifecycle()
    val isGoldSheetOpen by viewModel.isGoldSheetOpen.collectAsStateWithLifecycle()
    val isSafetySheetOpen by viewModel.isSafetySheetOpen.collectAsStateWithLifecycle()
    val isLanguageDialogOpen by viewModel.isLanguageDialogOpen.collectAsStateWithLifecycle()
    val inspectingProfile by viewModel.inspectingProfile.collectAsStateWithLifecycle()
    val isSparkCelebration by viewModel.isSparkCelebration.collectAsStateWithLifecycle()
    val isGoldMember by viewModel.isGoldMember.collectAsStateWithLifecycle()

    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // Handle toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Back handling when inside chat or inspecting profile
    BackHandler(enabled = activeChat != null || inspectingProfile != null) {
        if (activeChat != null) {
            viewModel.closeChat()
        } else if (inspectingProfile != null) {
            viewModel.setInspectingProfile(null)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (activeChat == null) {
                SparkTopBar(
                    currentLanguage = currentLanguage,
                    isGold = isGoldMember,
                    onLanguageClick = { viewModel.setLanguageDialogOpen(true) },
                    onFilterClick = { viewModel.setFilterSheetOpen(true) },
                    onGoldClick = { viewModel.setGoldSheetOpen(true) },
                    onSafetyClick = { viewModel.setSafetySheetOpen(true) }
                )
            }
        },
        bottomBar = {
            if (activeChat == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("spark_bottom_navigation")
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == 0) Icons.Default.LocalFireDepartment else Icons.Outlined.LocalFireDepartment,
                                contentDescription = strings.discover,
                                tint = if (currentTab == 0) SparkCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = { Text(strings.discover) },
                        modifier = Modifier.testTag("nav_tab_discover")
                    )

                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (matches.any { it.unreadCount > 0 }) {
                                        Badge { Text(matches.sumOf { it.unreadCount }.toString()) }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == 1) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = strings.matches,
                                    tint = if (currentTab == 1) SparkCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        label = { Text(strings.matches) },
                        modifier = Modifier.testTag("nav_tab_matches")
                    )

                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == 2) Icons.Default.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                contentDescription = strings.messages,
                                tint = if (currentTab == 2) SparkCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = { Text(strings.messages) },
                        modifier = Modifier.testTag("nav_tab_messages")
                    )

                    NavigationBarItem(
                        selected = currentTab == 3,
                        onClick = { viewModel.selectTab(3) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == 3) Icons.Default.Person else Icons.Outlined.Person,
                                contentDescription = strings.profile,
                                tint = if (currentTab == 3) SparkCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = { Text(strings.profile) },
                        modifier = Modifier.testTag("nav_tab_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            val currentActiveChat = activeChat
            if (currentActiveChat != null) {
                // Chat Detail View
                val currentMessages = allChatMessages[currentActiveChat.id] ?: emptyList()
                ChatDetailScreen(
                    match = currentActiveChat,
                    messages = currentMessages,
                    strings = strings,
                    isRecordingVoice = isRecordingVoice,
                    recordingTimerSec = recordingTimerSec,
                    onBack = { viewModel.closeChat() },
                    onSendMessage = { text ->
                        viewModel.sendMessage(currentActiveChat.id, text)
                    },
                    onStartVoiceRecording = {
                        viewModel.startVoiceRecording()
                    },
                    onStopVoiceRecording = { send ->
                        viewModel.stopVoiceRecording(send)
                    },
                    onReportUser = { reason ->
                        viewModel.reportUser(currentActiveChat.user, reason)
                    },
                    onBlockUser = {
                        viewModel.blockUser(currentActiveChat.user)
                    }
                )
            } else {
                when (currentTab) {
                    0 -> DiscoverScreen(
                        deck = deck,
                        cardIndex = cardIndex,
                        strings = strings,
                        onSwipeLeft = { viewModel.swipePass() },
                        onSwipeRight = { viewModel.swipeLike() },
                        onSwipeUp = { viewModel.swipeSuperSpark() },
                        onRewind = { viewModel.rewind() },
                        onBoost = { viewModel.activateBoost() },
                        onOpenFilters = { viewModel.setFilterSheetOpen(true) },
                        onResetFilters = { viewModel.resetFilters() },
                        onInspectProfile = { user -> viewModel.setInspectingProfile(user) }
                    )

                    1, 2 -> MatchesScreen(
                        matches = matches,
                        admirers = viewModel.admirers,
                        isGold = isGoldMember,
                        strings = strings,
                        onOpenChat = { match -> viewModel.openChat(match) },
                        onOpenGold = { viewModel.setGoldSheetOpen(true) },
                        onMatchAdmirer = { admirer ->
                            val match = viewModel.swipeLike()
                            viewModel.showToast("Matched with ${admirer.name}! ✨")
                        }
                    )

                    3 -> ProfileScreen(
                        currentLanguage = currentLanguage,
                        isGold = isGoldMember,
                        strings = strings,
                        onOpenLanguage = { viewModel.setLanguageDialogOpen(true) },
                        onOpenFilters = { viewModel.setFilterSheetOpen(true) },
                        onOpenGold = { viewModel.setGoldSheetOpen(true) },
                        onOpenSafety = { viewModel.setSafetySheetOpen(true) },
                        onBoost = { viewModel.activateBoost() }
                    )
                }
            }
        }
    }

    // Modal: Match Celebration Dialog
    isSparkCelebration?.let { user ->
        MatchCelebrationDialog(
            matchedUser = user,
            strings = strings,
            onSendMessage = { initialText ->
                viewModel.openChatFromCelebration(user)
                val match = matches.find { it.user.id == user.id }
                if (match != null) {
                    viewModel.sendMessage(match.id, initialText)
                }
            },
            onKeepSwiping = { viewModel.dismissCelebration() }
        )
    }

    // Sheet: Filter Bottom Sheet
    if (isFilterSheetOpen) {
        FilterBottomSheet(
            currentFilter = filterState,
            strings = strings,
            onDismiss = { viewModel.setFilterSheetOpen(false) },
            onApply = { newFilters -> viewModel.updateFilters(newFilters) },
            onReset = { viewModel.resetFilters() }
        )
    }

    // Sheet: Spark Gold Subscription Sheet
    if (isGoldSheetOpen) {
        SparkGoldSheet(
            strings = strings,
            isAlreadyGold = isGoldMember,
            onDismiss = { viewModel.setGoldSheetOpen(false) },
            onSelectPlan = { plan -> viewModel.upgradeToGold(plan) }
        )
    }

    // Dialog: Safety Center & Guidelines
    if (isSafetySheetOpen) {
        SafetyCenterDialog(
            strings = strings,
            onDismiss = { viewModel.setSafetySheetOpen(false) }
        )
    }

    // Dialog: Language Switcher
    if (isLanguageDialogOpen) {
        LanguageDialog(
            currentLanguage = currentLanguage,
            strings = strings,
            onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
            onDismiss = { viewModel.setLanguageDialogOpen(false) }
        )
    }

    // Sheet: Profile Detail View
    inspectingProfile?.let { user ->
        ProfileDetailSheet(
            user = user,
            onDismiss = { viewModel.setInspectingProfile(null) },
            onLike = {
                viewModel.setInspectingProfile(null)
                viewModel.swipeLike()
            },
            onPass = {
                viewModel.setInspectingProfile(null)
                viewModel.swipePass()
            },
            onReportOrBlock = {
                viewModel.setInspectingProfile(null)
                viewModel.blockUser(user)
            }
        )
    }
}
