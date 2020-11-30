package com.example.vivlio;
import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.LoginActivity;
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
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

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
     * Searches for j, should return result jatandip. Then switches to search books and then finds "c" which should return the Candy Shop Wars.
     */
    @Test
    public void checkShow() {
        solo.assertCurrentActivity("Didn't open to Login Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        assertTrue(solo.waitForText("Users", 1, 2000));
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "jat");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("jatan", 1, 2000));
        solo.clickOnView(solo.getView(R.id.search_switch));
        solo.clearEditText((EditText) solo.getView(R.id.searchTermEditText));
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "can");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("Candy",1,2000));

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
