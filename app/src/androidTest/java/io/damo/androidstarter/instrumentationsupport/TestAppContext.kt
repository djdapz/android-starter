package io.damo.androidstarter.instrumentationsupport

import io.damo.androidstarter.StarterApp

class TestAppContext(val app: StarterApp) {

    val testComponent = TestAppComponent(app)

    init {
        app.appComponent = testComponent
    }

    fun tearDown() {
        testComponent.shutdownServer()
        testComponent.appPreferences.clear()
    }
}
