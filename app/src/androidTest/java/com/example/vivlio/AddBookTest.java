package com.example.vivlio;

import android.app.Activity;
import android.hardware.camera2.CameraDevice;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.AddBook;
import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Fragments.MyBookListFragment;
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
public class AddBookTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void SetUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        Activity activity = rule.getActivity();
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");
        solo.clickOnView(solo.getView(R.id.LOGIN_TVlogin));
        solo.clickOnView(solo.getView(R.id.navigation_my_book_list));
        solo.clickOnView(solo.getView(R.id.addBtn));
    }

    /**
     * tests current activity to make sure it's book
     */
    @Test
    public void checkActivity() {
        solo.assertCurrentActivity("Wrong Activity", AddBook.class);
    }

    /**
     * tests to see if filling out all fields will allow the book to be uploaded (photo is optional)
     */
    @Test
    public void successAdd(){
        solo.assertCurrentActivity("AddBook not open", AddBook.class);
        solo.enterText((EditText) solo.getView(R.id.edit_title), "The Cat in the Hat");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "Dr. Seuss");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "9780394800011");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook failed", MainActivity.class);
    }

    /**
     * tests each possible outcome of fields not being filled out before making sure that when
     * fields are filled out, it works
     */
    @Test
    public void failureAdd(){
        solo.assertCurrentActivity("AddBook not open", AddBook.class);
        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "The Cat in the Hat");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "Dr. Seuss");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "9780394800011");
        solo.clickOnView(solo.getView(R.id.TV_upload));
        solo.assertCurrentActivity("AddBook failed", MainActivity.class);


    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
