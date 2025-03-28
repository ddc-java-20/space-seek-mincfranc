package edu.cnm.deepdive.spaceseek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.databinding.HeaderCalendarBinding;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.inject.Inject;

public class HeaderBinder implements MonthHeaderFooterBinder<ViewContainer> {

  private final LayoutInflater inflater;

  @Inject
  public HeaderBinder(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @Override
  public ViewContainer create(View view) {
    return new HeaderHolder(view);
  }

  @Override
  public void bind(ViewContainer container, CalendarMonth calendarMonth) {
    ((HeaderHolder) container).bind(calendarMonth);
  }

  private class HeaderHolder extends ViewContainer {

    private final HeaderCalendarBinding binding;

    public HeaderHolder(View view) {
      super(view);
      binding = HeaderCalendarBinding.bind(view);
    }

    public void bind(CalendarMonth calendarMonth) {
      binding.monthYear.setText(calendarMonth.getYearMonth().toString());
      ViewGroup headerGroup = binding.dayNames;
      DayOfWeek firstDayOfWeek = DayOfWeek.of(1);
      for (int i = 0; i < 7; i++) {
        TextView dayName = (TextView) inflater.inflate(R.layout.day_header, headerGroup, false);
        dayName.setText(
            firstDayOfWeek.plus(i).getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        headerGroup.addView(dayName);
      }
    }
  }
}