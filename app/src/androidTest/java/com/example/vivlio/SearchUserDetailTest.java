package com.example.vivlio;
import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Activities.SearchDetailActivity;
import com.example.vivlio.Activities.SearchUserDetailActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SearchUserDetailTest {

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
     * Searches for t which should return TIMVM Clicking on the user should open SearchUserDetailActivity which should have the contact details
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
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "jat");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("jatan",1,2000));
        solo.clickOnText("jatan");
        solo.assertCurrentActivity("Wrong Activity", SearchUserDetailActivity.class);
        assertTrue(solo.waitForText("jatandip",1,2000));

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
