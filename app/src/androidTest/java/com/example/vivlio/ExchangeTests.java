package com.example.vivlio;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.BorrowTaskActivity;
import com.example.vivlio.Activities.CreateAccountActivity;
import com.example.vivlio.Activities.LendTaskActivity;
import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Activities.RecievingTaskActivity;
import com.example.vivlio.Activities.ReturningTaskActivity;
import com.example.vivlio.Fragments.ScanFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ExchangeTests {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "jatandip@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "jatan1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Didnt open to fragment", MainActivity.class);
    }

    @Test
    public void testBorrow() {
        FloatingActionButton fab = (FloatingActionButton) solo.getView(R.id.EXCHANGE_FABborrow);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Didnt open to list", BorrowTaskActivity.class);
    }

    @Test
    public void testLend() {
        FloatingActionButton fab = (FloatingActionButton) solo.getView(R.id.EXCHANGE_FABlend);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Didnt open to list", LendTaskActivity.class);
    }

    @Test
    public void testReturn() {
        FloatingActionButton fab = (FloatingActionButton) solo.getView(R.id.EXCHANGE_FABreturning);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Didnt open to list", ReturningTaskActivity.class);
    }

    @Test
    public void testReceive() {
        FloatingActionButton fab = (FloatingActionButton) solo.getView(R.id.EXCHANGE_FABrecieving);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("Didnt open to list", RecievingTaskActivity.class);
    }
}
