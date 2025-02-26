package edu.cnm.deepdive.nasaapod.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodRepository;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class ApodViewModel extends ViewModel {

  private static final String TAG = ApodViewModel.class.getSimpleName();

  private final ApodRepository repository;
  private final MutableLiveData<DateRange> dateRange;
  private final LiveData<List<Apod>> apods;
  private final MutableLiveData<Throwable> throwable;

  @Inject
  ApodViewModel(ApodRepository repository) {
    this.repository = repository;
    dateRange = new MutableLiveData<>();
    apods = Transformations.switchMap(dateRange, (range) -> (range.endDate != null)
        ? repository.get(range.startDate, range.endDate)
        : repository.get(range.startDate));
    throwable = new MutableLiveData<>();
  }

  public LiveData<List<Apod>> getApods() {
    return apods;
  }

  public LiveData<Apod> getApod() {
    return repository.get();
  }

  @SuppressLint("CheckResult")
  public void setRange(LocalDate startDate, LocalDate endDate) {
    throwable.setValue(null);
    dateRange.setValue(new DateRange(startDate, endDate));
    //noinspection ResultOfMethodCallIgnored
    repository
        .fetch(startDate, endDate)
        .subscribe(
            () -> {
            },
            this::postThrowable
        );
  }

  @SuppressLint("CheckResult")
  public void setRange(LocalDate startDate) {
    throwable.setValue(null);
    dateRange.setValue(new DateRange(startDate));
    //noinspection ResultOfMethodCallIgnored
    repository
        .fetch(startDate)
        .subscribe(
            () -> {
            },
            this::postThrowable
        );
  }

  /**
   * @noinspection ResultOfMethodCallIgnored
   */
  @SuppressLint("CheckResult")
  public void setToday() {
    throwable.setValue(null);
    repository
        .fetch()
        .subscribe(
            () -> {
            },
            this::postThrowable
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  private record DateRange(LocalDate startDate, LocalDate endDate) {

    public DateRange(LocalDate startDate) {
      this(startDate, null);
    }

  }

}
