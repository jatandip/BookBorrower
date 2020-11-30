package com.example.vivlio;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Testing to see if the user can navigate to the profile fragment and
 * click on edit profile button, Checks if user can save his changes to his profile
 */


public class ProfileTesting {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

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
     * Logs in as a user, navigates to the profile fragment,
     * clicks the edit button to see if the user can edit the profile information
     * then saves the changes made
     */
    @Test
    public void checkShow() {
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.navigation_profile));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        FloatingActionButton edit = (FloatingActionButton)solo.getView(R.id.editButton);
        solo.clickOnView(edit);

        ImageButton confirm = (ImageButton) solo.getView(R.id.saveButton);
        solo.clickOnView(confirm);

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
