package com.example.vivlio;
import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Test class for the Search Fragment
 */
@RunWith(AndroidJUnit4.class)
public class SearchTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Sets up the Robotium object wit the appropriate activity information
     * @throws Exception
     */
    @Before
    public void SetUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Searches for t, should return result test. Then switches to search books and then finds "g" which shpuld return Game of Thrones.
     */
    @Test
    public void checkShow() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        assertTrue(solo.waitForText("Users", 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "j");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("jatan", 1, 2000));
        solo.clickOnView(solo.getView(R.id.search_switch));
        solo.clearEditText((EditText) solo.getView(R.id.searchTermEditText));
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "y");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("Year",1,2000));

    }

    /**
     * closes the activities
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
