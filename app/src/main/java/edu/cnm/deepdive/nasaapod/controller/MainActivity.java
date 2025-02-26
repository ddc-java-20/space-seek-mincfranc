package edu.cnm.deepdive.nasaapod.controller;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.nasaapod.databinding.ActivityMainBinding;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;
  private ApodViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    // TODO: 2025-02-26 Attach listeners to view widgets (fields in binding).
    // TODO: 2025-02-26 Update state of view widgets.
    setContentView(binding.getRoot());
    viewModel = new ViewModelProvider(this).get(ApodViewModel.class);
    viewModel
        .getApod()
        .observe(this, (apod) ->
            Log.d(TAG, "onChanged: " + apod));
    viewModel.setToday();
  }

}