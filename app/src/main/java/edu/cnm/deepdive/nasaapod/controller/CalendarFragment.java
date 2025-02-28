package edu.cnm.deepdive.nasaapod.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.nasaapod.adapter.DayBinder;
import edu.cnm.deepdive.nasaapod.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentCalendarBinding.inflate(inflater, container, false);
    // TODO: 2/28/25 Initialize UI.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity())
        .get(ApodViewModel.class);
    YearMonth currentMonth = YearMonth.now();
    YearMonth startingMonth = YearMonth.of(1995, Month.JUNE);
    DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
    viewModel
        .getApodMap()
        .observe(getViewLifecycleOwner(), (apodMap) -> {
          binding.calendar.setDayBinder(new DayBinder(apodMap));
          binding.calendar.setup(startingMonth, currentMonth, firstDayOfWeek);
          binding.calendar.scrollToMonth(currentMonth);
        });
    viewModel.setRange(currentMonth.atDay(1));
    // TODO: 2025-02-28 Observe livedata and start asynchronous processes, as necessary.
  }

  @Override
  public void onDestroyView() {
    // TODO: 2/28/25 Release references to binding
    super.onDestroyView();
  }

}
