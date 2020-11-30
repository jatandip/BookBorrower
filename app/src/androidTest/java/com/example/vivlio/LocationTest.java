package com.example.vivlio;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.AddBook;
import com.example.vivlio.Activities.LocationActivity;
import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Activities.Mybook_Accepted;
import com.example.vivlio.Activities.RequestDetailActivity;
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
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Log in to vanmaren@ualberta.ca
     * @throws Exception
     */
    @Before
    public void SetUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Didn't open to Login Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login failed", MainActivity.class);
    }

    /**
     * Enter the location activity from a RequestDetailActivity and back out
     */
    @Test
    public void enterFromRequestList() {
        solo.clickOnView(solo.getView(R.id.navigation_my_request_list));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnText("Accepted");
        solo.sleep(1000);

        solo.clickInList(0);
        solo.assertCurrentActivity("Didn't click", RequestDetailActivity.class);
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.btn_request_location));
        solo.assertCurrentActivity("Location activity didn't launch", LocationActivity.class);
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.done_button));
        solo.assertCurrentActivity("Didn't return from location", RequestDetailActivity.class);
        solo.sleep(1000);


    }

    /**
     * Enter the location activity from a MyBook_Accepted Activity and back out
     */
    @Test
    public void enterFromBookList() {
        solo.clickOnView(solo.getView(R.id.navigation_my_book_list));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.sleep(1000);
        solo.clickOnText("Accepted");
        solo.sleep(1000);

        solo.clickInList(0);
        solo.assertCurrentActivity("Didn't click", Mybook_Accepted.class);
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.mapsBtn));
        solo.assertCurrentActivity("Location activity didn't launch", LocationActivity.class);
        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.done_button));
        solo.assertCurrentActivity("Didn't return from location", Mybook_Accepted.class);
        solo.sleep(1000);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

