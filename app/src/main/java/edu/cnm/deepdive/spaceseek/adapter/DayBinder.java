package edu.cnm.deepdive.spaceseek.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.databinding.DayCalendarBinding;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import java.time.LocalDate;
import java.util.Map;
import javax.inject.Inject;

public class DayBinder implements MonthDayBinder<ViewContainer> {

  private final Map<LocalDate, Apod> apodMap;
  private final Drawable selectedCircle;
  private final Drawable rangeCircle;
  private final String apodTooltipFormat;

  @Inject
  public DayBinder(Context context, Map<LocalDate, Apod> apodMap) {
    this.apodMap = apodMap;
    apodTooltipFormat = context.getString(R.string.apod_tooltip_format);
    selectedCircle = context.getDrawable(R.drawable.selected_date_circle);
    rangeCircle = context.getDrawable(R.drawable.circle_background);
  }

  @Override
  public ViewContainer create(View view) {
    return new DayHolder(view);
  }

  @Override
  public void bind(ViewContainer container, CalendarDay calendarDay) {
    ((DayHolder) container).bind(calendarDay);
  }

  private class DayHolder extends ViewContainer {

    private final DayCalendarBinding binding;

    public DayHolder(View view) {
      super(view);
      binding = DayCalendarBinding.bind(view);
    }

    public void bind(CalendarDay calendarDay) {
      binding.getRoot().setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
      binding.getRoot().setBackground(null);
      Apod apod = apodMap.get(calendarDay.getDate());

      if (apod != null && calendarDay.getPosition() == DayPosition.MonthDate) {
        binding.getRoot().setBackground(selectedCircle);
        binding.getRoot().setTooltipText(
            String.format(apodTooltipFormat, apod.getMediaType(), apod.getTitle()));
      } else {
        binding.getRoot().setBackground(null);
        binding.getRoot().setTooltipText(null);
      }
    }
  }
}