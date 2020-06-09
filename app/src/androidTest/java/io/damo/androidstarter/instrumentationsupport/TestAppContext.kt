package io.damo.androidstarter.instrumentationsupport

import io.damo.androidstarter.StarterApp

class TestAppContext(val app: StarterApp) {

    val testComponent = TestAppComponent(app)

    init {
        app.appComponent = testComponent
    }

    fun tearDown() {
        device().setOrientationNatural()
        testComponent.shutdownServer()
        testComponent.appPreferences.clear()

        baseFavorites.forEach { testComponent.favoritesRepo.add(it) }
    }

    companion object {
        val baseFavorites = listOf(
            "Most tough men eat nails for breakfast. Chuck Norris does all of his grocery shopping at Home Depot.",
            "When Chuck Norris throws exceptions, it's across the room."
        )
    }

}
