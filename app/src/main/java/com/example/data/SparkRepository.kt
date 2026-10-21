package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SparkRepository {

    private val allSeedProfiles = listOf(
        UserProfile(
            id = "user_1",
            name = "Ananya Sharma",
            age = 24,
            city = "Mumbai",
            state = "Maharashtra",
            distanceKm = 8,
            occupation = "UI/UX Designer",
            bio = "Designing intuitive apps by day, chasing Marine Drive sunsets by dusk. Looking for someone who appreciates soulful indie music, spontaneous street food runs, and lively debates on movies.",
            nativeGreeting = "नमस्ते! (Namaste!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Trekking", "Filter Coffee", "Bollywood", "Badminton", "Street Food", "Indie Music"),
            prompts = listOf(
                ProfilePrompt("My ideal Sunday", "A breezy morning walk along Marine Drive, filter coffee at Cafe Madras, and bookstore hopping."),
                ProfilePrompt("Together we could", "Conquer the Harishchandragad trek and find Mumbai's top vada pav spot.")
            ),
            verified = true,
            height = "5'6\"",
            diet = "Vegetarian",
            drinking = "Socially",
            smoking = "Never",
            horoscope = "Taurus",
            avatarBgColor = 0xFFE11D48
        ),
        UserProfile(
            id = "user_2",
            name = "Rohan Reddy",
            age = 26,
            city = "Hyderabad",
            state = "Telangana",
            distanceKm = 4,
            occupation = "Software Architect & Techie",
            bio = "Believes Hyderabadi Dum Biryani is an emotion. Passionate badminton player, weekend trekker, and tech enthusiast who loves stargazing.",
            nativeGreeting = "నమస్కారం! (Namaskaram!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Biryani", "Cricket", "Badminton", "Startups", "Chai", "Trekking"),
            prompts = listOf(
                ProfilePrompt("I'm convinced that", "A warm cup of Niloufer Irani Chai with osmania biscuits cures any bad day."),
                ProfilePrompt("Bucket list item", "Watching the Indian cricket team play in a World Cup final live!")
            ),
            verified = true,
            height = "5'11\"",
            diet = "Non-Vegetarian",
            drinking = "Socially",
            smoking = "Never",
            horoscope = "Scorpio",
            avatarBgColor = 0xFFF97316
        ),
        UserProfile(
            id = "user_3",
            name = "Priya Sundaram",
            age = 25,
            city = "Chennai",
            state = "Tamil Nadu",
            distanceKm = 12,
            occupation = "Architect & Classical Artist",
            bio = "Restoring heritage architecture while practicing Bharatanatyam. Love early morning beach walks, filter coffee in dabarah-tumbler, and acoustic melodies.",
            nativeGreeting = "வணக்கம்! (Vanakkam!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Carnatic Music", "Filter Coffee", "Architecture", "Badminton", "Books", "Art"),
            prompts = listOf(
                ProfilePrompt("My simple pleasures", "The gentle aroma of degree filter coffee and watching sunrise at Besant Nagar beach."),
                ProfilePrompt("Dating me is like", "Having a permanent guide to the best dosas, book cafes, and tranquil sunset spots in the city.")
            ),
            verified = true,
            height = "5'5\"",
            diet = "Vegetarian",
            drinking = "Never",
            smoking = "Never",
            horoscope = "Virgo",
            avatarBgColor = 0xFF0D9488
        ),
        UserProfile(
            id = "user_4",
            name = "Arjun Nair",
            age = 27,
            city = "Kochi",
            state = "Kerala",
            distanceKm = 15,
            occupation = "Marine Officer & Travel Vlogger",
            bio = "Exploring God's Own Country between sailing schedules. Passionate about backwater kayaking, capturing sunset reflections, and discovering local seafood spots.",
            nativeGreeting = "നമസ്കാരം! (Namaskaram!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Photography", "Kerala Backwaters", "Trekking", "Football", "Kathakali", "Seafood"),
            prompts = listOf(
                ProfilePrompt("The key to my heart", "Freshly cooked Thalassery dum biryani and rainy evening conversations."),
                ProfilePrompt("Never have I ever", "Turned down a sudden road trip through the misty tea hills of Munnar.")
            ),
            verified = true,
            height = "6'0\"",
            diet = "Non-Vegetarian",
            drinking = "Socially",
            smoking = "Never",
            horoscope = "Leo",
            avatarBgColor = 0xFF2563EB
        ),
        UserProfile(
            id = "user_5",
            name = "Tanvi Verma",
            age = 23,
            city = "Delhi NCR",
            state = "Delhi",
            distanceKm = 18,
            occupation = "Journalist & Food Critic",
            bio = "Writing essays on culinary memories and heritage monuments. Always in search of crisp jalebis, heartfelt poetry, and fellow cricket fans.",
            nativeGreeting = "नमस्ते! (Namaste!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Street Food", "Chai", "Bollywood", "Cricket", "Standup Comedy", "Literature"),
            prompts = listOf(
                ProfilePrompt("Teach me something about", "Your favorite lesser-known neighborhood cafe or historic lane."),
                ProfilePrompt("Green flag I adore", "Someone who happily splits street chaat and isn't glued to their phone.")
            ),
            verified = true,
            height = "5'4\"",
            diet = "Vegetarian",
            drinking = "Never",
            smoking = "Never",
            horoscope = "Cancer",
            avatarBgColor = 0xFF7C3AED
        ),
        UserProfile(
            id = "user_6",
            name = "Vikram Patel",
            age = 28,
            city = "Bengaluru",
            state = "Karnataka",
            distanceKm = 6,
            occupation = "Product Lead & Runner",
            bio = "Building startups in Koramangala. Weekends belong to marathon training, CTR butter dosas, and indie acoustic gigs.",
            nativeGreeting = "నమస్కారం! (Namaskara!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=800&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Badminton", "Filter Coffee", "Trekking", "Startups", "Indie Rock", "Biryani"),
            prompts = listOf(
                ProfilePrompt("My Sunday ritual", "A peaceful 10k run around Cubbon park followed by crisp ghee podi dosa."),
                ProfilePrompt("A quick confession", "I judge tech events by the coffee quality and badminton courts nearby.")
            ),
            verified = true,
            height = "5'10\"",
            diet = "Eggetarian",
            drinking = "Socially",
            smoking = "Never",
            horoscope = "Gemini",
            avatarBgColor = 0xFF059669
        ),
        UserProfile(
            id = "user_7",
            name = "Kavya Iyer",
            age = 25,
            city = "Hyderabad",
            state = "Telangana",
            distanceKm = 7,
            occupation = "Digital Artist & Animator",
            bio = "Illustrating Indian mythological folklore with cyber aesthetic. Enjoys old Telugu tunes, badminton doubles, and exploring street art.",
            nativeGreeting = "నమస్కారం! (Namaskaram!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Telugu Cinema", "Biryani", "Art", "Badminton", "Travel", "Chai"),
            prompts = listOf(
                ProfilePrompt("Dream creative project", "An animated anthology of Deccan legends with live folk instruments."),
                ProfilePrompt("Best hangout", "Late evening coffee at Durgam Cheruvu bridge talking about art.")
            ),
            verified = true,
            height = "5'6\"",
            diet = "Vegetarian",
            drinking = "Socially",
            smoking = "Never",
            horoscope = "Aries",
            avatarBgColor = 0xFFDC2626
        ),
        UserProfile(
            id = "user_8",
            name = "Dev Malhotra",
            age = 26,
            city = "Delhi NCR",
            state = "Delhi",
            distanceKm = 22,
            occupation = "Fitness Coach & Guitarist",
            bio = "Gym by dawn, acoustic jams around bonfires at night. Looking for a high-energy partner who loves trekking and spontaneous road trips.",
            nativeGreeting = "सत श्री अकाल! (Sat Sri Akaal!)",
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=800&auto=format&fit=crop&q=80"
            ),
            interests = listOf("Cricket", "Gym", "Trekking", "Music", "Road Trips", "Street Food"),
            prompts = listOf(
                ProfilePrompt("My driving playlist", "Nuanced Sufi rock, Coke Studio, and evergreen acoustic melodies."),
                ProfilePrompt("A non-negotiable", "Respecting street animals and staying humble no matter what.")
            ),
            verified = true,
            height = "6'1\"",
            diet = "Non-Vegetarian",
            drinking = "Socially",
            smoking = "Never",
            horoscope = "Sagittarius",
            avatarBgColor = 0xFFD97706
        )
    )

    // Pre-seeded Admirers ("Likes You" grid for Gold)
    val admirers = listOf(
        allSeedProfiles[1],
        allSeedProfiles[2],
        allSeedProfiles[3],
        allSeedProfiles[6]
    )

    private val _profiles = MutableStateFlow<List<UserProfile>>(allSeedProfiles)
    val profiles: StateFlow<List<UserProfile>> = _profiles.asStateFlow()

    private val _matches = MutableStateFlow<List<Match>>(
        listOf(
            Match(
                id = "match_1",
                user = allSeedProfiles[0], // Ananya Sharma
                matchedAtTimestamp = System.currentTimeMillis() - 1000 * 60 * 30,
                lastMessage = "Hey! Love your bio! Filter coffee or Irani chai? ☕",
                lastMessageTime = "10:15 AM",
                unreadCount = 1
            ),
            Match(
                id = "match_2",
                user = allSeedProfiles[2], // Priya Sundaram
                matchedAtTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 4,
                lastMessage = "வணக்கம்! That architecture sketch on your profile is amazing!",
                lastMessageTime = "Yesterday",
                unreadCount = 0
            )
        )
    )
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "match_1" to listOf(
                ChatMessage(
                    id = "msg_1_1",
                    matchId = "match_1",
                    senderId = "user_1",
                    text = "Namaste! It's a Spark! ✨",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 20,
                    timeFormatted = "10:10 AM",
                    status = MessageStatus.READ
                ),
                ChatMessage(
                    id = "msg_1_2",
                    matchId = "match_1",
                    senderId = "current_user",
                    text = "Namaste Ananya! Great to match with you. How's Mumbai today?",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 18,
                    timeFormatted = "10:12 AM",
                    status = MessageStatus.READ
                ),
                ChatMessage(
                    id = "msg_1_3",
                    matchId = "match_1",
                    senderId = "user_1",
                    text = "Hey! Love your bio! Filter coffee or Irani chai? ☕",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                    timeFormatted = "10:15 AM",
                    status = MessageStatus.READ
                )
            ),
            "match_2" to listOf(
                ChatMessage(
                    id = "msg_2_1",
                    matchId = "match_2",
                    senderId = "user_3",
                    text = "வணக்கம்! That architecture sketch on your profile is amazing!",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 4,
                    timeFormatted = "Yesterday",
                    status = MessageStatus.READ
                )
            )
        )
    )
    val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

    private val _isGoldMember = MutableStateFlow(false)
    val isGoldMember: StateFlow<Boolean> = _isGoldMember.asStateFlow()

    private val _blockedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val blockedUserIds: StateFlow<Set<String>> = _blockedUserIds.asStateFlow()

    fun filterProfiles(filter: FilterState): List<UserProfile> {
        val blocked = _blockedUserIds.value
        return allSeedProfiles.filter { profile ->
            if (profile.id in blocked) return@filter false
            if (filter.selectedCity != "All Cities" && profile.city != filter.selectedCity) return@filter false
            if (profile.distanceKm > filter.maxDistanceKm) return@filter false
            if (profile.age < filter.minAge || profile.age > filter.maxAge) return@filter false
            if (filter.verifiedOnly && !profile.verified) return@filter false
            if (filter.selectedInterests.isNotEmpty()) {
                val matchesAny = profile.interests.any { it in filter.selectedInterests }
                if (!matchesAny) return@filter false
            }
            true
        }
    }

    fun addMatch(user: UserProfile): Match {
        val newMatch = Match(
            id = "match_${user.id}_${System.currentTimeMillis()}",
            user = user,
            matchedAtTimestamp = System.currentTimeMillis(),
            lastMessage = "You both sparked! Say hello 👋",
            lastMessageTime = "Just now",
            unreadCount = 0
        )
        _matches.value = listOf(newMatch) + _matches.value
        return newMatch
    }

    fun sendMessage(matchId: String, text: String, isVoice: Boolean = false, durationSec: Int = 0): ChatMessage {
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            matchId = matchId,
            senderId = "current_user",
            text = text,
            isVoiceNote = isVoice,
            voiceDurationSec = durationSec,
            timestamp = System.currentTimeMillis(),
            timeFormatted = "Just now",
            status = MessageStatus.SENT
        )
        val currentList = _chatMessages.value[matchId] ?: emptyList()
        _chatMessages.value = _chatMessages.value + (matchId to (currentList + newMsg))

        // Update last message in match
        _matches.value = _matches.value.map { match ->
            if (match.id == matchId) {
                match.copy(
                    lastMessage = if (isVoice) "🎤 Voice note (${durationSec}s)" else text,
                    lastMessageTime = "Just now"
                )
            } else match
        }
        return newMsg
    }

    fun receiveSimulatedReply(matchId: String, partnerUserId: String, text: String): ChatMessage {
        val reply = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            matchId = matchId,
            senderId = partnerUserId,
            text = text,
            isVoiceNote = false,
            voiceDurationSec = 0,
            timestamp = System.currentTimeMillis(),
            timeFormatted = "Just now",
            status = MessageStatus.READ
        )
        val currentList = _chatMessages.value[matchId] ?: emptyList()
        _chatMessages.value = _chatMessages.value + (matchId to (currentList + reply))

        // Update last message in match
        _matches.value = _matches.value.map { match ->
            if (match.id == matchId) {
                match.copy(
                    lastMessage = text,
                    lastMessageTime = "Just now",
                    unreadCount = match.unreadCount + 1
                )
            } else match
        }
        return reply
    }

    fun blockUser(userId: String) {
        _blockedUserIds.value = _blockedUserIds.value + userId
        // Remove matching chats
        _matches.value = _matches.value.filter { it.user.id != userId }
    }

    fun setGoldMember(active: Boolean) {
        _isGoldMember.value = active
    }
}
