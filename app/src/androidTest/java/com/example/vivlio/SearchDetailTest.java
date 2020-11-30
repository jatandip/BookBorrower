package com.example.vivlio;
import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Activities.SearchDetailActivity;
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
 * Test class for the Search Detail Activity
 */
@RunWith(AndroidJUnit4.class)
public class SearchDetailTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Sets up the Robotium object with the appropriate activity information.
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
     * Searches for c which should return the Candy Shop War, Clicking on the book should open SearchDetailActivity which should have the owner
     * with status pending or available.
     */
    @Test
    public void checkSearchDetail() {
        solo.assertCurrentActivity("Didn't open to Login Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        solo.clickOnView(solo.getView(R.id.search_switch));
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "can");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("Candy",1,2000));
        solo.clickOnText("Candy");
        solo.assertCurrentActivity("Wrong Activity", SearchDetailActivity.class);
        assertTrue(solo.waitForText("Available",1,2000) || solo.waitForText("Pending",1,2000));
        if(solo.waitForText("Available", 1, 2000)) {
            solo.clickOnText("Available");
            assertTrue((solo.waitForText("Pending",1,2000)));
        }

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
