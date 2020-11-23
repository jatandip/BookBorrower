package com.example.vivlio;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.vivlio.Activities.MainActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;


/**
 * Test class for the Search Fragment
 */
@RunWith(AndroidJUnit4.class)
public class BookTest {
    private Solo solo;
    private Book newBook(){
        return new Book("Game of Thrones", "PotatoBoy", "William Xu", "1837281928309");
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
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * tests current activity to make sure it's book
     */
    @Test
    public void checkActivity(){
        solo.assertCurrentActivity("Wrong Activity", Book.class);
    }

    /**
     * tests getting book description
     */
    @Test
    public void testGetters(){
        Book testBook = newBook();
        assertEquals("Game of Thrones", testBook.getTitle());
        assertEquals("PotatoBoy", testBook.getAuthor());
        assertEquals("William Xu", testBook.getOwner());
        assertEquals("1837281928309", testBook.getISBN());
    }

    /**
     * tests setting and making sure the setters work
     */
    @Test
    public void testSetters(){
        Book testBook = newBook();
        testBook.setTitle("Pokemon");
        assertEquals("Pokemon", testBook.getTitle());
        testBook.setAuthor("Ash Ketchum");
        assertEquals("Ash Ketchum", testBook.getAuthor());
        testBook.setISBN("1234567890");
        assertEquals("1234567890", testBook.getISBN());
        testBook.setOwner("John Jimmy Jones");
        assertEquals("John Jimmy Jones", testBook.getOwner());
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
