package com.example.mallet;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.mallet.frontend.view.common.activity.ActivityMain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ActivityMainTest {

    @Rule
    public ActivityScenarioRule<ActivityMain> activityScenarioRule = new ActivityScenarioRule<>(ActivityMain.class);

    @Test
    public void testNavigationToFragmentHome() {
        // Click on the home item in the bottom navigation
        Espresso.onView(withId(R.id.bottom_nav_home)).perform(ViewActions.click());

        // Verify that the FragmentHome is displayed
        Espresso.onView(withId(R.id.mainFl)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToFragmentDatabase() {
        // Click on the library item in the bottom navigation
        Espresso.onView(withId(R.id.bottom_nav_library)).perform(ViewActions.click());

        // Verify that the FragmentDatabase is displayed
        Espresso.onView(withId(R.id.mainFl)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToCreateNewSetDialog() {
        // Click on the add new item in the bottom navigation
        Espresso.onView(withId(R.id.bottom_nav_add_new)).perform(ViewActions.click());

        // Verify that the "Create Set" and "Create Group" options are displayed in the dialog
        Espresso.onView(withText("Create Set")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withText("Create Group")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Optionally, perform additional checks for dialog elements and interactions
    }
}
