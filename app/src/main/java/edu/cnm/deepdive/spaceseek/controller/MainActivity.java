package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.databinding.ActivityMainBinding;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;
import edu.cnm.deepdive.spaceseek.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfig;
  private LoginViewModel loginViewModel;
  private ApodViewModel apodViewModel;
  /**
   * @noinspection deprecation
   */
  private GoogleSignInAccount account;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI(); // Inflate the layout and set the content view
    setupNavigation(); // Configure navigation for the app
//    setupRandomApodFeature(); // Set up button for fetching random APODs
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


  private void setupViewModel() {
    ViewModelProvider provider = new ViewModelProvider(this);
    loginViewModel = provider.get(LoginViewModel.class);
    loginViewModel
        .getAccount()
        .observe(this, account -> {
          binding.bottomNavigationView.setVisibility(account == null ? View.GONE : View.VISIBLE);
          this.account = account;
          invalidateMenu();
          // TODO: 3/29/2025 If account is null and in other fragments not prelogin or login, then navigate to prelogin 
        });

    apodViewModel = provider.get(ApodViewModel.class);
  }

  
  private void setupNavigation() {
    // Configure AppBar to include all top-level destinations
    appBarConfig = new AppBarConfiguration.Builder(
        R.id.calendar_fragment,
        R.id.favorites_fragment,
        R.id.birthday_fragment
    ).build();

    // Obtain the NavController from the NavHostFragment
    navController = ((NavHostFragment) binding.navHostFragment.getFragment())
        .getNavController();

    // Set up ActionBar with the NavController and AppBarConfiguration
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
    NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
  }

//  private void setupRandomApodFeature() {
    // Initialize ViewModel for APOD operations
//    viewModel = new ViewModelProvider(this).get(ApodViewModel.class);

    // Find the button in the layout and set up its functionality
//    Button randomButton = findViewById(R.id.random_button);
//    randomButton.setOnClickListener((view) -> {
      // Fetch 5 random APODs when the button is clicked
//      viewModel.fetchRandomApods(5);
//    });
//  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    return super.onOptionsItemSelected(item);
    // TODO: 3/29/2025 Check to see if signout option was selected, if so, invoke viewModelSignOut, viewModel will be doing the sign out 
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    return super.onPrepareOptionsMenu(menu);
    // TODO: 3/29/2025 Show or hide sign out menu option depending on whether account field is null or not 
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
    // TODO: 3/29/2025 Use getMenuInflater to inflate the menu resource with sign out option and attach to this menu 
  }
}