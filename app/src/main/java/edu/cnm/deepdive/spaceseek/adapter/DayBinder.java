package edu.cnm.deepdive.spaceseek.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.databinding.DayCalendarBinding;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

@FragmentScoped
public class DayBinder implements MonthDayBinder<ViewContainer> {

  private static final String TAG = DayBinder.class.getSimpleName();

  private final Map<LocalDate, Apod> apodMap;
  private final String apodTooltipFormat;
  private final String[] mediaTypes;

  private final Drawable selectedCircle; // Drawable for selected date
  private final Drawable rangeCircle;    // Drawable for date range

  private OnApodClickListener listener;

  @Inject
  public DayBinder(@ActivityContext Context context) {
    this.apodMap = new HashMap<>();
    apodTooltipFormat = context.getString(R.string.apod_tooltip_format);
    mediaTypes = context.getResources().getStringArray(R.array.media_types);

    // Load drawables for selected date and date ranges
    selectedCircle = context.getDrawable(R.drawable.selected_date_circle);
    rangeCircle = context.getDrawable(R.drawable.circle_background);
// TODO: 3/27/2025  reference shape from drawable for both circles 
    listener = (apod) -> {
    }; // Initialize listener with a NO-OP action
  }

  @NotNull
  @Override
  public ViewContainer create(@NotNull View view) {
    return new DayHolder(view);
  }

  @Override
  public void bind(@NotNull ViewContainer holder, CalendarDay calendarDay) {
    ((DayHolder) holder).bind(calendarDay);
  }

  public Map<LocalDate, Apod> getApodMap() {
    return apodMap;
  }

  public void setListener(OnApodClickListener listener) {
    this.listener = listener;
  }

  @FunctionalInterface
  public interface OnApodClickListener {
    void onApodClick(Apod apod);
  }

  public class DayHolder extends ViewContainer {

    private static final OnClickListener NO_OP_LISTENER = (v) -> {
    };
    private final DayCalendarBinding binding;

    private Apod apod;

    public DayHolder(@NotNull View view) {
      super(view);
      binding = DayCalendarBinding.bind(view);
    }

    public void bind(CalendarDay calendarDay) {
      TextView dayText = binding.getRoot();
      dayText.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

      // Reset background and style
      dayText.setBackground(null);

      // TODO: 2025-02-28 Use information from apodMap to modify style/content of widgets.
      Apod apod = apodMap.get(calendarDay.getDate());
      if (apod != null) {
        this.apod = apod;
        dayText.setClickable(true);
        dayText.setFocusable(true);
        dayText.setSoundEffectsEnabled(true);
        dayText.setOnClickListener(this::translateClick);

        // Apply appropriate appearance for selected date or date range
        if (calendarDay.getDate().equals(apod.getDate())) {
          dayText.setBackground(selectedCircle);
        } else {
          dayText.setBackground(rangeCircle);
        }

        dayText.setTextAppearance(
            (calendarDay.getPosition() == DayPosition.MonthDate)
                ? R.style.CalendarTextAppearance_AvailableDay
                : R.style.CalendarTextAppearance_AvailableDay_OutOfMonth
        );
        dayText.setTooltipText(String.format(apodTooltipFormat,
            mediaTypes[apod.getMediaType().ordinal()], apod.getTitle().strip()));
      } else {
        this.apod = null;
        dayText.setClickable(false);
        dayText.setFocusable(false);
        dayText.setSoundEffectsEnabled(false);
        dayText.setOnClickListener(NO_OP_LISTENER);
        dayText.setBackground(null);
        dayText.setTextAppearance(R.style.CalendarTextAppearance);
        dayText.setTooltipText(null);
      }
    }

    private void translateClick(View view) {
      listener.onApodClick(apod);
    }
  }
}