package com.example.vivlio;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.CreateAccountActivity;
import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Activities.Mybook_Avalible;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * A test that checks if the user can edit a book.
 */
public class EditBookTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

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
     * Logs into the user using the provided username and password
     * Tests to make sure it is in the correct activity
     * Clicks on the MyBookList fragment
     * Adds the information for a new book and clicks confirm to add the book
     * Checks to see if you can edit the book information
     */
    @Test
    public void checkShow() {
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "vanmaren@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "timvm1234");
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_my_book_list));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        FloatingActionButton add = (FloatingActionButton) solo.getView(R.id.addBtn);
        solo.clickOnView(add);

        solo.enterText((EditText) solo.getView(R.id.edit_author), "testingAuthor");
        solo.enterText((EditText) solo.getView(R.id.edit_title), "testingTitle");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "testingIsbn");

        ImageButton confirm = (ImageButton) solo.getView(R.id.button_upload);
        solo.clickOnView(confirm);

        // test details for avalible
        solo.clickOnText("Available");
        solo.sleep(1000);
        solo.clickInList(0);
        solo.assertCurrentActivity("Didn't click", Mybook_Avalible.class);
        solo.sleep(1000);


        ImageButton edit = (ImageButton) solo.getView(R.id.editBtn);
        solo.clickOnView(edit);





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
