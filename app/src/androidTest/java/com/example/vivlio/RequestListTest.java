package com.example.vivlio;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.LocationActivity;
import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Activities.RequestDetailActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Testing to see if the user can navigate to the Request List fragment and then filter the list
 * by selecting the tabs at the top.
 */
public class RequestListTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void SetUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Starts on main activity
     * Selects My Requests tab on the bottom nav bar
     * Waits 2 seconds
     * Switches to Accepted tab and waits 2 seconds
     * Switches to Borrowed tab and waits 2 seconds
     * Switches to All tab and waits 2 seconds
     */
    @Test
    public void checkMyRequestListFragment() {
        solo.assertCurrentActivity("Didn't open to Login Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");

        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_my_request_list));
        solo.sleep(1000);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnText("Pending");
        solo.sleep(1000);
        solo.clickOnText("Accepted");
        solo.sleep(1000);
        solo.clickOnText("Borrowed");
        solo.sleep(1000);
        solo.clickOnText("All");
        solo.sleep(1000);

        solo.clickOnText("Accepted");
        solo.sleep(1000);

        solo.clickInList(0);
        solo.assertCurrentActivity("Didn't click", RequestDetailActivity.class);

        solo.sleep(1000);

        solo.clickOnView((Button) solo.getView(R.id.btn_request_location));
        solo.assertCurrentActivity("Location activity didn't launch", LocationActivity.class);

        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.done_button));
        solo.assertCurrentActivity("Didn't return from location", RequestDetailActivity.class);

        solo.sleep(1000);
    }

    /**
     * Closes the activities.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


}
