package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

  private static final String TAG = SettingsActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings); // Inflate layout for settings
    setupActionBar();
  }

  private void setupActionBar() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.settings_label); // Title for Settings
      getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back navigation
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed(); // Handle back button navigation
    return super.onSupportNavigateUp();
  }

}