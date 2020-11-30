package com.example.vivlio;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.AddBook;
import com.example.vivlio.Activities.LoginActivity;
import com.example.vivlio.Activities.MainActivity;
import com.example.vivlio.Fragments.MyBookListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
     * tests to see if filling out all fields will allow the book to be uploaded (photo is optional)
     */
    @Test
    public void successAdd(){
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "");

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "test@test.com");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "test1234");

        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_my_book_list));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        FloatingActionButton add = (FloatingActionButton)solo.getView(R.id.addBtn);
        solo.clickOnView(add);

        solo.assertCurrentActivity("AddBook not open", AddBook.class);
        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "9780394800790");

        ImageButton confirm = (ImageButton) solo.getView(R.id.button_upload);

        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook failed", MainActivity.class);
    }

    /**
     * tests each possible outcome of fields not being filled out before making sure that when
     * fields are filled out, it works
     */
    @Test
    public void failureAdd(){
        solo.assertCurrentActivity("Didnt open to Login", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "");

        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETusername), "test@test.com");
        solo.enterText((EditText) solo.getView(R.id.LOGIN_ETpassword), "test1234");

        solo.clickOnImageButton(0);

        solo.assertCurrentActivity("Login failed", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.navigation_my_book_list));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        FloatingActionButton add = (FloatingActionButton)solo.getView(R.id.addBtn);
        solo.clickOnView(add);

        solo.assertCurrentActivity("AddBook not open", AddBook.class);
        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        ImageButton confirm = (ImageButton) solo.getView(R.id.button_upload);
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "1234512345");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook succeeded", AddBook.class);

        solo.enterText((EditText) solo.getView(R.id.edit_title), "League of Legends");
        solo.enterText((EditText) solo.getView(R.id.edit_author), "William");
        solo.enterText((EditText) solo.getView(R.id.edit_isbn), "9781234567897");
        solo.clickOnView(confirm);
        solo.assertCurrentActivity("AddBook failed", MainActivity.class);


    }
    @Test
    public void testCamera(){
        //not sure about yet
    }
    @Test
    public void testGallery(){
        //not sure about yet
    }
    @Test
    public void testScan(){
        //not implemented yet
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
