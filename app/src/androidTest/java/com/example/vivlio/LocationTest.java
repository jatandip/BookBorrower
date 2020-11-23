package com.example.vivlio;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.AddBook;
import com.example.vivlio.Activities.LocationActivity;
import com.example.vivlio.Activities.MainActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test class for the Search Fragment
 */
@RunWith(AndroidJUnit4.class)
public class LocationTest {
    private Solo solo;

    private LocationActivity testLocationSet(){
        return new LocationActivity(69.69, -69.69);
    }

    private LocationActivity testLocationDefault(){
        return new LocationActivity();
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void SetUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * tests current activity to make sure it's book
     */
    @Test
    public void checkActivity() {
        solo.assertCurrentActivity("Wrong Activity", AddBook.class);
    }

    /**
     * unfinished
     */
    @Test
    public void testMarkerLocation(){
        LocationActivity location = testLocationDefault();
        //unsure
    }

    /**
     * unfinished
     */
    @Test
    public void testMapReady(){
        //unsure
    }

    /**
     * tests to see if borrower pressing cancel will bring back to initial activity
     */
    @Test
    public void testCancelBorrowerSuccess(){
        //for borrower
        LocationActivity location = testLocationDefault();
        solo.assertCurrentActivity("LocationActivity not open", LocationActivity.class);
        solo.clickOnButton("Cancel");
        //location not yet implemented in code
    }

    /**
     * tests to see if lender pressing cancel will bring back to initial activity
     */
    @Test
    public void testCancelLenderSuccess(){
        //for lender
        LocationActivity location = testLocationSet();
        solo.assertCurrentActivity("LocationActivity not open", LocationActivity.class);
        solo.clickOnButton("Cancel");
        //location not yet implemented in code
    }

    /**
     * tests to see if borrower pressing confirm will bring back to initial activity
     */
    @Test
    public void testConfirmBorrowerSuccess(){
        //for borrower
        LocationActivity location = testLocationDefault();
        solo.assertCurrentActivity("LocationActivity not open", LocationActivity.class);
        solo.clickOnButton("Confirm");
        //location not yet implemented in code
    }

    /**
     * tests to see if lender (after picking location on map) pressing confirm will bring back
     * to initial activity, returning an intent with longitude and latitude
     */
    @Test
    public void testConfirmLenderSuccess(){
        //for lender
        LocationActivity location = testLocationSet();
        solo.assertCurrentActivity("LocationActivity not open", LocationActivity.class);
        //make sure marker is on map
        solo.clickOnButton("Confirm");
        //location not yet implemented in code, intent to make sure long and lat are correct
    }

    /**
     * tests to see if lender (without picking location on map) will not be allowed to return
     * to initial activity
     */
    @Test
    public void testConfirmLenderFailure(){
        //for lender
        LocationActivity location = testLocationSet();
        solo.assertCurrentActivity("LocationActivity not open", LocationActivity.class);
        //make sure there is no marker on map
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("LocationActivity succeeded", LocationActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

