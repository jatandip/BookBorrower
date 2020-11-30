package com.example.vivlio.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.vivlio.Fragments.MyBookListFragment;
import com.example.vivlio.Fragments.MyRequestListFragment;
import com.example.vivlio.Fragments.ProfileFragment;
import com.example.vivlio.Fragments.ScanFragment;
import com.example.vivlio.Fragments.SearchFragment;
import com.example.vivlio.R;
import com.example.vivlio.Models.User;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;


/**
 * MainActivity
 *
 * Main hub of activity. Holds the bottom navigation bar. Selecting an item on the navigation bar
 * replaces the current fragment in nav_host_fragment with the fragment corresponding the item
 * pressed.
 *
 * Notifications are handled in this activity. On each Firebase Snapshot, it checks a dictionary
 * with the old values of a book's ISBN and status and sees if the current database has any changes
 * that should trigger a notification. Then it updates the dictionary with the current values. If a
 * notification is triggered a batch appears on the relevant menu item in the navigation bar.
 *
 * The two conditions for notifications are:
 * Case 1: a book in a user's "owned" collection has a change in status from "available" to "pending"
 * Case 2: a book in a user's "requested" collection has a change in status from "pending" to "accepted"
 *
 * Issues:
 * There is no fragment opened by default on launch, this may be something to change in the future.
 * The nav_host_fragment has a hardcoded margin of 58dp, that should maybe automatically sit on top
 * of the nav bar.
 * Notifications are just a small badge on the relevant menu item in the navigation bar, they
 * could maybe be more prominent like next to the changed item in the list as well.
 *
 * */

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference requestedCollectionReference;
    private CollectionReference ownedCollectionReference;

    private HashMap<String, String> oldBookData;
    private HashMap<String, String> oldRequestData;

    private BadgeDrawable requestsNotificationBadge;
    private BadgeDrawable booksNotificationBadge;

    /**
     * Handles the task of the MainActivity.
     * The navigation bar has a listener to launch the corresponding fragment when an item is
     * pressed.
     * The notifications are handled by checking changes the the database.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_my_request_list, R.id.navigation_search, R.id.navigation_scan,
                R.id.navigation_my_book_list, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // initialize badges
        requestsNotificationBadge = navView.getOrCreateBadge(R.id.navigation_my_request_list);
        booksNotificationBadge = navView.getOrCreateBadge(R.id.navigation_my_book_list);

        requestsNotificationBadge.setVisible(false);
        booksNotificationBadge.setVisible(false);

        // bottom navigation bar listener
        // replaces fragment in nav_host_fragment with the fragment corresponding to the option pressed
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.navigation_my_request_list:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new MyRequestListFragment()).commit();
                        requestsNotificationBadge.setVisible(false);
                        return true;
                    case R.id.navigation_search:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new SearchFragment()).commit();
                        return true;
                    case R.id.navigation_scan:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new ScanFragment()).commit();
                        return true;
                    case R.id.navigation_my_book_list:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new MyBookListFragment()).commit();
                        booksNotificationBadge.setVisible(false);
                        return true;
                    case R.id.navigation_profile:

                        User user =new User("NAME", "test", "EMAIL", "PHONE");
                        getIntent().putExtra("User", user);

                        manager.beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).commit();
                        return true;

                }

                return false;
            }
        });

        // launch scan activity right away
        navView.setSelectedItemId(R.id.navigation_scan);

        // handle notifications
        // notification should show in two cases:
        // 1 when one of the users owned books changes its status from "available" to "pending"
        // 2 when one of the users requested books changes its status from "pending" to "accepted"

        // set up firebase access
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        requestedCollectionReference = db.collection("users/" + uid + "/requested");
        ownedCollectionReference = db.collection("users/" + uid + "/owned");

        oldBookData = new HashMap<>();
        oldRequestData = new HashMap<>();

        // handling case 1:
        ownedCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    /*
                    try {
                        Log.d("old book data", oldBookData.get(doc.getId()));
                    } catch (Exception e) {
                        Log.d("old book data", "fail");
                    }
                    try {
                        Log.d("current book data", doc.getData().get("status").toString());
                    } catch (Exception e) {
                        Log.d("current book data", "fail");
                    }
                    */

                    // see if book is in hashmap
                    try {
                        if (oldBookData.get(doc.getId()).equals("available") && doc.getData().get("status").toString().equals("pending")) {
                            booksNotificationBadge.setVisible(true);
                        }
                    } catch (Exception e) {
                        Log.d("Hashmap Error", "oldBookData probably doesn't have anything in it");
                    }

                    // update hashmap
                    if (doc.getData().containsKey("status")) {
                        oldBookData.put(doc.getId(), doc.getData().get("status").toString());
                    }

                }
            }
        });

        // handling case 2:
        requestedCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    /*
                    try {
                        Log.d("old request data", oldRequestData.get(doc.getId()));
                    } catch (Exception e) {
                        Log.d("old request data", "fail");
                    }

                    try {
                        Log.d("current request data", doc.getData().get("status").toString());
                    } catch (Exception e) {
                        Log.d("current request data", "fail");
                    }
                    */

                    // see if book is in hashmap
                    try {
                        if (oldRequestData.get(doc.getId()).equals("pending") && doc.getData().get("status").toString().equals("accepted")) {
                            requestsNotificationBadge.setVisible(true);
                        }
                    } catch (Exception e) {
                        Log.d("Hashmap Error", "oldRequestData probably doesn't have anything in it");
                    }

                    // update hashmap
                    if (doc.getData().containsKey("status")) {
                        oldRequestData.put(doc.getId(), doc.getData().get("status").toString());
                    }
                }
            }
        });
    }
}