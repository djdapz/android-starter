package io.damo.androidstarter.favorites

import io.damo.androidstarter.MainScreen
import io.damo.androidstarter.instrumentationsupport.TestAppContext
import io.damo.androidstarter.instrumentationsupport.startMainActivity

class FavoritesTest(testAppContext: TestAppContext) {

    private val mainScreen = MainScreen(testAppContext)

    fun testNavigation() {
        startMainActivity()

        mainScreen.clickOnFavoritesTab()

        mainScreen.checkFavoritesTabIsDisplayed()
    }

    fun testFavoritesHoldsDefaultFavorites() {
        startMainActivity()

        mainScreen.clickOnFavoritesTab()

        mainScreen.checkFavoritesTabIsDisplayed()

        mainScreen.checkBaseFavoritesAreDisplayed()
    }

    fun testAddFavoriteFromRandom() {
        startMainActivity()

        mainScreen.clickOnFavoritesTab()

        mainScreen.checkFavoritesTabIsDisplayed()
        mainScreen.checkBaseFavoritesAreDisplayed()

        mainScreen.clickOnRandomTab()

        mainScreen.addRandomJokeToFavorites()
        mainScreen.checkFavoritesButtonIsHiden()

        mainScreen.clickOnFavoritesTab()

        mainScreen.checkBaseFavoritesAreDisplayed()
        mainScreen.checkRandomFavoriteIsDisplayedOnce()
    }

    fun testAddFavoriteFromCategory() {
        startMainActivity()

        mainScreen.clickOnCategoriesTab()
        mainScreen.checkCategoriesTabIsDisplayed()

        mainScreen.clickCategoryByName("Nerdy")
        mainScreen.checkNerdyCategoryJokesAreDisplayed()

        mainScreen.addFirstJokeToFavorites()

        mainScreen.clickOnFavoritesTab()
        mainScreen.checkFavoritesTabIsDisplayed()

        mainScreen.checkFirstNerdyJokeIsDisplayed()
    }

    fun testRemoveFavorites() {
        startMainActivity()

        mainScreen.clickOnFavoritesTab()
        mainScreen.checkFavoritesTabIsDisplayed()

        mainScreen.removeFirstFavorite()
        mainScreen.checkFirstFavoriteIsGone()

    }
}