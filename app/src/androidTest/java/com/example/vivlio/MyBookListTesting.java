package com.example.vivlio;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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
 * Test class for the Search Fragment
 */
@RunWith(AndroidJUnit4.class)
public class MyBookListTesting {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

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

    
    @Test
    public void checkShow() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_my_book_list));
        //assertTrue(solo.waitForText("Users", 1, 2000));
        Activity current = solo.getCurrentActivity();
        TabItem tabs = (TabItem) solo.getView(R.id.tab_all);
        //solo. clickOnView(solo.getView(tabs.getChildAt(0)));








        //solo.assertCurrentActivity("<Error Message>", AddBook.class);
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













