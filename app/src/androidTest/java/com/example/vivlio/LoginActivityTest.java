package com.example.vivlio;

import android.util.Log;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    @Test
    public void testSuccessfulLogin(){
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "");


        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "test@vivlio.com");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "pass1234");

        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Login failed", MainActivity.class);
    }

    @Test
    public void testFailedLogin(){
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "");

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "test@vivlio.com");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "pass12");

        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Login succeeded", LoginActivity.class);
    }

    @Test
    public void testEmptyFields(){
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);

        //Empty fields
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login succeeded", LoginActivity.class);

        //empty password
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "test@vivlio.com");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login succeeded", LoginActivity.class);

        //empty username
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "pass1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login succeeded", LoginActivity.class);

        //login
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "test@vivlio.com");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login failed", MainActivity.class);
    }

    @Test
    public void openCreateAccount(){
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);

        solo.clickOnText("new to Vivlio?");

        solo.assertCurrentActivity("Didnt open to Create account", CreateAccountActivity.class);
    }
}
