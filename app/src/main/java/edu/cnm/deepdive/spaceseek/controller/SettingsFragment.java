package edu.cnm.deepdive.spaceseek.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SettingsFragment extends PreferenceFragmentCompat {

  private static final String TAG = SettingsFragment.class.getSimpleName();
  private SharedPreferences.OnSharedPreferenceChangeListener listener;

  @Override
  public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey); // Load preferences dynamically
    setupDobPreference();
  }

  @Override
  public void onResume() {
    super.onResume();
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
    listener = (sharedPreferences, key) -> {
      switch (key) {
        case "dark_mode":
          boolean isDarkMode = sharedPreferences.getBoolean(key, false);
          applyTheme(isDarkMode);
          break;
        case "notifications":
          // Future notification handling here
          break;
        case "dob":
          String dob = sharedPreferences.getString(key, "");
          if (dob != null && isValidDate(dob)) {
            fetchPersonalizedApods(dob);
          } else {
            Log.w(TAG, "Invalid date format for DOB: " + dob);
          }
          break;
      }
    };
    preferences.registerOnSharedPreferenceChangeListener(listener);
  }

  @Override
  public void onPause() {
    super.onPause();
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
    preferences.unregisterOnSharedPreferenceChangeListener(listener);
  }

  private void setupDobPreference() {
    EditTextPreference dobPreference = findPreference("dob");
    if (dobPreference != null) {
      dobPreference.setOnBindEditTextListener(editText ->
          editText.setHint("YYYY-MM-DD")); // Set placeholder for correct format
    }
  }

  private boolean isValidDate(String dob) {
    try {
      LocalDate.parse(dob); // Validate if the input matches the ISO-8601 date format
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private void applyTheme(boolean isDarkMode) {
    int themeId = isDarkMode ? R.style.AppTheme_Dark : R.style.AppTheme_Light;
    requireActivity().setTheme(themeId);
    requireActivity().recreate(); // TODO: Replace activity recreation with selective UI updates
  }

  private void fetchPersonalizedApods(String dob) {
    ApodViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel.fetchApodsForDateAcrossYears(dob); // Fetch personalized APODs for the given DOB
  }

}