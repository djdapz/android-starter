package io.damo.androidstarter

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import io.damo.androidstarter.instrumentationsupport.*
import io.damo.androidstarter.instrumentationsupport.TestAppContext.Companion.baseFavorites
import io.damo.androidstarter.instrumentationsupport.TestDispatcher.Companion.randomJokes
import org.hamcrest.CoreMatchers.*

class MainScreen(testAppContext: TestAppContext) {

    fun waitForJoke(jokeText: String) {
        waitForText(jokeText)
        onView(withId(R.id.jokeTextView)).check(matches(withText(jokeText)))
    }

    fun assertServerErrorDisplayed() {
        onView(withId(R.id.jokeTextView)).check(matches(withText("Expected server response to be a 200")))
    }

    fun startRefresh() {
        onView(withId(R.id.swipeRefresh)).perform(swipeDown())
    }

    fun clickOnFavoritesTab() {
        onView(withId(R.id.favoritesNavigationItem)).perform(click())
    }

    fun clickOnCategoriesTab() {
        onView(withId(R.id.categoriesNavigationItem)).perform(click())
    }

    fun checkCategoriesTabIsDisplayed() {
        onView(withId(R.id.categoriesList)).check(matches(isDisplayed()))
        checkForText(R.string.categories_title)
    }

    fun clickOnRandomTab() {
        onView(withId(R.id.randomNavigationItem)).perform(click())
    }

    fun checkRandomTabIsDisplayed() {
        onView(withId(R.id.jokeTextView)).check(matches(isDisplayed()))
        checkForText(R.string.random_title)
    }

    fun checkAllCategoriesAreDisplayed() {
        onData(anything()).atPosition(0).check(matches(withText("Explicit")))
        onData(anything()).atPosition(1).check(matches(withText("Nerdy")))
        onData(anything()).atPosition(2).check(matches(withText("Other")))
    }

    fun clickCategoryByName(category: String) {
        onView(withText(category)).perform(click())
    }

    fun checkCategoryIsDisplayed(category: String) {
        onView(withId(R.id.categoryJokesList)).check(matches(isDisplayed()))
        checkForText("$category Jokes")
    }

    fun checkNerdyCategoryJokesAreDisplayed() {
        onData(anything()).atPosition(0).onChildView(withId(R.id.jokeInCategoryList))
            .check(matches(withText(randomJokes[0])))
        onData(anything()).atPosition(1).onChildView(withId(R.id.jokeInCategoryList))
            .check(matches(withText(randomJokes[1])))
        onData(anything()).atPosition(2).onChildView(withId(R.id.jokeInCategoryList))
            .check(matches(withText(randomJokes[2])))
    }

    fun clickUpButton() {
        onView(withContentDescription("Navigate up")).perform(click())
    }

    fun checkBaseFavoritesAreDisplayed() {
        checkForText(baseFavorites[0])
        checkForText(baseFavorites[1])
    }

    fun checkFavoritesTabIsDisplayed() {
        onView(withId(R.id.favoritesList)).check(matches(isDisplayed()))
        checkForText(R.string.favorites_title)
    }

    fun addRandomJokeToFavorites() {
        onView(withId(R.id.addToFavorites)).perform(click())
    }

    fun checkRandomFavoriteIsDisplayedOnce() {
        checkForCountOfText(randomJokes[0], 1)
    }

    fun checkFavoritesButtonIsHiden() {
        onView(withId(R.id.addToFavorites)).check(matches(not(isDisplayed())))
    }

    fun addFirstJokeToFavorites() {
        onData(anything()).atPosition(0).onChildView(withId(R.id.favoriteJoke)).perform(click())
    }

    fun checkFirstNerdyJokeIsDisplayed() {
        checkForText(randomJokes[0])
    }

    fun removeFirstFavorite() {
        onData(anything()).atPosition(0).onChildView(withId(R.id.removeFromFavorites))
            .perform(click())
    }

    fun checkFirstFavoriteIsGone() {
        checkForText(baseFavorites[1])
        checkForAbsenceOfText(baseFavorites[0])
    }
}
