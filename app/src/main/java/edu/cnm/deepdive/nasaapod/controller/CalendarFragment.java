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
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.adapter.DayBinder;
import edu.cnm.deepdive.nasaapod.databinding.FragmentCalendarBinding;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.viewmodel.ApodViewModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

  private static final String TAG = CalendarFragment.class.getSimpleName();

  @Inject
  DayBinder dayBinder;
  // TODO: 3/3/25 Add injected header binder 
  
  private FragmentCalendarBinding binding;
  private ApodViewModel viewModel;
  private YearMonth selectedMonth;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    LocalDate firstApodDate = LocalDate.parse(getString(R.string.first_apod_date));
    YearMonth firstApodMonth = YearMonth.from(firstApodDate);
    DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault())
        .getFirstDayOfWeek();
    YearMonth currentMonth = YearMonth.now();
    // TODO: 3/3/25 Attach a listener to dayBinder. 
    binding = FragmentCalendarBinding.inflate(inflater, container, false);
    binding.calendar.setDayBinder(dayBinder);
    // TODO: 3/3/25 Set month header binder on calendar. 
    binding.calendar.setup(firstApodMonth, currentMonth, firstDayOfWeek);
    // TODO: 3/3/25 Set a month scroll listener on calendar. 
    return binding.getRoot();
  }


  //
//    apodMap.keySet().stream(): This line creates a stream of keys from the apodMap, which is a Map<LocalDate, Apod>. The keySet() method returns a Set of keys, and stream() converts the Set into a stream.
//      .map(YearMonth::from): This line applies a mapping function to each element in the stream. The map method transforms each LocalDate key into a YearMonth object using the YearMonth::from method reference. The YearMonth::from method takes a LocalDate and returns a YearMonth object representing the year and month of that date.
//      .distinct(): This line ensures that the resulting stream contains distinct YearMonth objects. It removes any duplicate YearMonth objects that may have been produced due to multiple LocalDate keys falling within the same month.
//    .forEach(binding.calendar::notifyMonthChanged): This line performs an action for each YearMonth object in the stream. The forEach method iterates over the stream and applies the provided action to each element. In this case, the action is specified as binding.calendar::notifyMonthChanged, which is a method reference to the notifyMonthChanged method of the binding.calendar object.
//
//  So, the code iterates over the keys of the apodMap, converts each LocalDate key to a YearMonth object, ensures distinct months, and then calls the notifyMonthChanged method on the binding.calendar object for each distinct month. This is likely used to update the calendar view when new data is available, ensuring that the calendar is notified of changes in the displayed months.
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity())
        .get(ApodViewModel.class);
    viewModel
        .getApodMap()
        .observe(getViewLifecycleOwner(), this::handleApods);
    // TODO: 2025-02-28 Observe livedata and start asynchronous processes, as necessary.
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

  @Override
  public void onDestroyView() {
    // TODO: 2/28/25 Release references to binding
    super.onDestroyView();
  }

}
