package com.example.it314118_fyp.viewController.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.it314118_fyp.R;

import com.example.it314118_fyp.databinding.ActivityMainBinding;
import com.example.it314118_fyp.viewController.home.HomeFragment;
import com.example.it314118_fyp.viewController.profile.ProfileFragment;
import com.example.it314118_fyp.viewController.setting.SettingsFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.profile:
                        replaceFragment(new ProfileFragment());
                        break;
                    case R.id.setting:
                        replaceFragment(new SettingsFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
    }
}