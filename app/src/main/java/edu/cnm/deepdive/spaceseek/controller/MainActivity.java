package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.databinding.ActivityMainBinding;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfig;
  private ApodViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI(); // Inflate the layout and set the content view
    setupNavigation(); // Configure navigation for the app
    setupRandomApodFeature(); // Set up button for fetching random APODs
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfig);
  }

  private void setupUI() {
    // Inflate the layout using ViewBinding
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
  }

  private void setupNavigation() {
    // Configure AppBar to include all top-level destinations
    appBarConfig = new AppBarConfiguration.Builder(
        R.id.calendar_fragment,
        R.id.image_fragment,
        R.id.settings_fragment,
        R.id.favorites_fragment // Include FavoritesFragment as a top-level destination
    ).build();

    // Obtain the NavController from the NavHostFragment
    navController = ((NavHostFragment) binding.navHostFragment.getFragment())
        .getNavController();

    // Set up ActionBar with the NavController and AppBarConfiguration
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
  }

  private void setupRandomApodFeature() {
    // Initialize ViewModel for APOD operations
    viewModel = new ViewModelProvider(this).get(ApodViewModel.class);

    // Find the button in the layout and set up its functionality
    Button randomButton = findViewById(R.id.random_button);
    randomButton.setOnClickListener((view) -> {
      // Fetch 5 random APODs when the button is clicked
      viewModel.fetchRandomApods(5);
    });
  }

}