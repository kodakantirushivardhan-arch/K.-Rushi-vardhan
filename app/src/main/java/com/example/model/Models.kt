package com.example.model

data class ProfilePrompt(
    val question: String,
    val answer: String
)

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val city: String,
    val state: String,
    val distanceKm: Int,
    val occupation: String,
    val bio: String,
    val nativeGreeting: String,
    val photoUrls: List<String>,
    val interests: List<String>,
    val prompts: List<ProfilePrompt>,
    val verified: Boolean = true,
    val height: String = "5'7\"",
    val diet: String = "Vegetarian",
    val drinking: String = "Socially",
    val smoking: String = "Never",
    val horoscope: String = "Leo",
    val avatarBgColor: Long = 0xFFE11D48
)

enum class MessageStatus {
    SENT,
    DELIVERED,
    READ
}

data class ChatMessage(
    val id: String,
    val matchId: String,
    val senderId: String,
    val text: String,
    val isVoiceNote: Boolean = false,
    val voiceDurationSec: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormatted: String = "10:30 AM",
    val status: MessageStatus = MessageStatus.READ
)

data class Match(
    val id: String,
    val user: UserProfile,
    val matchedAtTimestamp: Long,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0
)

data class FilterState(
    val maxDistanceKm: Int = 50,
    val selectedCity: String = "All Cities",
    val selectedInterests: Set<String> = emptySet(),
    val verifiedOnly: Boolean = false,
    val minAge: Int = 21,
    val maxAge: Int = 32
)

enum class GoldPlan(
    val title: String,
    val durationMonths: Int,
    val priceInr: Int,
    val tag: String? = null
) {
    ONE_MONTH("1 Month", 1, 399, null),
    THREE_MONTHS("3 Months", 3, 799, "POPULAR"),
    TWELVE_MONTHS("12 Months", 12, 1499, "BEST VALUE")
}
