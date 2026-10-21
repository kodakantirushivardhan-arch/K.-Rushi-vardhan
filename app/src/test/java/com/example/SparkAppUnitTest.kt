package com.example

import com.example.data.SparkRepository
import com.example.localization.AppLanguage
import com.example.localization.LocalizationManager
import com.example.model.FilterState
import com.example.model.GoldPlan
import com.example.viewmodel.SparkViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SparkAppUnitTest {

    private lateinit var repository: SparkRepository
    private lateinit var viewModel: SparkViewModel

    @Before
    fun setup() {
        repository = SparkRepository()
        viewModel = SparkViewModel(repository)
    }

    @Test
    fun testMultilingualSupport_allLanguagesProvided() {
        AppLanguage.values().forEach { lang ->
            val strings = LocalizationManager.getStrings(lang)
            assertNotNull("Strings for $lang should not be null", strings)
            assertTrue("Discover text should not be empty for $lang", strings.discover.isNotEmpty())
            assertTrue("Matches text should not be empty for $lang", strings.matches.isNotEmpty())
            assertTrue("Its a spark text should not be empty for $lang", strings.itsASpark.isNotEmpty())
        }
    }

    @Test
    fun testLanguageSwitching_updatesViewModelState() {
        assertEquals(AppLanguage.ENGLISH, viewModel.currentLanguage.value)
        
        viewModel.setLanguage(AppLanguage.TELUGU)
        assertEquals(AppLanguage.TELUGU, viewModel.currentLanguage.value)
        assertEquals("అన్వేషించండి", viewModel.strings.value.discover)

        viewModel.setLanguage(AppLanguage.HINDI)
        assertEquals(AppLanguage.HINDI, viewModel.currentLanguage.value)
        assertEquals("खोजें", viewModel.strings.value.discover)

        viewModel.setLanguage(AppLanguage.TAMIL)
        assertEquals(AppLanguage.TAMIL, viewModel.currentLanguage.value)
        assertEquals("கண்டறியவும்", viewModel.strings.value.discover)

        viewModel.setLanguage(AppLanguage.MALAYALAM)
        assertEquals(AppLanguage.MALAYALAM, viewModel.currentLanguage.value)
        assertEquals("കണ്ടെത്തുക", viewModel.strings.value.discover)
    }

    @Test
    fun testLocationAndInterestFiltering() {
        // Filter by Hyderabad
        val hydFilter = FilterState(selectedCity = "Hyderabad")
        val hydProfiles = repository.filterProfiles(hydFilter)
        assertTrue(hydProfiles.isNotEmpty())
        assertTrue(hydProfiles.all { it.city == "Hyderabad" })

        // Filter by Biryani interest
        val biryaniFilter = FilterState(selectedInterests = setOf("Biryani"))
        val biryaniProfiles = repository.filterProfiles(biryaniFilter)
        assertTrue(biryaniProfiles.isNotEmpty())
        assertTrue(biryaniProfiles.all { it.interests.contains("Biryani") })
    }

    @Test
    fun testSwipingAndMatchingFlow() {
        val initialIndex = viewModel.cardIndex.value
        assertEquals(0, initialIndex)

        // Swipe Like
        viewModel.swipeLike()
        assertEquals(1, viewModel.cardIndex.value)

        // Rewind
        viewModel.rewind()
        assertEquals(0, viewModel.cardIndex.value)
    }

    @Test
    fun testSparkGoldSubscription() {
        assertFalse(viewModel.isGoldMember.value)
        viewModel.upgradeToGold(GoldPlan.THREE_MONTHS)
        assertTrue(viewModel.isGoldMember.value)
    }

    @Test
    fun testSafetyBlockUser() {
        val initialProfiles = repository.profiles.value
        val userToBlock = initialProfiles.first()

        viewModel.blockUser(userToBlock)
        val remaining = repository.filterProfiles(FilterState())
        assertFalse("Blocked user should not appear in deck", remaining.any { it.id == userToBlock.id })
    }
}
