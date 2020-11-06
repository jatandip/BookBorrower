package com.example.vivlio;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
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
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

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
     * Searches for c which should return the Lion, The With and the Wardrobe by C.S. Lewis. Clicking on the book should open SearchDetailActivity which should have the owner test2
     * with status pending or available.
     */
    @Test
    public void checkSearchDetail() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_search));
        solo.clickOnView(solo.getView(R.id.search_switch));
        solo.enterText((EditText) solo.getView(R.id.searchTermEditText), "w");
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.search_button));
        assertTrue(solo.waitForText("Lion",1,2000));
        solo.clickOnText("Witch");
        solo.assertCurrentActivity("Wrong Activity", SearchDetailActivity.class);
        assertTrue(solo.waitForText("test",1,2000));
        if(solo.waitForText("available", 1, 2000)) {
            solo.clickOnText("available");
            assertTrue((solo.waitForText("pending",1,2000)));
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
