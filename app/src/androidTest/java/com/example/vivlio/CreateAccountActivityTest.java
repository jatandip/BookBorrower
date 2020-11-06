package com.example.vivlio;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CreateAccountActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.clickOnText("new to Vivlio?");
        solo.assertCurrentActivity("Create Account didnt open", CreateAccountActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    @Test
    public void testEmptyFields(){
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETemail), "");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETfirstName), "");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETlastName), "");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETpassword), "");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETphone), "");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETrepassword), "");
        solo.clickOnImageButton(0);
    }

    @Test
    public void testDiffPass() {
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETemail), "test6969@vivlio.com");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETusername), "Kiriyu.Coco");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETfirstName), "Coco");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETlastName), "Kaichou");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETphone), "11490");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETpassword), "pass1234");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETrepassword), "pass123");
        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Created account", CreateAccountActivity.class);
    }

    //YOU GOTTA MANUALLY DELETE THE USER FROM AUTH AND FIRESTORE AFTER YOU RUN THIS
    @Test
    public void createAccount() {
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETemail), "test6969@vivlio.com");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETusername), "Kiriyu.Coco");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETfirstName), "Coco");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETlastName), "Kaichou");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETphone), "11490");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETpassword), "pass1234");
        solo.enterText((EditText) solo.getView(R.id.CREACC_ETrepassword), "pass1234");
        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Account creation failed", LoginActivity.class);
    }
}
