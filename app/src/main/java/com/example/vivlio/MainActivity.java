package com.example.vivlio;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//Hello kishan testing //


/*
* Main hub of activity
*
* Holds the bottom navigation bar.
* Selecting an item on the navigation bar replaces the current fragment in nav_host_fragment with the
* fragment corresponding the item pressed.
*
* A few things to note:
* I had to change the layout to a Frame Layout, the default Constraint Layout had a problem with
* fragments overlapping with the first fragment in the background when the app launched.
* Right now there is no fragment opened by default on launch.
* Also I've left in the default generated UI components of Home, Dashboard, and Notifications. They
* aren't connected to anything and should be safe to delete, but I've left them in case anyone wants
* to check out how they were implemented. They use a ViewModel which I don't know anything about.
*
* Each fragment should be ready to be replaced by a real working implementation.
*
* I also haven't looked at Login, that should probably be called right at the top, I'll
* have to double check.
*
* Also right now the nav_host_fragment has a hardcoded margin of 58dp, ideally it should automatically
* sit on top of the nav bar, I'm trying to find a way to do that.
*
* */

public class MainActivity extends AppCompatActivity {

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


        // bottom navigation bar listener
        // replaces fragment in nav_host_fragment with the fragment corresponding to the option pressed
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.navigation_my_request_list:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new MyRequestListFragment()).commit();
                        return true;
                    case R.id.navigation_search:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new SearchFragment()).commit();
                        return true;
                    case R.id.navigation_scan:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new ScanFragment()).commit();
                        return true;
                    case R.id.navigation_my_book_list:
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new MyBookListFragment()).commit();
                        return true;
                    case R.id.navigation_profile:

                        User user =new User("NAME", "test", "EMAIL", "PHONE", "PASSWORD");
                        getIntent().putExtra("User", user);

                        manager.beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).commit();
                        return true;

                }

                return false;
            }
        });
    }


}