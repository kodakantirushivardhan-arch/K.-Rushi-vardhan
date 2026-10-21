package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SparkRepository
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.localization.SparkStrings
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SparkViewModel(
    private val repository: SparkRepository = SparkRepository()
) : ViewModel() {

    // Language state
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _strings = MutableStateFlow(LocalizationManager.getStrings(AppLanguage.ENGLISH))
    val strings: StateFlow<SparkStrings> = _strings.asStateFlow()

    // Active bottom navigation tab: 0=Discover, 1=Matches, 2=Messages, 3=Profile
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Filters
    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // Profile card stack
    private val _deck = MutableStateFlow<List<UserProfile>>(emptyList())
    val deck: StateFlow<List<UserProfile>> = _deck.asStateFlow()

    private val _cardIndex = MutableStateFlow(0)
    val cardIndex: StateFlow<Int> = _cardIndex.asStateFlow()

    // History for Rewind
    private val swipeHistory = mutableListOf<UserProfile>()

    // Modal / Sheet visibility states
    private val _isSparkCelebration = MutableStateFlow<UserProfile?>(null)
    val isSparkCelebration: StateFlow<UserProfile?> = _isSparkCelebration.asStateFlow()

    private val _activeChat = MutableStateFlow<Match?>(null)
    val activeChat: StateFlow<Match?> = _activeChat.asStateFlow()

    private val _isFilterSheetOpen = MutableStateFlow(false)
    val isFilterSheetOpen: StateFlow<Boolean> = _isFilterSheetOpen.asStateFlow()

    private val _isGoldSheetOpen = MutableStateFlow(false)
    val isGoldSheetOpen: StateFlow<Boolean> = _isGoldSheetOpen.asStateFlow()

    private val _isSafetySheetOpen = MutableStateFlow(false)
    val isSafetySheetOpen: StateFlow<Boolean> = _isSafetySheetOpen.asStateFlow()

    private val _isLanguageDialogOpen = MutableStateFlow(false)
    val isLanguageDialogOpen: StateFlow<Boolean> = _isLanguageDialogOpen.asStateFlow()

    private val _inspectingProfile = MutableStateFlow<UserProfile?>(null)
    val inspectingProfile: StateFlow<UserProfile?> = _inspectingProfile.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Spark Gold & Admirers
    val isGoldMember: StateFlow<Boolean> = repository.isGoldMember
    val admirers: List<UserProfile> = repository.admirers

    // Matches & Chats
    val matches: StateFlow<List<Match>> = repository.matches
    val allChatMessages: StateFlow<Map<String, List<ChatMessage>>> = repository.chatMessages

    // Voice note simulation in chat
    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

    private val _recordingTimerSec = MutableStateFlow(0)
    val recordingTimerSec: StateFlow<Int> = _recordingTimerSec.asStateFlow()
    private var recordingJob: Job? = null

    init {
        refreshDeck()
    }

    fun selectTab(tab: Int) {
        _currentTab.value = tab
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
        _strings.value = LocalizationManager.getStrings(lang)
        _isLanguageDialogOpen.value = false
    }

    fun refreshDeck() {
        val filtered = repository.filterProfiles(_filterState.value)
        _deck.value = filtered
        _cardIndex.value = 0
    }

    fun updateFilters(newFilters: FilterState) {
        _filterState.value = newFilters
        _isFilterSheetOpen.value = false
        refreshDeck()
    }

    fun resetFilters() {
        _filterState.value = FilterState()
        refreshDeck()
    }

    fun swipePass() {
        val currentIndex = _cardIndex.value
        val list = _deck.value
        if (currentIndex < list.size) {
            val user = list[currentIndex]
            swipeHistory.add(user)
            _cardIndex.value = currentIndex + 1
        }
    }

    fun swipeLike() {
        val currentIndex = _cardIndex.value
        val list = _deck.value
        if (currentIndex < list.size) {
            val user = list[currentIndex]
            swipeHistory.add(user)
            _cardIndex.value = currentIndex + 1

            // Indian profiles mutual match chance:
            // High match likelihood to demonstrate interactive chat right away!
            val isMutual = user.id in listOf("user_1", "user_2", "user_4", "user_5", "user_7")
            if (isMutual) {
                repository.addMatch(user)
                _isSparkCelebration.value = user
            }
        }
    }

    fun swipeSuperSpark() {
        val currentIndex = _cardIndex.value
        val list = _deck.value
        if (currentIndex < list.size) {
            val user = list[currentIndex]
            swipeHistory.add(user)
            _cardIndex.value = currentIndex + 1
            // Super spark always sparks mutual interest!
            repository.addMatch(user)
            _isSparkCelebration.value = user
        }
    }

    fun rewind() {
        if (swipeHistory.isNotEmpty() && _cardIndex.value > 0) {
            swipeHistory.removeAt(swipeHistory.size - 1)
            _cardIndex.value = _cardIndex.value - 1
            showToast("Swipe Rewound! ⏪")
        } else {
            showToast("No swipes to rewind.")
        }
    }

    fun activateBoost() {
        showToast(strings.value.boostActivated)
    }

    fun dismissCelebration() {
        _isSparkCelebration.value = null
    }

    fun openChatFromCelebration(user: UserProfile) {
        _isSparkCelebration.value = null
        val match = repository.matches.value.find { it.user.id == user.id }
        if (match != null) {
            openChat(match)
        }
    }

    fun openChat(match: Match) {
        _activeChat.value = match
    }

    fun closeChat() {
        _activeChat.value = null
    }

    fun sendMessage(matchId: String, text: String) {
        if (text.isBlank()) return
        val currentMatch = _activeChat.value ?: return
        repository.sendMessage(matchId, text)

        // Simulate intelligent realistic Indian reply after 1.4s
        viewModelScope.launch {
            delay(1400)
            val replyText = generateSimulatedReply(currentMatch.user, text)
            repository.receiveSimulatedReply(matchId, currentMatch.user.id, replyText)
        }
    }

    fun startVoiceRecording() {
        _isRecordingVoice.value = true
        _recordingTimerSec.value = 0
        recordingJob = viewModelScope.launch {
            while (_isRecordingVoice.value) {
                delay(1000)
                _recordingTimerSec.value += 1
            }
        }
    }

    fun stopVoiceRecording(send: Boolean) {
        recordingJob?.cancel()
        val duration = _recordingTimerSec.value.coerceAtLeast(2)
        _isRecordingVoice.value = false
        if (send) {
            val currentMatch = _activeChat.value
            if (currentMatch != null) {
                repository.sendMessage(
                    matchId = currentMatch.id,
                    text = "🎤 Voice note (${duration}s)",
                    isVoice = true,
                    durationSec = duration
                )
                // Simulate audio response
                viewModelScope.launch {
                    delay(1600)
                    repository.receiveSimulatedReply(
                        currentMatch.id,
                        currentMatch.user.id,
                        "Awesome voice note! Your voice has such a warm vibe 😊 Let's plan a chai date soon!"
                    )
                }
            }
        }
    }

    private fun generateSimulatedReply(partner: UserProfile, prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("coffee") || lower.contains("chai") || lower.contains("tea") ->
                "Haha totally Irani chai with bun maska! Or maybe filter coffee on a rainy Sunday morning? ☕ What's your go-to spot?"
            lower.contains("biryani") || lower.contains("food") ->
                "Hyderabadi Dum Biryani with extra mirchi ka salan, without a doubt! 🍲 Have you tried Shadab or Paradise?"
            lower.contains("cricket") || lower.contains("ipl") ->
                "Watching cricket with friends is pure nostalgia! Which team do you passionately cheer for? 🏏"
            lower.contains("hi") || lower.contains("hello") || lower.contains("namaste") || lower.contains("vanakkam") ->
                "${partner.nativeGreeting} So glad we sparked! Your profile looks so vibrant. How is your day going?"
            lower.contains("weekend") || lower.contains("trek") ->
                "Trekking in the morning and cafe hopping in the evening is my absolute dream weekend! 🏔️ Where did you trek last?"
            else ->
                "That's so fascinating! Honestly, we seem to share so much in common. Tell me more about your favorite weekend spots! ✨"
        }
    }

    fun reportUser(user: UserProfile, reason: String) {
        repository.blockUser(user.id)
        if (_activeChat.value?.user?.id == user.id) {
            _activeChat.value = null
        }
        if (_inspectingProfile.value?.id == user.id) {
            _inspectingProfile.value = null
        }
        refreshDeck()
        showToast("Report submitted for ${user.name}. Account blocked.")
    }

    fun blockUser(user: UserProfile) {
        repository.blockUser(user.id)
        if (_activeChat.value?.user?.id == user.id) {
            _activeChat.value = null
        }
        if (_inspectingProfile.value?.id == user.id) {
            _inspectingProfile.value = null
        }
        refreshDeck()
        showToast("${user.name} has been blocked.")
    }

    fun upgradeToGold(plan: GoldPlan) {
        repository.setGoldMember(true)
        _isGoldSheetOpen.value = false
        showToast("Welcome to Spark Gold! All admirers & features unlocked! 👑")
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun setFilterSheetOpen(open: Boolean) {
        _isFilterSheetOpen.value = open
    }

    fun setGoldSheetOpen(open: Boolean) {
        _isGoldSheetOpen.value = open
    }

    fun setSafetySheetOpen(open: Boolean) {
        _isSafetySheetOpen.value = open
    }

    fun setLanguageDialogOpen(open: Boolean) {
        _isLanguageDialogOpen.value = open
    }

    fun setInspectingProfile(user: UserProfile?) {
        _inspectingProfile.value = user
    }
}
