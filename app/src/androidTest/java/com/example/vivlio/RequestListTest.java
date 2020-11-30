package com.example.vivlio;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.MainActivity;
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
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

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
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_my_request_list));
        solo.sleep(2000);

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnText("Pending");
        solo.sleep(2000);
        solo.clickOnText("Accepted");
        solo.sleep(2000);
        solo.clickOnText("Borrowed");
        solo.sleep(2000);
        solo.clickOnText("All");
        solo.sleep(2000);


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
