package io.damo.androidstarter.instrumentationsupport

import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import io.damo.androidstarter.R
import io.damo.androidstarter.StarterApp
import junit.framework.TestCase.fail
import org.hamcrest.CoreMatchers

fun startMainActivity() {
    onView(withId(R.id.startMainActivity)).perform(click())
}

fun device(): UiDevice =
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

fun waitForButtonEnabled(text: String, isEnabled: Boolean = true, timeoutInMs: Long = 500) {
    waitForObject(
        By.textContains(text).enabled(isEnabled),
        timeoutInMs
    )
}

fun stringFromResId(@StringRes stringRes: Int): String =
    ApplicationProvider
        .getApplicationContext<StarterApp>()
        .getString(stringRes)

fun checkForCountOfText(text: String, count: Int) =
    if (count == 0) checkForAbsenceOfText(text)
    else device()
        .wait(Until.findObjects(By.textContains(text)), 0)
        ?.run { ViewMatchers.assertThat("Wrong Count", size, CoreMatchers.`is`(count)) }
        ?: fail("Timed out waiting for object with text : $text")

fun checkForAbsenceOfText(text: String) = device()
    .wait(Until.findObjects(By.textContains(text)), 0)
    ?.run { fail("Expected 0, Found $size occurrences of your text: $text") }

fun checkForText(string: String) = waitForText(string, 0)

fun checkForText(@StringRes stringRes: Int) = waitForText(stringRes, 0)

fun waitForText(@StringRes stringRes: Int, timeoutInMs: Long = 500) =
    waitForText(stringFromResId(stringRes), timeoutInMs)

fun waitForText(text: String, timeoutInMs: Long = 500) =
    waitForObject(By.textContains(text), timeoutInMs)

fun waitForObject(bySelector: BySelector, timeoutInMs: Long) {
    device()
        .wait(Until.findObject(bySelector), timeoutInMs)
        ?: fail("Timed out waiting for object by selector: $bySelector")
}
