package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                } else if (id == R.id.nav_account) {
                    startActivity(new Intent(BaseActivity.this, MyAccount.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(BaseActivity.this, Setting.class));
                } else if (id == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
