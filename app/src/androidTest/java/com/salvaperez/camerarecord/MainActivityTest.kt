package com.salvaperez.camerarecord


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.salvaperez.camerarecord.assertions.RecyclerViewMatcher
import com.salvaperez.camerarecord.presentation.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest{

    @Rule
    @JvmField
    var activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {}


    @Test
    fun open_camera_add_video_and_click_in_video(){
        addVideo()

        onView(withRecyclerView(R.id.rvVideo).atPosition(0))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private fun addVideo(){
        val resultData = Intent()
        resultData.putExtra("data", "photo")
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(toPackage("com.android.camera2")).respondWith(result)

        onView(withId(R.id.fabStartCamera)).perform(click())
        intended(toPackage("com.android.camera2"))
    }
}