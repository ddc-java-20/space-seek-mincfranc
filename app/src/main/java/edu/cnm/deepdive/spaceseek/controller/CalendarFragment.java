package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.adapter.DayBinder;
import edu.cnm.deepdive.spaceseek.adapter.HeaderBinder;
import edu.cnm.deepdive.spaceseek.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;
import java.time.LocalDate;
import java.time.YearMonth;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentCalendarBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

    setupCalendar();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void setupCalendar() {
    YearMonth currentMonth = YearMonth.now();
    LocalDate today = LocalDate.now();

    binding.calendarView.setup(currentMonth.minusMonths(6), currentMonth.plusMonths(6),
        today.getDayOfWeek());
    binding.calendarView.scrollToMonth(currentMonth);

    binding.calendarView.setDayBinder(new DayBinder(requireContext(), (day, apod) -> {
      if (apod != null) {
        viewModel.getApod(day());
      }
    }));

    binding.calendarView.setMonthHeaderBinder(new HeaderBinder());
  }
}