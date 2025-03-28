package edu.cnm.deepdive.spaceseek.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.kizitonwose.calendar.core.CalendarMonth;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.adapter.DayBinder;
import edu.cnm.deepdive.spaceseek.adapter.HeaderBinder;
import edu.cnm.deepdive.spaceseek.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import edu.cnm.deepdive.spaceseek.model.entity.Apod.MediaType;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Unit;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private static final String TAG = CalendarFragment.class.getSimpleName();

  @Inject
  DayBinder dayBinder;
  @Inject
  HeaderBinder headerBinder;

  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;
  private YearMonth selectedMonth;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    LocalDate firstApodDate = LocalDate.parse(
        getString(R.string.first_apod_date)); // First APOD date
    YearMonth firstApodMonth = YearMonth.from(firstApodDate);
    DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
    YearMonth currentMonth = YearMonth.now(); // Current year and month for setup

    // Set the DayBinder listener for displaying specific APODs
    dayBinder.setListener(this::showApod);

    binding = FragmentCalendarBinding.inflate(inflater, container, false);
    binding.calendar.setDayBinder(dayBinder);
    binding.calendar.setMonthHeaderBinder(headerBinder);
    binding.calendar.setup(firstApodMonth, currentMonth, firstDayOfWeek);
    binding.calendar.setMonthScrollListener(this::handleMonthScroll);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();

    // Observe APOD data and update calendar
    viewModel.getApodMap().observe(owner, this::handleApods);
    viewModel.getYearMonth().observe(owner, this::handleYearMonth);

    // Configure NumberPickers programmatically
    configureNumberPickers();
  }

  private void configureNumberPickers() {
    // Configure Year Picker
    binding.yearPicker.setMinValue(1995); // First APOD year
    binding.yearPicker.setMaxValue(LocalDate.now().getYear()); // Current year
    binding.yearPicker.setValue(LocalDate.now().getYear()); // Default to current year

    // Configure Month Picker
    binding.monthPicker.setMinValue(1); // January
    binding.monthPicker.setMaxValue(12); // December
    binding.monthPicker.setValue(LocalDate.now().getMonthValue()); // Default to current month

    // Configure Day Picker
    binding.dayPicker.setMinValue(1); // First day of month
    binding.dayPicker.setMaxValue(31); // Maximum days in a month
    binding.dayPicker.setValue(LocalDate.now().getDayOfMonth()); // Default to current day

    // Add listeners to update the calendar based on picker changes
    binding.yearPicker.setOnValueChangedListener(
        (picker, oldVal, newVal) -> updateCalendar(newVal, binding.monthPicker.getValue()));
    binding.monthPicker.setOnValueChangedListener(
        (picker, oldVal, newVal) -> updateCalendar(binding.yearPicker.getValue(), newVal));
  }

  private void updateCalendar(int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);
    viewModel.setYearMonth(yearMonth);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @NonNull
  private Unit handleMonthScroll(CalendarMonth calendarMonth) {
    viewModel.setYearMonth(calendarMonth.getYearMonth());
    return Unit.INSTANCE;
  }

  private void handleYearMonth(YearMonth yearMonth) {
    if (!yearMonth.equals(selectedMonth)) {
      binding.calendar.scrollToMonth(yearMonth);
      selectedMonth = yearMonth;
    }
  }

  private void showApod(Apod apod) {
    if (apod.getMediaType() == MediaType.IMAGE) {
      Navigation.findNavController(binding.getRoot())
          .navigate(CalendarFragmentDirections.showImage(apod.getId()));
    } else {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apod.getLowDefUrl().toString()));
      startActivity(intent);
    }
  }

  private void handleApods(Map<LocalDate, Apod> apodMap) {
    Map<LocalDate, Apod> binderMap = dayBinder.getApodMap();
    binderMap.clear();
    binderMap.putAll(apodMap);
    apodMap
        .keySet()
        .stream()
        .map(YearMonth::from)
        .distinct()
        .forEach(binding.calendar::notifyMonthChanged);
  }
}